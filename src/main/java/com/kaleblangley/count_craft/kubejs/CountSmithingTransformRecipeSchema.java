package com.kaleblangley.count_craft.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeConstructor;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static dev.latvian.mods.kubejs.recipe.schema.minecraft.SmithingTransformRecipeSchema.*;

public interface CountSmithingTransformRecipeSchema {
    RecipeKey<Integer[]> INDEXS = NumberComponent.INT.asArray().key("indexs");
    RecipeKey<Integer[]> COUNTS = NumberComponent.INT.asArray().key("counts");
    RecipeSchema SCHEMA = new RecipeSchema(RESULT, TEMPLATE, BASE, ADDITION, INDEXS, COUNTS)
            .uniqueOutputId(RESULT)
            .constructor(RESULT, TEMPLATE, BASE, ADDITION, INDEXS, COUNTS)
            .constructor(RecipeConstructor.Factory.defaultWith((recipe, key) -> {
                if (key == TEMPLATE) {
                    return InputItem.of(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), 1);
                } else {
                    return null;
                }
            }), RESULT, BASE, ADDITION, INDEXS, COUNTS);
}
