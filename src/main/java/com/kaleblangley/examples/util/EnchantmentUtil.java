package com.kaleblangley.examples.util;

import com.kaleblangley.examples.Examples;
import com.kaleblangley.examples.api.IEnchantmentParameter;
import com.kaleblangley.examples.mixin.accessor.EnchantmentAccessor;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentUtil {
    public static void modifyEnchantment(Enchantment enchantment){
        String descriptionId = enchantment.getDescriptionId();
        IEnchantmentParameter enchantmentParameter = (IEnchantmentParameter) enchantment;
        EnchantmentAccessor enchantmentAccessor = (EnchantmentAccessor) enchantment;

        enchantmentParameter.setMaxLevel(1);
    }
}
