package com.kaleblangley.count_craft.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeConstructor;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static dev.latvian.mods.kubejs.recipe.schema.minecraft.SmithingTransformRecipeSchema.*;

public interface CountSmithingTransformRecipeSchema {
    RecipeKey<Integer[]> INDEXS = NumberComponent.INT.asArray().key("indexs");
    RecipeKey<Integer[]> COUNTS = NumberComponent.INT.asArray().key("counts");
    RecipeKey<Boolean> IS_COPY_NBT = BooleanComponent.BOOLEAN.key("count_craft:is_copy_nbt").preferred("isCopyNbt").optional(true).exclude();
    RecipeSchema SCHEMA = new RecipeSchema(CountSmithingRecipeJs.class, CountSmithingRecipeJs::new, RESULT, TEMPLATE, BASE, ADDITION, INDEXS, COUNTS, IS_COPY_NBT)
            .uniqueOutputId(RESULT)
            .constructor(RESULT, TEMPLATE, BASE, ADDITION, INDEXS, COUNTS)
            .constructor(RecipeConstructor.Factory.defaultWith((recipe, key) -> {
                if (key == TEMPLATE) {
                    return InputItem.of(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), 1);
                } else {
                    return null;
                }
            }), RESULT, BASE, ADDITION, INDEXS, COUNTS);

    class CountSmithingRecipeJs extends RecipeJS {
        public RecipeJS isCopyNbt(boolean is) {
            return this.setValue(IS_COPY_NBT, is);
        }
    }
}
