package com.kaleblangley.examples.util;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public class TrailRender {
    public static double saveTrailPos(List<Vec3> trails, Vec3 current, double distanceAccumulator) {
        if (trails.isEmpty()) {
            trails.add(current);
            return distanceAccumulator;
        }
        Vec3 last = trails.get(trails.size() - 1);
        double dist = last.distanceTo(current);
        double spacing = 0.4;

        distanceAccumulator += dist;
        if (distanceAccumulator >= spacing) {
            int steps = (int)(distanceAccumulator / spacing);
            Vec3 delta = current.subtract(last).scale(1.0 / (steps + 1));
            for (int i = 1; i <= steps; i++) {
                trails.add(last.add(delta.scale(i)));
            }
            distanceAccumulator -= steps * spacing;
        }
        trails.add(current);

        while (trails.size() > 20) {
            trails.remove(0);
        }
        return distanceAccumulator;
    }

    public static void renderTrail(
            List<Vec3> trails,
            Vec3 entityPos,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int color) {
        if (trails.size() < 2) return;

        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer buffer = bufferSource.getBuffer(TGRenderTypes.TRAIL_RENDER_TYPE);

        Minecraft mc = Minecraft.getInstance();
        Vec3 camDir = new Vec3(mc.gameRenderer.getMainCamera().getLookVector());

        int red = FastColor.ARGB32.red(color);
        int green = FastColor.ARGB32.green(color);
        int blue = FastColor.ARGB32.blue(color);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlConst.GL_SRC_ALPHA, GlConst.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 1; i < trails.size(); i++) {
            Vec3 pos0 = trails.get(i - 1).subtract(entityPos);
            Vec3 pos1 = trails.get(i).subtract(entityPos);

            Vec3 dir = pos1.subtract(pos0).normalize();

            float progress = i / (float) trails.size();
            float width = 0.25f * progress;
            int alpha = (int) (200 * progress);
            int argb = FastColor.ARGB32.color(alpha, red, green, blue);

            Vec3 side = dir.cross(camDir).normalize().scale(width);
            Vec3 left0 = pos0.add(side.scale(+width));
            Vec3 right0 = pos0.add(side.scale(-width));
            Vec3 left1 = pos1.add(side.scale(+width));
            Vec3 right1 = pos1.add(side.scale(-width));

            addVertex(buffer, matrix4f, left0, argb);
            addVertex(buffer, matrix4f, right0, argb);
            addVertex(buffer, matrix4f, right1, argb);
            addVertex(buffer, matrix4f, left1, argb);
        }
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private static void addVertex(VertexConsumer buffer, Matrix4f matrix, Vec3 pos, int argb) {
        buffer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .color(argb)
                .endVertex();
    }
}
