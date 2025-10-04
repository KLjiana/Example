package com.kaleblangley.examples.client.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.renderer.LevelRenderer.class)
public class LevelRenderer {
    @Shadow @Final private ObjectArrayList<net.minecraft.client.renderer.LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;setupRender(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;ZZ)V"))
    private void cancelCompileChunk(net.minecraft.client.renderer.LevelRenderer instance, Camera chunkrenderdispatcher$renderchunk, Frustum levelrenderer$renderchunkinfo, boolean queue, boolean levelrenderer$renderchunkstorage) {
//        var iterator = this.renderChunksInFrustum.iterator();
//        iterator.forEachRemaining(renderChunkInfo -> renderChunkInfo.chunk.compiled.set(new ChunkRenderDispatcher.CompiledChunk()));
    }
}
