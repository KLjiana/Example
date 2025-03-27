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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowRenderer.class)
public class ArrowRenderMixin<T extends AbstractArrow> extends EntityRenderer<T> {
    protected ArrowRenderMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    public void renderTrace(T spectralArrowEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CallbackInfo ci) {
        TrailRender.render(((TrailSaver) spectralArrowEntity).getPastPositions(), spectralArrowEntity, poseStack, multiBufferSource);
//        List<Vec3> trails = ((TrailSaver) spectralArrowEntity).getPastPositions();
//        if (trails.isEmpty()) return;
//        Vec3 pos0;
//        Vec3 pos1;
//        Vec3 camera = this.entityRenderDispatcher.camera.getPosition();
//
//        poseStack.pushPose();
//        poseStack.translate(-camera.x, -camera.y, -camera.z);
//        poseStack.translate(spectralArrowEntity.getX(), spectralArrowEntity.getY(), spectralArrowEntity.getZ());
//        Matrix4f matrix4f = poseStack.last().pose();
//
//        VertexConsumer bufferbuilder = bufferSource.getBuffer(RenderType.lightning());
//        for (int i = 1; i < trails.size(); i++) {
//            pos0 = trails.get(i - 1).subtract(spectralArrowEntity.position());
//            pos1 = trails.get(i).subtract(spectralArrowEntity.position());
//
//            double x1 = pos0.x;
//            double y1 = pos0.y;
//            double z1 = pos0.z;
//            double x2 = pos1.x;
//            double y2 = pos1.y;
//            double z2 = pos1.z;
//            float width0 = 0.3f / trails.size() * (i - 1);
//            float width1 = 0.3f / trails.size() * i;
//            bufferbuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z1 - width0)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z1 + width0)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x2, (float) y2, (float) z2 + width1)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x2, (float) y2, (float) z2 - width1)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//
//            bufferbuilder.vertex(matrix4f, (float) x1 - width0, (float) y1, (float) z1)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x1 + width0, (float) y1, (float) z1)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x2 + width1, (float) y2, (float) z2)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//            bufferbuilder.vertex(matrix4f, (float) x2 - width1, (float) y2, (float) z2)
//                    .color(1, 1, 1, 0.5f)
//                    .endVertex();
//        }
//        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(@NotNull T p_114482_) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
