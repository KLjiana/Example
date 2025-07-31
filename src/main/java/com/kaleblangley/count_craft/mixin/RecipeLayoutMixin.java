package com.kaleblangley.count_craft.mixin;

import com.kaleblangley.count_craft.jei.DrawableHandler;
import mezz.jei.api.gui.drawable.IScalableDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.util.ImmutablePoint2i;
import mezz.jei.library.gui.ingredients.CycleTicker;
import mezz.jei.library.gui.recipes.RecipeLayout;
import mezz.jei.library.gui.recipes.ShapelessIcon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(value = RecipeLayout.class, remap = false)
public class RecipeLayoutMixin<R>{
    @Shadow @Final private R recipe;

    @Shadow @Final private List<IRecipeSlotDrawable> recipeCategorySlots;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void addOverlay(IRecipeCategory recipeCategory, Collection recipeCategoryDecorators, Object recipe, IScalableDrawable recipeBackground, int recipeBorderPadding, ShapelessIcon shapelessIcon, ImmutablePoint2i recipeTransferButtonPos, List recipeCategorySlots, List allSlots, CycleTicker cycleTicker, IFocusGroup focuses, CallbackInfo ci) {
        DrawableHandler.applyDrawable(this.recipeCategorySlots, this.recipe);
    }
}
