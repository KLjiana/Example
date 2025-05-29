package org.mcteampotato.attchment;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Optional;

public class FoodData {
    public static FoodInfo getInfo(ResourceLocation itemId) {
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == null) return null;
        FoodProperties empty = new FoodProperties(0, 0, false, 0, Optional.empty(), new ArrayList<>());
        FoodProperties props = Optional.ofNullable(item.getFoodProperties(new ItemStack(item), null)).orElse(empty);
        int nutrition = props.nutrition();
        if (nutrition <= 0) return null;
        int durationTicks = (int) (10 * Math.log(nutrition+1) * 60 * 20);
        return new FoodInfo(itemId, nutrition, durationTicks, 0);
    }


    public static class FoodInfo {
        private final ResourceLocation id;
        private final int hearts;
        private final int durationTicks;
        private final int maxExtraHearts;

        public FoodInfo(ResourceLocation id, int hearts, int durationTicks, int maxExtraHearts) {
            this.id = id;
            this.hearts = hearts;
            this.durationTicks = durationTicks;
            this.maxExtraHearts = maxExtraHearts;
        }

        public ResourceLocation getId() {
            return id;
        }

        public int getHearts() {
            return hearts;
        }

        public int getDurationTicks() {
            return durationTicks;
        }

        public int getMaxExtraHearts() {
            return maxExtraHearts;
        }
    }
}
