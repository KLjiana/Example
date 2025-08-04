package com.kaleblangley.count_craft.kubejs;

import com.google.gson.JsonObject;
import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.impl.ICountSerializer;
import com.kaleblangley.count_craft.init.RecipeSerializerInit;
import com.kaleblangley.count_craft.mixin.ShapedKubeJSRecipeAccessor;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CountShapedRecipe extends ShapedKubeJSRecipe implements ICountRecipe {
    private final Int2IntOpenHashMap index2count;

    public CountShapedRecipe(ShapedKubeJSRecipe kubeJSRecipe, Int2IntOpenHashMap index2count) {
        this(
                kubeJSRecipe.getId(),
                kubeJSRecipe.getGroup(),
                kubeJSRecipe.category(),
                kubeJSRecipe.getWidth(),
                kubeJSRecipe.getHeight(),
                kubeJSRecipe.getIngredients(),
                kubeJSRecipe.getResultItem(null),
                (((ShapedKubeJSRecipeAccessor) kubeJSRecipe)).getMirror(),
                kubeJSRecipe.kjs$getIngredientActions(),
                kubeJSRecipe.kjs$getModifyResult(),
                kubeJSRecipe.kjs$getStage(),
                index2count
        );
    }

    public CountShapedRecipe(ResourceLocation id, String group, CraftingBookCategory category,
                             int width, int height, NonNullList<Ingredient> ingredients,
                             ItemStack result, boolean mirror, List<IngredientAction> ingredientActions,
                             @Nullable ModifyRecipeResultCallback modifyResult, String stage, Int2IntOpenHashMap index2count) {
        super(id, group, category, width, height, ingredients, result, mirror, ingredientActions, modifyResult, stage);
        index2count.defaultReturnValue(0);
        this.index2count = index2count;
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
        return RecipeSerializerInit.SHAPED.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        //TODO 代码提取的优化
        IntArrayList naturalIndexs = new IntArrayList(index2count.keySet());
        naturalIndexs.sort(IntComparators.NATURAL_COMPARATOR);
        IntList frontEmpty = getFrontEmpty(container);

        for (int i = 0; i < naturalIndexs.size(); i++) {
            int index = naturalIndexs.getInt(i);
            int slotIndex = index - 1 + frontEmpty.getInt(i);
            container.removeItem(slotIndex, index2count.get(index) - 1);
        }
        return super.getRemainingItems(container);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> indexItemStack = getIndexItemStack(container);
        List<Predicate<ItemStack>> testList = getCountTestList();
        for (int i = 0; i < testList.size(); i++) {
            Predicate<ItemStack> predicate = testList.get(i);
            if (i >= indexItemStack.size()) break;
            ItemStack itemStack = indexItemStack.get(i);
            if (!predicate.test(itemStack)) return false;
        }
        return super.matches(container, level);
    }

    public static class Serializer implements ICountSerializer, RecipeSerializer<CountShapedRecipe> {
        private static final RecipeSerializer<ShapedRecipe> SHAPED = UtilsJS.cast(RegistryInfo.RECIPE_SERIALIZER.getValue(new ResourceLocation("crafting_shaped")));

        @Override
        public @NotNull CountShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe shapedRecipe = SHAPED.fromJson(id, json);
            boolean mirror = GsonHelper.getAsBoolean(json, "kubejs:mirror", true);
            boolean shrink = GsonHelper.getAsBoolean(json, "kubejs:shrink", true);
            Map<String, Ingredient> key = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] pattern = ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern"));
            if (shrink) {
                pattern = ShapedRecipe.shrink(pattern);
            }

            int w = pattern[0].length();
            int h = pattern.length;
            NonNullList<Ingredient> ingredients = ShapedRecipe.dissolvePattern(pattern, key, w, h);
            List<IngredientAction> ingredientActions = IngredientAction.parseList(json.get("kubejs:actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs:modify_result")) {
                modifyResult = RecipesEventJS.MODIFY_RESULT_CALLBACKS.get(id);
            }

            String stage = GsonHelper.getAsString(json, "kubejs:stage", "");
            ShapedKubeJSRecipe jsRecipe = new ShapedKubeJSRecipe(id, shapedRecipe.getGroup(), shapedRecipe.category(), w, h, ingredients, shapedRecipe.result, mirror, ingredientActions, modifyResult, stage);
            return new CountShapedRecipe(jsRecipe, fromJson(json));
        }

        @Override
        public CountShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapedRecipe shapedRecipe = SHAPED.fromNetwork(id, buf);
            int flags = buf.readByte();
            String group = shapedRecipe.getGroup();
            CraftingBookCategory category = shapedRecipe.category();
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();
            NonNullList<Ingredient> ingredients = shapedRecipe.getIngredients();
            ItemStack result = shapedRecipe.result;
            List<IngredientAction> ingredientActions = (flags & 1) != 0 ? IngredientAction.readList(buf) : List.of();
            String stage = (flags & 2) != 0 ? buf.readUtf() : "";
            boolean mirror = (flags & 4) != 0;
            ShapedKubeJSRecipe jsRecipe = new ShapedKubeJSRecipe(id, group, category, width, height, ingredients, result, mirror, ingredientActions, null, stage);
            return new CountShapedRecipe(jsRecipe, fromNetwork(buf));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CountShapedRecipe r) {
            SHAPED.toNetwork(buf, r);
            int flags = 0;
            if (r.kjs$getIngredientActions() != null && !r.kjs$getIngredientActions().isEmpty()) {
                flags |= 1;
            }

            if (((ShapedKubeJSRecipeAccessor) r).getMirror()) {
                flags |= 4;
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
