package com.kaleblangley.examples.util;

import com.kaleblangley.examples.Examples;
import com.kaleblangley.examples.api.IEnchantmentParameter;
import com.kaleblangley.examples.mixin.accessor.EnchantmentAccessor;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentUtil {
    public void modifyEnchantment(Enchantment enchantment){
        String descriptionId = enchantment.getDescriptionId();
        IEnchantmentParameter enchantmentParameter = (IEnchantmentParameter) enchantment;
        EnchantmentAccessor enchantmentAccessor = (EnchantmentAccessor) enchantment;

        Examples.LOGGER.info(descriptionId);
    }
}
