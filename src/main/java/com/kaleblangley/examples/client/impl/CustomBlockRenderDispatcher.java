package com.kaleblangley.examples.client.impl;

import com.kaleblangley.examples.client.api.ICustomBlockRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;

public class CustomBlockRenderDispatcher {
    private Object2ObjectArrayMap<BlockPos, ICustomBlockRenderer> renderers = new Object2ObjectArrayMap<>();

    public void putRenderer(BlockPos blockPos, ICustomBlockRenderer renderer) {
        renderers.put(blockPos, renderer);
    }
}
