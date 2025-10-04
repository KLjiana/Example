package com.kaleblangley.examples.client.impl;

import com.google.common.collect.ImmutableList;
import com.kaleblangley.examples.client.Examples;
import com.kaleblangley.examples.client.api.ICustomBlockRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomBlockRenderRegisterer {
    private static final Object2ObjectLinkedOpenHashMap<Class<? extends Block>, Supplier<ICustomBlockRenderer>> RENDER_MANAGER = new Object2ObjectLinkedOpenHashMap<>();

    public static void register(Class<? extends Block> block, Supplier<ICustomBlockRenderer> render) {
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
        return RENDER_MANAGER.getOrDefault(block, () -> null).get();
    }

    public static boolean hasCustomRenderer(BlockState block) {
        return hasCustomRenderer(block.getBlock());
    }

    public static boolean hasCustomRenderer(Block block) {
        return hasCustomRenderer(block.getClass());
    }

    //CACHE?
    public static boolean hasCustomRenderer(Class<? extends Block> block) {
        return RENDER_MANAGER.containsKey(block);
    }

    public static ImmutableList<Supplier<ICustomBlockRenderer>> getRenderers() {
        return ImmutableList.copyOf(RENDER_MANAGER.values());
    }
}
