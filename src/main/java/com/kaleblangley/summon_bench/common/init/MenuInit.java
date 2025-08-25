package com.kaleblangley.summon_bench.common.init;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    private static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SummonBench.MODID);
    public static final RegistryObject<MenuType<SummonMenu>> SUMMON_MENU = MENU_TYPE.register("summon_menu", () -> IForgeMenuType.create(SummonMenu::new));

    public static void init(IEventBus bus) {
        MENU_TYPE.register(bus);
    }
}
