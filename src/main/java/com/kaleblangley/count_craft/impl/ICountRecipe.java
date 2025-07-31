package com.kaleblangley.count_craft.impl;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface ICountRecipe {
    Int2IntOpenHashMap getIndex2count();

    NonNullList<Ingredient> getIngredients();

    default IntList getFrontEmpty(CraftingContainer container) {
        IntArrayList list = new IntArrayList();
        int empty = 0;
        int found = 0;
        for (int i = 0; i < container.getItems().size(); i++) {
            if (container.getItem(i).isEmpty()) {
                empty++;
            } else {
                if (getIndex2count().containsKey(found + 1)) {
                    list.add(empty);
                }
                empty = 0;
                found++;
            }
        }
        return list;
    }

    default List<ItemStack> getRealItemStacks(CraftingContainer container) {
        return container.getItems()
                .stream()
                .filter(itemStack -> !itemStack.isEmpty())
                .toList();
    }

    default List<ItemStack> getIndexItemStack(CraftingContainer container) {
        List<ItemStack> indexList = new ArrayList<>();
        List<ItemStack> realItemStacks = getRealItemStacks(container);
        getIndex2count().keySet().forEach(index -> {
            if (index > container.getContainerSize() || index > realItemStacks.size()) return;
            indexList.add(realItemStacks.get(index - 1));
        });
        return indexList;
    }

    default List<Predicate<ItemStack>> getCountTestList() {
        List<Predicate<ItemStack>> countTestList = new ObjectArrayList<>();
        getIndex2count().forEach((index, count) -> countTestList.add(getCountPredicate(count)));
        return countTestList;
    }

    default Predicate<ItemStack> getCountPredicate(int requiredCount) {
        return itemStack -> itemStack.getCount() >= requiredCount;
    }
}
