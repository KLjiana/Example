package com.kaleblangley.count_craft.kubejs;

import com.google.gson.JsonObject;
import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.impl.ICountSerializer;
import com.kaleblangley.count_craft.impl.ShapelessRecipeAccessor;
import com.kaleblangley.count_craft.init.RecipeSerializerInit;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CountShapelessRecipe extends ShapelessKubeJSRecipe implements ICountRecipe {
    private final Int2IntOpenHashMap index2count;

    public CountShapelessRecipe(ShapelessKubeJSRecipe kubeJSRecipe, Int2IntOpenHashMap index2count) {
        this(((ShapelessRecipeAccessor) kubeJSRecipe).getShapelessRecipe(), kubeJSRecipe.kjs$getIngredientActions(), kubeJSRecipe.kjs$getModifyResult(), kubeJSRecipe.kjs$getStage(), index2count);
    }

    public CountShapelessRecipe(ShapelessRecipe original, List<IngredientAction> kjs$getIngredientActions, @Nullable ModifyRecipeResultCallback modifyResult, String stage, Int2IntOpenHashMap index2count) {
        super(original, kjs$getIngredientActions, modifyResult, stage);
        index2count.defaultReturnValue(0);
        this.index2count = index2count;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        List<Ingredient> ingredients = getIngredients();
        Int2IntMap index2count = getIndex2count();
        boolean[] used = new boolean[ingredients.size()];

        Int2IntMap slotToIngredientIndex = new Int2IntOpenHashMap();

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack input = container.getItem(slot);
            if (input.isEmpty()) continue;

            for (int i = 0; i < ingredients.size(); i++) {
                if (used[i]) continue;
                Ingredient ing = ingredients.get(i);
                if (ing.test(input)) {
                    slotToIngredientIndex.put(slot, i);
                    used[i] = true;
                    break;
                }
            }
        }

        for (Int2IntMap.Entry entry : slotToIngredientIndex.int2IntEntrySet()) {
            int slot = entry.getIntKey();
            int ingredientIndex = entry.getIntValue();

            int countIndex = ingredientIndex + 1;
            if (index2count.containsKey(countIndex)) {
                int requiredCount = index2count.get(countIndex);
                if (requiredCount > 1) {
                    container.removeItem(slot, requiredCount - 1);
                }
            }
        }

        return super.getRemainingItems(container);
    }



    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        List<Ingredient> ingredients = new ArrayList<>(getIngredients());
        Int2IntMap index2count = getIndex2count();

        boolean[] used = new boolean[ingredients.size()];

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack inputStack = container.getItem(slot);
            if (inputStack.isEmpty()) continue;

            boolean matched = false;

            for (int i = 0; i < ingredients.size(); i++) {
                if (used[i]) continue;

                Ingredient ing = ingredients.get(i);
                if (ing.test(inputStack)) {
                    int index = i + 1;
                    if (index2count.containsKey(index)) {
                        int requiredCount = index2count.get(index);
                        if (inputStack.getCount() < requiredCount) {
                            return false;
                        }
                    }

                    used[i] = true;
                    matched = true;
                    break;
                }
            }

            if (!matched) return false;
        }

        for (int i = 0; i < ingredients.size(); i++) {
            if (!used[i] && !ingredients.get(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }


    @Override
    public Int2IntOpenHashMap getIndex2count() {
        return index2count;
    }

    @Override
    public NonNullList<Ingredient> getIngredientList() {
        return getIngredients();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SHAPELESS.get();
    }

    public static class Serializer implements ICountSerializer, RecipeSerializer<CountShapelessRecipe> {
        private static final RecipeSerializer<ShapelessRecipe> SHAPELESS = UtilsJS.cast(RegistryInfo.RECIPE_SERIALIZER.getValue(new ResourceLocation("crafting_shapeless")));
        
        @Override
        public @NotNull CountShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe shapelessRecipe = SHAPELESS.fromJson(id, json);
            List<IngredientAction> kjs$getIngredientActions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs:modify_result")) {
                modifyResult = RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }

            String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            ShapelessKubeJSRecipe jsRecipe = new ShapelessKubeJSRecipe(shapelessRecipe, kjs$getIngredientActions, modifyResult, stage);
            return new CountShapelessRecipe(jsRecipe, fromJson(json));  
        }

        @Override
        public CountShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe shapelessRecipe = SHAPELESS.fromNetwork(id, buf);
            int flags = buf.readByte();
            List<IngredientAction> kjs$getIngredientActions = (flags & 1) != 0 ? IngredientAction.readList(buf) : List.of();
            String stage = (flags & 2) != 0 ? buf.readUtf() : "";
            ShapelessKubeJSRecipe jsRecipe =  new ShapelessKubeJSRecipe(shapelessRecipe, kjs$getIngredientActions, (ModifyRecipeResultCallback)null, stage);
            return new CountShapelessRecipe(jsRecipe, fromNetwork(buf));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CountShapelessRecipe r) {
            SHAPELESS.toNetwork(buf, r);
            int flags = 0;
            if (r.kjs$getIngredientActions() != null && !r.kjs$getIngredientActions().isEmpty()) {
                flags |= 1;
            }

            if (!r.kjs$getStage().isEmpty()) {
                flags |= 2;
            }

            buf.writeByte(flags);
            if (r.kjs$getIngredientActions() != null && !r.kjs$getIngredientActions().isEmpty()) {
                IngredientAction.writeList(buf, r.kjs$getIngredientActions());
            }

            if (!r.kjs$getStage().isEmpty()) {
                buf.writeUtf(r.kjs$getStage());
            }
            tooNetwork(buf, r);
        }
    }
}
