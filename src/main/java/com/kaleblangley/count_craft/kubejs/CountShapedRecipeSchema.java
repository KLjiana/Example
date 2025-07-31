package com.kaleblangley.count_craft.kubejs;

import com.kaleblangley.count_craft.mixin.ShapedRecipeJSMixin;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema;

import static dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema.*;

public interface CountShapedRecipeSchema {
    RecipeKey<Integer[]> INDEXS = NumberComponent.INT.asArray().key("indexs");
    RecipeKey<Integer[]> COUNTS = NumberComponent.INT.asArray().key("counts");
    RecipeSchema SCHEMA = new RecipeSchema(ShapedRecipeSchema.ShapedRecipeJS.class, ShapedRecipeSchema.ShapedRecipeJS::new, RESULT, PATTERN, KEY, INDEXS, COUNTS, KJS_MIRROR, KJS_SHRINK)
            .constructor(RESULT, PATTERN, KEY, INDEXS, COUNTS)
            .constructor((recipe, schemaType, keys, from) -> ((ShapedRecipeJSMixin) recipe).set2DValues(from), RESULT, INGREDIENTS, INDEXS, COUNTS)
            .uniqueOutputId(RESULT);
}
