package com.kaleblangley.count_craft.kubejs;

import com.google.gson.JsonObject;
import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.impl.ICountSerializer;
import com.kaleblangley.count_craft.impl.ShapelessRecipeAccessor;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class CountShapelessRecipe extends ShapelessKubeJSRecipe implements ICountRecipe {
    private final Int2IntOpenHashMap index2count;

    public CountShapelessRecipe(ShapelessKubeJSRecipe kubeJSRecipe, Int2IntOpenHashMap index2count) {
        this(((ShapelessRecipeAccessor) kubeJSRecipe).getShapelessRecipe(), kubeJSRecipe.kjs$getIngredientActions(), kubeJSRecipe.kjs$getModifyResult(), kubeJSRecipe.kjs$getStage(), index2count);
    }

    public CountShapelessRecipe(ShapelessRecipe original, List<IngredientAction> ingredientActions, @Nullable ModifyRecipeResultCallback modifyResult, String stage, Int2IntOpenHashMap index2count) {
        super(original, ingredientActions, modifyResult, stage);
        index2count.defaultReturnValue(0);
        this.index2count = index2count;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        IntArrayList naturalIndexs = new IntArrayList(index2count.keySet());
        naturalIndexs.sort(IntComparators.NATURAL_COMPARATOR);
        IntList frontEmpty = getFrontEmpty(container);

        for (int i = 0; i < naturalIndexs.size(); i++) {
            int index = naturalIndexs.getInt(i);
            if (i >= frontEmpty.size()) {
                continue;
            }
            int slotIndex = index - 1 + frontEmpty.getInt(i);
            if (slotIndex >= 0 && slotIndex < container.getContainerSize()) {
                container.removeItem(slotIndex, index2count.get(index) - 1);
            }
        }
        return super.getRemainingItems(container);
    }


    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
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

    @Override
    public Int2IntOpenHashMap getIndex2count() {
        return index2count;
    }

    public static class SerializerJS extends SerializerKJS implements ICountSerializer {
        @Override
        public @NotNull ShapelessKubeJSRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            return new CountShapelessRecipe(super.fromJson(id, jsonObject), fromJson(jsonObject));
        }

        @Override
        public ShapelessKubeJSRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new CountShapelessRecipe(super.fromNetwork(id, buf), fromNetwork(buf));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessKubeJSRecipe recipe) {
            super.toNetwork(buf, recipe);
            tooNetwork(buf, recipe);
        }
    }
}
