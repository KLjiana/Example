package com.kaleblangley.count_craft.mixin;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = RecipeSlot.class, remap = false)
public interface RecipeSlotAccessor {
    @Mutable
    @Accessor("overlay")
    void setOverlay(IDrawable overlay);
}
