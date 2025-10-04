package com.kaleblangley.examples.client.renderer;

import com.kaleblangley.examples.client.api.ICustomBlockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class DoorBlockRenderer implements ICustomBlockRenderer {
    @Override
    public void tesselateBlock(ModelBlockRenderer modelBlockRenderer, BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType) {
        poseStack.pushPose();
        float partialTick = Minecraft.getInstance().getPartialTick();
        poseStack.mulPose(Axis.YP.rotationDegrees(partialTick * 360F));
        ICustomBlockRenderer.super.tesselateBlock(modelBlockRenderer, level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay, modelData, renderType);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderFace(BlockState state, BlockGetter level, BlockPos offset, Direction face, BlockPos pos) {
        return true;
    }
}
