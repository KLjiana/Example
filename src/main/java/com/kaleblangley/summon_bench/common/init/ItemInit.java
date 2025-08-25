package com.kaleblangley.summon_bench.common.init;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.item.SummonBenchItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    private static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, SummonBench.MODID);
    public static final RegistryObject<Item> SUMMON_BENCH = ITEM.register("summon_bench", SummonBenchItem::new);

    public static void init(IEventBus bus) {
        ITEM.register(bus);
    }
}
