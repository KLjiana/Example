package com.kaleblangley.count_craft.kubejs;

import com.google.gson.JsonObject;
import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.impl.ICountSerializer;
import com.kaleblangley.count_craft.mixin.ShapedKubeJSRecipeMixin;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
                (((ShapedKubeJSRecipeMixin) kubeJSRecipe)).getMirror(),
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

    public static class SerializerJS extends SerializerKJS implements ICountSerializer {
        @Override
        public @NotNull CountShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new CountShapedRecipe(super.fromJson(id, json), fromJson(json));
        }

        @Override
        public CountShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new CountShapedRecipe(super.fromNetwork(id, buf), fromNetwork(buf));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedKubeJSRecipe r) {
            super.toNetwork(buf, r);
            tooNetwork(buf, r);
        }
    }
}
