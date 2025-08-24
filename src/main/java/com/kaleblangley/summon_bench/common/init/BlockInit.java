package com.kaleblangley.summon_bench.common.init;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.SummonBenchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    private static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, SummonBench.MODID);
    public static final RegistryObject<Block> SUMMON_BENCH = BLOCK.register("summon_bench", SummonBenchBlock::new);

    public static void init(IEventBus bus) {
        BLOCK.register(bus);
    }
}
