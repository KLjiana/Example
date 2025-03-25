package com.kaleblangley.examples.mixin.accessor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {
    @Mutable
    @Accessor("slots")
    EquipmentSlot[] getSlot();
    @Mutable
    @Accessor("slots")
    void setSlot(EquipmentSlot[] equipmentSlots);

    @Mutable
    @Accessor("rarity")
    Enchantment.Rarity getRarity();
    @Mutable
    @Accessor("rarity")
    void setRarity(Enchantment.Rarity rarity);

    @Mutable
    @Accessor("category")
    EnchantmentCategory getCategory();
    @Mutable
    @Accessor("category")
    void setCategory(EnchantmentCategory enchantmentCategory);
}
