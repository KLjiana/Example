package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.api.IEnchantmentParameter;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements IEnchantmentParameter {
    @Unique
    private Integer minLevel;
    @Unique
    private Integer maxLevel;
    @Unique
    private Integer minCost;
    @Unique
    private Integer maxCost;
    @Unique
    private Boolean treasureOnly;
    @Unique
    private Boolean curse;
    @Unique
    private Boolean tradeable;
    @Unique
    private Boolean discoverable;
    @Unique
    private Boolean allowedOnBooks;

    @Override
    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    @Override
    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }

    @Override
    public void setTreasureOnly(boolean treasureOnly) {
        this.treasureOnly = treasureOnly;
    }

    @Override
    public void setCurse(boolean curse) {
        this.curse = curse;
    }

    @Override
    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    @Override
    public void setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
    }

    @Override
    public void setAllowedOnBooks(boolean allowedOnBooks) {
        this.allowedOnBooks = allowedOnBooks;
    }

    @Inject(method = "getMinLevel", at = @At(value = "HEAD"), cancellable = true)
    public void modifyMinLevel(CallbackInfoReturnable<Integer> cir) {
        if (minLevel != null) {
            cir.setReturnValue(minLevel);
        }
    }

    @Redirect(method = "getMaxLevel", at = @At(value = "HEAD"))
    public void modifyMaxLevel(CallbackInfoReturnable<Integer> cir) {
        if (maxLevel != null) {
            cir.setReturnValue(maxLevel);
        }
    }

    @Inject(method = "getMinCost", at = @At(value = "HEAD"), cancellable = true)
    public void modifyMinCost(int level, CallbackInfoReturnable<Integer> cir) {
        if (minCost != null) {
            cir.setReturnValue(minCost);
        }
    }

    @Inject(method = "getMaxCost", at = @At(value = "HEAD"), cancellable = true)
    public void modifyMaxCost(CallbackInfoReturnable<Integer> cir) {
        if (maxCost != null) {
            cir.setReturnValue(maxCost);
        }
    }

    @Inject(method = "isTreasureOnly", at = @At(value = "HEAD"), cancellable = true)
    public void modifyTreasureOnly(CallbackInfoReturnable<Boolean> cir) {
        if (treasureOnly != null) {
            cir.setReturnValue(treasureOnly);
        }
    }

    @Inject(method = "isCurse", at = @At(value = "HEAD"), cancellable = true)
    public void modifyCurse(CallbackInfoReturnable<Boolean> cir) {
        if (curse != null) {
            cir.setReturnValue(curse);
        }
    }

    @Inject(method = "isTradeable", at = @At(value = "HEAD"), cancellable = true)
    public void modifyTradeable(CallbackInfoReturnable<Boolean> cir) {
        if (tradeable != null) {
            cir.setReturnValue(tradeable);
        }
    }

    @Inject(method = "isDiscoverable", at = @At(value = "HEAD"), cancellable = true)
    public void modifyDiscoverable(CallbackInfoReturnable<Boolean> cir) {
        if (discoverable != null) {
            cir.setReturnValue(discoverable);
        }
    }

    @Inject(method = "isAllowedOnBooks", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void modifyAllowedOnBooks(CallbackInfoReturnable<Boolean> cir) {
        if (allowedOnBooks != null) {
            cir.setReturnValue(allowedOnBooks);
        }
    }
}
