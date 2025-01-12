package com.kaleblangley.examples;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod("examples")
public class Examples {
    public static final Logger LOGGER = LoggerFactory.getLogger("examples");
    public Examples(){
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
