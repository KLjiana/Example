package com.kaleblangley.examples.client.mixin;

import com.kaleblangley.examples.client.api.ICustomBlockRenderer;
import com.kaleblangley.examples.client.impl.CustomBlockRenderRegisterer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.Cancellable;

import java.util.BitSet;
import java.util.List;
import java.util.function.BiConsumer;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {
    @Inject(
            method = "tesselateBlock(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void customTesselateBlock(BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.tesselateBlock(new ICustomBlockRenderer.RenderContent(modelBlockRenderer, level, state, pos, poseStack, consumer), model, checkSides, random, seed, packedOverlay, modelData, renderType),
                ci
        );
    }

    @Inject(
            method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void customTesselateWithAO(BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.tesselateWithAO(modelBlockRenderer, level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay, modelData, renderType),
                ci
        );
    }

    @Inject(
            method = "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void customTesselateWithoutAO(BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.tesselateWithoutAO(modelBlockRenderer, level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay, modelData, renderType),
                ci
        );
    }

    @Inject(
            method = "renderModelFaceAO",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customRenderModelFaceAO(BlockAndTintGetter level, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, List<BakedQuad> quads, float[] shape, BitSet shapeFlags, ModelBlockRenderer.AmbientOcclusionFace aoFace, int packedOverlay, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.renderModelFaceAO(modelBlockRenderer, level, state, pos, poseStack, consumer, quads, shape, shapeFlags, aoFace, packedOverlay),
                ci
        );
    }

    @Inject(
            method = "putQuadData",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customPutQuadData(BlockAndTintGetter level, BlockState state, BlockPos pos, VertexConsumer consumer, PoseStack.Pose pose, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int lightmap0, int lightmap1, int lightmap2, int lightmap3, int packedOverlay, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.putQuadData(modelBlockRenderer, level, state, pos, consumer, pose, quad, brightness0, brightness1, brightness2, brightness3, lightmap0, lightmap1, lightmap2, lightmap3, packedOverlay),
                ci
        );
    }

    @Inject(
            method = "calculateShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customCalculateShape(BlockAndTintGetter level, BlockState state, BlockPos pos, int[] vertices, Direction direction, float[] shape, BitSet shapeFlags, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.calculateShape(modelBlockRenderer, level, state, pos, vertices, direction, shape, shapeFlags),
                ci
        );
    }

    @Inject(
            method = "renderModelFaceFlat",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customRenderModelFaceFlat(BlockAndTintGetter level, BlockState state, BlockPos pos, int packedLight, int packedOverlay, boolean repackLight, PoseStack poseStack, VertexConsumer consumer, List<BakedQuad> quads, BitSet shapeFlags, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.renderModelFaceFlat(modelBlockRenderer, level, state, pos, packedLight, packedOverlay, repackLight, poseStack, consumer, quads, shapeFlags),
                ci
        );
    }


    @Inject(
            method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFIILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void customRenderModel(PoseStack.Pose pose, VertexConsumer consumer, BlockState state, BakedModel model, float red, float green, float blue, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
        cancelAndCall(
                state,
                (modelBlockRenderer, renderer) ->
                        renderer.renderModel(modelBlockRenderer, pose, consumer, state, model, red, green, blue, packedLight, packedOverlay, modelData, renderType),
                ci
        );
    }


    @Unique
    private void cancelAndCall(BlockState state, BiConsumer<ModelBlockRenderer, ICustomBlockRenderer> render, Cancellable ci) {
        if (CustomBlockRenderRegisterer.hasCustomRenderer(state)) {
            ICustomBlockRenderer renderer = CustomBlockRenderRegisterer.getRenderer(state);
            if (renderer != null) {
                ci.cancel();
                if (renderer.getUpdateLevel() == ICustomBlockRenderer.UpdateLevel.DEFAULT) {
                    render.accept((ModelBlockRenderer) (Object) this, renderer);
                } else {
//                    new CustomBlockRenderDispatcher().putRenderer(BlockPos.ZERO, new DoorBlockRenderer());
                }
            }
        }
    }


//    @Redirect(
//            method = {
//                    "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
//                    "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"
//            },
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;)Z")
//    )
//    private boolean renderFaceEvery(BlockState voxelshape1, BlockGetter flag, BlockPos voxelshape, Direction block$blockstatepairkey, BlockPos object2bytelinkedopenhashmap) {
//        return true;
//    }
}
