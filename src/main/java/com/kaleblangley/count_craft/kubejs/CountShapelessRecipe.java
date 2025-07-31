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
        List<Ingredient> ingredients = getIngredients();
        Int2IntMap index2count = getIndex2count();
        IntList frontEmpty = getFrontEmpty(container);

        int matched = 0;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;

            if (matched >= ingredients.size()) {
                return false; // 多余物品，不匹配
            }

            Ingredient expected = ingredients.get(matched);
            if (!expected.test(stack)) {
                return false; // 类型不匹配
            }

            int index = matched + 1;
            if (index2count.containsKey(index)) {
                int required = index2count.get(index);
                if (stack.getCount() < required) {
                    return false; // 数量不足
                }
            }

            matched++;
        }

        if (matched == ingredients.size()) {
            return super.matches(container, level);
        } else {
            return false;
        }
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
