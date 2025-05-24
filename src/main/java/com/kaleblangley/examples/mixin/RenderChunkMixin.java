package com.kaleblangley.examples.mixin;

import com.kaleblangley.examples.Examples;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderDispatcher.RenderChunk.class)
public class RenderChunkMixin {
    @Inject(method = "setOrigin", at = @At("HEAD"))
    public void setPos(int x, int y, int z, CallbackInfo ci){
//        Examples.LOGGER.info("{},{},{}", x, y, z);
    }
}
