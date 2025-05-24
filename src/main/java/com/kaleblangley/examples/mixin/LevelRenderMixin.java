package com.kaleblangley.examples.mixin;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin {
    @Redirect(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;set(FFF)V"))
    public void cancel(Uniform instance, float x, float y, float z){
//        instance.set(x,y* Minecraft.getInstance().getPartialTick(),z);
    }

    @Inject(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;upload()V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void rotato(RenderType renderType, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix, CallbackInfo ci, boolean flag1, ObjectListIterator objectlistiterator, ShaderInstance shaderinstance, Uniform uniform, LevelRenderer.RenderChunkInfo levelrenderer$renderchunkinfo1, ChunkRenderDispatcher.RenderChunk chunkrenderdispatcher$renderchunk, VertexBuffer vertexbuffer, BlockPos blockpos){

    }
}
