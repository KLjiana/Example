package com.kaleblangley.wrench_plus.mixin;

import com.kaleblangley.wrench_plus.impl.IDamage;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Shadow public abstract boolean canReplaceEqualItem(ItemStack candidate, ItemStack existing);

    @Inject(method = "canReplaceCurrentItem", at = @At("HEAD"), cancellable = true)
    public void modifyReplace(ItemStack candidate, ItemStack existing, CallbackInfoReturnable<Boolean> cir) {
        if (existing.isEmpty()) {
            cir.setReturnValue(true);
        } else {
            Item candidateItem = candidate.getItem();
            Item existingItem = existing.getItem();
            
            if (candidateItem instanceof IDamage || candidateItem instanceof SwordItem) {
                float candidateDamage = getDamage(candidateItem);
                
                if (!(existingItem instanceof SwordItem) && !(existingItem instanceof IDamage)) {
                    cir.setReturnValue(true);
                } else {
                    float existingDamage = getDamage(existingItem);
                    if (candidateDamage != existingDamage) {
                        cir.setReturnValue(candidateDamage > existingDamage);
                    } else {
                        cir.setReturnValue(this.canReplaceEqualItem(candidate, existing));
                    }
                }
            }
        }
    }

    @Unique
    private float getDamage(Item item) {
        if (item instanceof IDamage) {
            return ((IDamage) item).getDamage();
        } else if (item instanceof SwordItem) {
            return ((SwordItem) item).getDamage();
        }
        return 0.0f;
    }
}
