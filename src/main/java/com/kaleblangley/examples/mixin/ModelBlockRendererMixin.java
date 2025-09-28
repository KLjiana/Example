package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.api.ICustomBlockRenderer;
import com.kaleblangley.examples.impl.CustomBlockRenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
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
                        renderer.tesselateBlock(modelBlockRenderer, level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay, modelData, renderType),
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

//    @Inject(
//            method = "",
//            at = @At("HEAD"),
//            remap = false,
//            cancellable = true
//    )
//    private void custom(BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, long seed, int packedOverlay, ModelData modelData, RenderType renderType, CallbackInfo ci) {
//        cancelAndCall(
//                state,
//                (modelBlockRenderer, renderer) ->
//                        renderer.tesselateBlock(modelBlockRenderer, level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay, modelData, renderType),
//                ci
//        );
//    }

    @Unique
    private void cancelAndCall(BlockState state, BiConsumer<ModelBlockRenderer, ICustomBlockRenderer> render, Cancellable ci) {
        ICustomBlockRenderer renderer = CustomBlockRenderManager.getRenderer(state);
        if (renderer != null) {
            render.accept((ModelBlockRenderer) (Object) this, renderer);
            ci.cancel();
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
