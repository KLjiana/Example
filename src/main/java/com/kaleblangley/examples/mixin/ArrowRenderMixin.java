package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.impl.TrailSaver;
import com.kaleblangley.examples.util.TrailRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ArrowRenderer.class)
public class ArrowRenderMixin<T extends AbstractArrow> extends EntityRenderer<T> {
    protected ArrowRenderMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    public void renderTrace(T spectralArrowEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CallbackInfo ci) {
        List<Vec3> trails = ((TrailSaver) spectralArrowEntity).getPastPositions();
        TrailRender.renderTrail(trails, spectralArrowEntity.position(), poseStack, multiBufferSource, 0xFFFD3E03);
    }


    @Override
    public ResourceLocation getTextureLocation(@NotNull T p_114482_) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
