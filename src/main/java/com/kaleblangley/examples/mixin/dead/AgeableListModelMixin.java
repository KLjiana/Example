package com.kaleblangley.examples.mixin.dead;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(AgeableListModel.class)
public abstract class AgeableListModelMixin {
    @Mutable
    @Shadow @Final private float bodyYOffset;

    @Inject(method = "<init>(Ljava/util/function/Function;ZFFFFF)V", at = @At("TAIL"))
    public void bigHead(Function pRenderType, boolean pScaleHead, float pBabyYHeadOffset, float pBabyZHeadOffset, float pBabyHeadScale, float pBabyBodyScale, float pBodyYOffset, CallbackInfo ci){
        this.bodyYOffset = pBodyYOffset + 100;
    }
}
