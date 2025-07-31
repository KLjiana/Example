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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

@SuppressWarnings("unused")
public class DrawableHandler {
    // TODO 绝对的屎山，绝对的偷懒，这一块
    public static <R> void applyDrawable(List<IRecipeSlotDrawable> recipeCategorySlots, R recipe) {
        if (!(recipe instanceof ICountRecipe countRecipe)) return;
        Int2IntOpenHashMap indexToCount = countRecipe.getIndex2count();
        List<ItemStack> itemStacks = countRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getItems().length >= 1)
                .map(ingredient -> ingredient.getItems()[0])
                .toList();

        List<ItemStack> requiredItems = new ArrayList<>();
        IntArrayList intArrayList = new IntArrayList(indexToCount.keySet());
        intArrayList.sort(IntComparators.NATURAL_COMPARATOR);
        intArrayList.forEach(index -> {
            if (index > itemStacks.size()) return;
            requiredItems.add(itemStacks.get(index - 1));
        });

        List<IRecipeSlotDrawable> inputSlot = recipeCategorySlots
                .stream()
                .filter(slot->slot.getRole()!=RecipeIngredientRole.OUTPUT)
                .toList();


        int lastSlotIndex = 0;
        int empty = 1;
        for (ItemStack requiredItem : requiredItems) {
            for (int i = lastSlotIndex; i < inputSlot.size(); i++) {
                IRecipeSlotDrawable slot = inputSlot.get(i);
                if (slot.getRole() == RecipeIngredientRole.OUTPUT) continue;
                Optional<ItemStack> optional = slot.getAllIngredients().map(ITypedIngredient::getItemStack).map(Optional::get).findFirst();
                if (optional.isPresent()) {
                    if (ItemStack.isSameItemSameTags(optional.get(), requiredItem)) {
                        int count = indexToCount.get(lastSlotIndex + empty);
                        if (count > 1 && slot instanceof RecipeSlotAccessor accessor) {
                            accessor.setOverlay(new DrawableCount(count));

                            lastSlotIndex = i;
                            empty = 0;
                            break;
                        }
                    }
                }
                empty++;
            }
        }
    }
}
