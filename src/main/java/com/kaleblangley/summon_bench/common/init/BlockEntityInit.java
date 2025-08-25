package com.kaleblangley.summon_bench.common.init;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SummonBench.MODID);
    public static final RegistryObject<BlockEntityType<SummonBenchBlockEntity>> SUMMON_BENCH = BLOCK_ENTITY_TYPE.register("summon_bench", () -> BlockEntityType.Builder.of(SummonBenchBlockEntity::new, BlockInit.SUMMON_BENCH.get()).build(null));

    public static void init(IEventBus bus) {
        BLOCK_ENTITY_TYPE.register(bus);
    }
}
