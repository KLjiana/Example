package com.kaleblangley.examples.impl;

import com.google.common.collect.ImmutableList;
import com.kaleblangley.examples.Examples;
import com.kaleblangley.examples.api.ICustomBlockRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomBlockRenderManager {
    private static final Object2ObjectLinkedOpenHashMap<Class<? extends Block>, ICustomBlockRenderer> RENDER_MANAGER = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(Class<? extends Block> block, ICustomBlockRenderer render) {
        try {
            if (RENDER_MANAGER.containsKey(block)) {
                Examples.LOGGER.warn("Block {} 's renderer register again!", block.getSimpleName());
            }
            RENDER_MANAGER.put(block, render);
        } catch (Throwable throwable) {
            Examples.LOGGER.error("Can't register {}", block.getSimpleName(), throwable);
        }
    }

    @Nullable
    public static ICustomBlockRenderer getRenderer(BlockState block) {
        return getRenderer(block.getBlock());
    }

    @Nullable
    public static ICustomBlockRenderer getRenderer(Block block) {
        return getRenderer(block.getClass());
    }

    @Nullable
    public static ICustomBlockRenderer getRenderer(Class<? extends Block> block) {
        return RENDER_MANAGER.getOrDefault(block, null);
    }

    public static ImmutableList<ICustomBlockRenderer> getRenderers() {
        return ImmutableList.copyOf(RENDER_MANAGER.values());
    }
}
