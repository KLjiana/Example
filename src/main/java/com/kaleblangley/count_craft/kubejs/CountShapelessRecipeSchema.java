package com.kaleblangley.count_craft.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapelessRecipeSchema;

public interface CountShapelessRecipeSchema {
    RecipeKey<Integer[]> INDEXS = NumberComponent.INT.asArray().key("indexs");
    RecipeKey<Integer[]> COUNTS = NumberComponent.INT.asArray().key("counts");
    RecipeSchema SCHEMA = new RecipeSchema(ShapelessRecipeSchema.ShapelessRecipeJS.class,
            ShapelessRecipeSchema.ShapelessRecipeJS::new,
            ShapelessRecipeSchema.RESULT,
            ShapelessRecipeSchema.INGREDIENTS,
            INDEXS,
            COUNTS
    ).uniqueOutputId(ShapelessRecipeSchema.RESULT);
}
