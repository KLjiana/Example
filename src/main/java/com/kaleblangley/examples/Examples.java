package com.kaleblangley.examples;

import com.kaleblangley.examples.util.ClassUtil;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


@Mod(Examples.MODID)
public class Examples {
    public static final String MODID = "examples";
    public static final Logger LOGGER = LoggerFactory.getLogger("examples");

    public Examples() {
        ClassUtil.getAllEnchantment();
    }
}
