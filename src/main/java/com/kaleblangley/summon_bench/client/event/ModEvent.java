package com.kaleblangley.summon_bench.client.event;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.client.render.block.SummonBenchRenderer;
import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SummonBench.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.SUMMON_BENCH.get(), context -> new SummonBenchRenderer());
    }
}
