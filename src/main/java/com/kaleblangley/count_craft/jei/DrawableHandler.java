package com.kaleblangley.count_craft.jei;

import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.mixin.RecipeSlotAccessor;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import java.util.*;

@SuppressWarnings("unused")
public class DrawableHandler {
    // TODO 绝对的屎山，绝对的偷懒，这一块
    public static <R> void applyDrawable(List<IRecipeSlotDrawable> recipeCategorySlots, R recipe) {
        if (!(recipe instanceof ICountRecipe countRecipe)) return;

        // 映射：第几个 Ingredient（1-based） → 显示的数量
        Int2IntOpenHashMap indexToCount = countRecipe.getIndex2count();

        // 获取每个 Ingredient 的代表性 ItemStack（只取第一个）
        List<ItemStack> itemStacks = countRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getItems().length >= 1)
                .map(ingredient -> ingredient.getItems()[0])
                .toList();

        // 拿出所有要绘制的 index 并排序（例如 [2, 5]）
        IntArrayList sortedIndices = new IntArrayList(indexToCount.keySet());
        sortedIndices.sort(IntComparators.NATURAL_COMPARATOR);
        List<IRecipeSlotDrawable> inputSlots = recipeCategorySlots.stream()
                .filter(slot -> slot.getRole() != RecipeIngredientRole.OUTPUT)
                .toList();

        int inputIndex = 0;
        Map<ItemStack, Integer> matchCount = new HashMap<>();

        for (int rawIndex : sortedIndices) {
            if (rawIndex <= 0 || rawIndex > itemStacks.size()) continue;

            ItemStack requiredItem = itemStacks.get(rawIndex - 1);
            int requiredCount = indexToCount.get(rawIndex);

            int currentOccurrence = matchCount.merge(requiredItem, 1, Integer::sum);

            int occurrence = 0;
            for (int i = 0; i < inputSlots.size(); i++) {
                IRecipeSlotDrawable slot = inputSlots.get(i);
                Optional<ItemStack> optional = slot.getAllIngredients()
                        .map(ITypedIngredient::getItemStack)
                        .flatMap(Optional::stream)
                        .findFirst();

                if (optional.isPresent() && ItemStack.isSameItemSameTags(optional.get(), requiredItem)) {
                    occurrence++;
                    if (occurrence == currentOccurrence) {
                        if (requiredCount > 1 && slot instanceof RecipeSlotAccessor accessor) {
                            accessor.setOverlay(new DrawableCount(requiredCount));
                        }
                        break;
                    }
                }
            }
        }
    }
}
