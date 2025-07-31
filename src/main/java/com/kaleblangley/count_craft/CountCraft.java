package com.kaleblangley.count_craft;

import com.kaleblangley.count_craft.init.RecipeSerializerInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Mod(CountCraft.MODID)
public class CountCraft {
    public static final String MODID = "count_craft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public CountCraft() {
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
        RecipeSerializerInit.init(modBusEvent);
    }
}
