package com.kaleblangley.count_craft.mixin;

import dev.latvian.mods.kubejs.recipe.component.ComponentValueMap;
import dev.latvian.mods.kubejs.recipe.schema.minecraft.ShapedRecipeSchema;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ShapedRecipeSchema.ShapedRecipeJS.class, remap = false)
public interface ShapedRecipeJSAccessor {
    @Invoker("set2DValues")
    void set2DValues(ComponentValueMap from);
}
