package com.kaleblangley.summon_bench.client.event;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.client.render.block.SummonBenchRenderer;
import com.kaleblangley.summon_bench.client.screen.SummonScreen;
import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import com.kaleblangley.summon_bench.common.init.BlockInit;
import com.kaleblangley.summon_bench.common.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SummonBench.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvent {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.SUMMON_BENCH.get(), SummonBenchRenderer::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuInit.SUMMON_MENU.get(), SummonScreen::new);
        });
    }
}
