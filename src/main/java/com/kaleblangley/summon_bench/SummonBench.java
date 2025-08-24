package com.kaleblangley.summon_bench;

import com.kaleblangley.summon_bench.common.init.BlockInit;
import com.kaleblangley.summon_bench.common.init.MenuInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(SummonBench.MODID)
public class SummonBench {
    public static final String MODID = "summon_bench";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SummonBench() {
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();

        BlockInit.init(modBusEvent);
        MenuInit.init(modBusEvent);
    }
}
