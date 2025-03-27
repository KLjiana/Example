package com.kaleblangley.examples;

import com.kaleblangley.examples.util.EnchantmentUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(Examples.MODID)
public class Examples {
    public static final String MODID = "examples";
    public static final Logger LOGGER = LoggerFactory.getLogger("examples");

    public Examples() {
        ForgeRegistries.ENCHANTMENTS.getValues().forEach(EnchantmentUtil::modifyEnchantment);
    }
}
