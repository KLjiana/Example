package org.mcteampotato.attchment.compat;

import net.minecraft.world.item.ItemStack;
import org.mcteampotato.attchment.FoodData;
import someassemblyrequired.item.sandwich.SandwichContents;
import someassemblyrequired.registry.ModItems;

public class SomeAssemblyRequired {
    public static boolean isSandwich(ItemStack itemStack){
        return itemStack.is(ModItems.SANDWICH);
    }

    public static FoodData.FoodInfo tryResetFoodInfo(FoodData.FoodInfo defaultInfo, ItemStack itemStack){
        if (isSandwich(itemStack)){
            SandwichContents contents = SandwichContents.get(itemStack);
            int nutrition = contents.nutrition(null);
            if (nutrition <= 0) return defaultInfo;
            float saturation = contents.saturation(null);
            int durationTicks = (int) (10 * Math.log(nutrition+1) * 60 * 20);
//            int durationTicks = (nutrition) * 60 * 20; // DEBUG;
            return new FoodData.FoodInfo(itemStack, nutrition, saturation, nutrition, durationTicks);
        }
        return defaultInfo;
    }
}
