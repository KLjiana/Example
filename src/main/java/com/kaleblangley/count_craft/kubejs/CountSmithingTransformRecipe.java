package com.kaleblangley.count_craft.kubejs;

import com.google.gson.JsonObject;
import com.kaleblangley.count_craft.impl.ICountRecipe;
import com.kaleblangley.count_craft.impl.ICountSerializer;
import com.kaleblangley.count_craft.init.RecipeSerializerInit;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CountSmithingTransformRecipe extends SmithingTransformRecipe implements ICountRecipe {
    private final Int2IntOpenHashMap index2count;
    private final boolean isCopyNbt;

    public CountSmithingTransformRecipe(SmithingTransformRecipe transformRecipe, boolean isCopyNbt, Int2IntOpenHashMap index2count) {
        this(transformRecipe.getId(), transformRecipe.template, transformRecipe.base, transformRecipe.addition, transformRecipe.result, isCopyNbt, index2count);
    }

    public CountSmithingTransformRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, boolean isCopyNbt, Int2IntOpenHashMap index2count) {
        super(id, template, base, addition, result);
        this.isCopyNbt = isCopyNbt;
        this.index2count = index2count;
    }

    @Override
    public Int2IntOpenHashMap getIndex2count() {
        return index2count;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredientList() {
        NonNullList<Ingredient> ingredients = NonNullList.of(Ingredient.EMPTY, template, base, addition);
        return ingredients;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return isCopyNbt ? super.assemble(container, registryAccess) : getResultItem(registryAccess).copy();
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        for (var entry : index2count.int2IntEntrySet()) {
            ItemStack selectedItem = container.getItem(entry.getIntKey()-1);
            if (selectedItem.getCount() < entry.getIntValue()) {
                return false;
            }
        }
        return super.matches(container, level);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SMITHING_TRANSFORM.get();
    }

    public static class Serializer implements RecipeSerializer<CountSmithingTransformRecipe>, ICountSerializer {
        private static final RecipeSerializer<SmithingTransformRecipe> SMITHING_TRANSFORM = UtilsJS.cast(RegistryInfo.RECIPE_SERIALIZER.getValue(new ResourceLocation("smithing_transform")));

        @Override
        public @NotNull CountSmithingTransformRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            SmithingTransformRecipe transformRecipe = SMITHING_TRANSFORM.fromJson(resourceLocation, jsonObject);
            boolean isCopyNbt = GsonHelper.getAsBoolean(jsonObject, "count_craft:is_copy_nbt", true);
            Int2IntOpenHashMap map = fromJson(jsonObject);
            return new CountSmithingTransformRecipe(transformRecipe, isCopyNbt, map);
        }

        @Override
        public CountSmithingTransformRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf byteBuf) {
            SmithingTransformRecipe transformRecipe = SMITHING_TRANSFORM.fromNetwork(resourceLocation, byteBuf);
            boolean isCopyNbt = byteBuf.readBoolean();
            Int2IntOpenHashMap map = fromNetwork(byteBuf);
            return new CountSmithingTransformRecipe(transformRecipe, isCopyNbt, map);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf byteBuf, @NotNull CountSmithingTransformRecipe transformRecipe) {
            SMITHING_TRANSFORM.toNetwork(byteBuf, transformRecipe);
            byteBuf.writeBoolean(transformRecipe.isCopyNbt);
            tooNetwork(byteBuf, transformRecipe);
        }
    }
}
