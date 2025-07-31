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
        if (!(recipe instanceof ICountRecipe countRecipe)) {
            return;
        }

        List<ItemStack> itemStacks = countRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getItems().length >= 1)
                .map(ingredient -> ingredient.getItems()[0].copy())
                .toList();

        Int2IntOpenHashMap indexToCount = countRecipe.getIndex2count();
        IntArrayList sortedIndices = new IntArrayList(indexToCount.keySet());
        sortedIndices.sort(IntComparators.NATURAL_COMPARATOR);

        List<IRecipeSlotDrawable> inputSlots = recipeCategorySlots.stream()
                .filter(slot -> slot.getRole() != RecipeIngredientRole.OUTPUT)
                .toList();

        for (int rawIndex : sortedIndices) {
            if (rawIndex <= 0 || rawIndex > itemStacks.size()) {
                continue;
            }

            ItemStack requiredItem = itemStacks.get(rawIndex - 1);
            int requiredCount = indexToCount.get(rawIndex);

            int targetOccurrence = 0;
            for (int j = 0; j < rawIndex; j++) {
                if (ItemStack.isSameItemSameTags(itemStacks.get(j), requiredItem)) {
                    targetOccurrence++;
                }
            }

            int occurrence = 0;
            for (IRecipeSlotDrawable slot : inputSlots) {
                Optional<ItemStack> opt = slot.getAllIngredients()
                        .map(ITypedIngredient::getItemStack)
                        .flatMap(Optional::stream)
                        .findFirst();
                if (opt.isEmpty()) {
                    continue;
                }
                ItemStack slotItem = opt.get();
                if (ItemStack.isSameItemSameTags(slotItem, requiredItem)) {
                    occurrence++;
                    if (occurrence == targetOccurrence) {
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
