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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
            int ingredientIndex = entry.getIntValue(); // 从0开始

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
        List<Ingredient> ingredients = new ArrayList<>(getIngredients()); // 可变列表
        Int2IntMap index2count = getIndex2count();

        // 用于标记配方 Ingredient 是否被匹配过
        boolean[] used = new boolean[ingredients.size()];

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack inputStack = container.getItem(slot);
            if (inputStack.isEmpty()) continue;

            boolean matched = false;

            for (int i = 0; i < ingredients.size(); i++) {
                if (used[i]) continue;

                Ingredient ing = ingredients.get(i);
                if (ing.test(inputStack)) {
                    // 如果这个 Ingredient 在 index2count 中，需要检查数量
                    int index = i + 1;
                    if (index2count.containsKey(index)) {
                        int requiredCount = index2count.get(index);
                        if (inputStack.getCount() < requiredCount) {
                            return false; // 数量不够
                        }
                    }

                    used[i] = true;
                    matched = true;
                    break;
                }
            }

            if (!matched) return false; // 有输入物品无法匹配任何 Ingredient
        }

        // 检查是否所有 Ingredient 都被匹配到了（允许少物品？可根据需求删这段）
        for (int i = 0; i < ingredients.size(); i++) {
            if (!used[i] && !ingredients.get(i).isEmpty()) {
                return false; // 有 Ingredient 没匹配上
            }
        }

        return true;
    }


    @Override
    public Int2IntOpenHashMap getIndex2count() {
        return index2count;
    }

    private Int2IntMap getIndexSlotMap(CraftingContainer container) {
        Int2IntOpenHashMap indexToSlot = new Int2IntOpenHashMap();
        List<Ingredient> ingredients = getIngredients();
        IntList frontEmpty = getFrontEmpty(container);

        int found = 0;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;

            if (found >= ingredients.size()) break;

            Ingredient expected = ingredients.get(found);
            if (expected.test(stack)) {
                int index = found + 1; // 1-based
                int offset = frontEmpty.size() > found ? frontEmpty.getInt(found) : 0;
                int expectedSlot = index - 1 + offset;
                indexToSlot.put(index, slot);
                found++;
            }
        }

        return indexToSlot;
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
