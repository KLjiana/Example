package com.kaleblangley.count_craft.mixin;

import com.kaleblangley.count_craft.impl.ShapelessRecipeAccessor;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ShapelessKubeJSRecipe.class, remap = false)
public class ShapelessKubeJSRecipeMixin implements ShapelessRecipeAccessor {
    @Unique private ShapelessRecipe original;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void getOriginal(ShapelessRecipe original, List<IngredientAction> ingredientActions, ModifyRecipeResultCallback modifyResult, String stage, CallbackInfo ci) {
        this.original = original;
    }

    @Override
    public ShapelessRecipe getShapelessRecipe() {
        return original;
    }
}
