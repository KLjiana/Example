package vice.sol_valheim.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.sol_valheim.SOLValheim;
import vice.sol_valheim.attchment.FoodData;
import vice.sol_valheim.attchment.FoodDataAttachment;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir){
        Player player = (Player) (Object) this;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(food.getItem());
        FoodData.FoodInfo info = FoodData.getInfo(id);
        FoodDataAttachment foodData = player.getData(SOLValheim.FOOD_DATA);
        if (!player.isLocalPlayer()) {
            if (food.is(Items.ROTTEN_FLESH)){
                foodData.clear(player);
            } else {
                foodData.addFood(info, player);
            }
        }
    }
}
