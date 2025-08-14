package com.kaleblangley.wrench_plus.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AllItems.class, remap = false)
public class AllItemsMixin {
    @ModifyReturnValue(method = "lambda$static$41", at = @At("RETURN"))
    private static Item.Properties modifyWrenchDamage(Item.Properties original) {
        return original;
    }
}
