package org.mcteampotato.attchment;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mcteampotato.attchment.compat.SOLCompat;
import org.mcteampotato.attchment.compat.SomeAssemblyRequired;

import java.util.ArrayList;
import java.util.Optional;

public class FoodData {
    public static final FoodProperties empty = new FoodProperties(0, 0, false, 0, Optional.empty(), new ArrayList<>());

    public static FoodInfo getInfo(ItemStack itemStack) {
        FoodProperties props = Optional.ofNullable(itemStack.getFoodProperties(null)).orElse(empty);
        int nutrition = props.nutrition();
        if (nutrition <= 0) return null;
        int durationTicks = (int) (10 * Math.log(nutrition+1) * 60 * 20);
//        int durationTicks = (nutrition) * 60 * 20; // DEBUG;
        FoodInfo foodInfo = new FoodInfo(itemStack, nutrition, props.saturation(), nutrition, durationTicks);
        if (SOLCompat.isLoadSomeAssemblyRequired()) {
            foodInfo = SomeAssemblyRequired.tryResetFoodInfo(foodInfo, itemStack);
        }
        return foodInfo;
    }


    public static class FoodInfo {
        private final ItemStack itemStack;
        private final int nutrition;
        private final float saturation;
        private final int hearts;
        private final int durationTicks;
        private final int maxExtraHearts;

        public FoodInfo(ItemStack itemStack, int nutrition, float saturation, int hearts, int durationTicks) {
            this(itemStack, nutrition, saturation, hearts, durationTicks, 0);
        }

        public FoodInfo(ItemStack itemStack, int nutrition, float saturation, int hearts, int durationTicks, int maxExtraHearts) {
            this.itemStack = itemStack;
            this.nutrition = nutrition;
            this.saturation = saturation;
            this.hearts = hearts;
            this.durationTicks = durationTicks;
            this.maxExtraHearts = maxExtraHearts;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getNutrition() {
            return nutrition;
        }

        public float getSaturation() {
            return saturation;
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
