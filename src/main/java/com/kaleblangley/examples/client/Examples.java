package com.kaleblangley.examples.client;

import com.kaleblangley.examples.client.impl.CustomBlockRenderRegisterer;
import com.kaleblangley.examples.client.renderer.DoorBlockRenderer;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(Examples.MODID)
public class Examples {
    public static final String MODID = "examples";
    public static final Logger LOGGER = LoggerFactory.getLogger("examples");

    public Examples() {
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBusEvent.<FMLClientSetupEvent>addListener(event -> {
                event.enqueueWork(() -> {
                    CustomBlockRenderRegisterer.register(DoorBlock.class, DoorBlockRenderer::new);
                });
            });
        });
    }
}
