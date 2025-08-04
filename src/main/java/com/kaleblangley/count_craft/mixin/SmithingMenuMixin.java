package com.kaleblangley.count_craft.mixin;

import com.kaleblangley.count_craft.kubejs.CountSmithingTransformRecipe;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {
    @Shadow
    @Nullable
    private SmithingRecipe selectedRecipe;

    @Inject(method = "shrinkStackInSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void countShirk(int index, CallbackInfo ci, ItemStack itemstack) {
        if (this.selectedRecipe instanceof CountSmithingTransformRecipe transformRecipe) {
            transformRecipe.getIndex2count().computeIfPresent(index + 1, (indexx, count) -> {
                itemstack.shrink(count - 1);
                return count;
            });
        }
    }
}
