package com.kaleblangley.count_craft.mixin;

import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ShapedKubeJSRecipe.class, remap = false)
public interface ShapedKubeJSRecipeAccessor {
    @Accessor("mirror")
    boolean getMirror();
}
