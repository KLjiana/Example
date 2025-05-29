package org.mcteampotato.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.mcteampotato.SOLValpotato;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.mcteampotato.attchment.FoodData;
import org.mcteampotato.attchment.FoodDataAttachment;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir){
        Player player = (Player) (Object) this;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(food.getItem());
        FoodData.FoodInfo info = FoodData.getInfo(id);
        FoodDataAttachment foodData = player.getData(SOLValpotato.FOOD_DATA);
        if (!player.isLocalPlayer()) {
            if (food.is(Items.ROTTEN_FLESH)){
                foodData.clear(player);
            } else {
                foodData.addFood(info, player);
            }
        }
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, foodProperties.saturation() >= 30 ? 30 : Math.round(foodProperties.saturation()), (int) foodProperties.saturation() / 30));
    }

    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    public void alwaysEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
}
