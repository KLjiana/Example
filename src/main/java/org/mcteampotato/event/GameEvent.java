package org.mcteampotato.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.attchment.FoodData;
import org.mcteampotato.attchment.FoodDataAttachment;
import org.mcteampotato.network.SyncFoodDataPacket;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = SOLValpotato.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {
    @SubscribeEvent
    public static void tooltipsEvent(ItemTooltipEvent event) {
        List<Component> toolTips = event.getToolTip();
        ItemStack itemStack = event.getItemStack();
        FoodData.FoodInfo foodInfo = FoodData.getInfo(BuiltInRegistries.ITEM.getKey(itemStack.getItem()));
        if (foodInfo != null) {
            if (itemStack.is(Items.OMINOUS_BOTTLE)) return;
            if (itemStack.is(Items.ROTTEN_FLESH)){
                toolTips.add(1, Component.translatable("tooltips.sol_valpotato.empty").withStyle(ChatFormatting.GREEN));
                return;
            }
            List<Component> list = new ArrayList<>();
            list.add(Component.literal("❤ %.1f Heart".formatted(((float) foodInfo.getHearts()) / 2)).withStyle(ChatFormatting.RED));
            list.add(Component.literal("⌚ %.1f Minute".formatted(((float) foodInfo.getDurationTicks()) / 1200)).withStyle(ChatFormatting.GOLD));
            toolTips.addAll(1, list);
        }
    }

    @SubscribeEvent
    public static void rightClick(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        FoodData.FoodInfo info = FoodData.getInfo(id);
        if (item instanceof PotionItem || stack.is(Items.ROTTEN_FLESH) || item instanceof OminousBottleItem) return;
        if (info != null && stack.getFoodProperties(player) != null) {
            FoodDataAttachment foodData = player.getData(SOLValpotato.FOOD_DATA);
            if (foodData.isFull() || foodData.isSame(item)){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer){
            FoodDataAttachment foodData = serverPlayer.getData(SOLValpotato.FOOD_DATA);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncFoodDataPacket(foodData.serialize()));
        }
    }

    @SubscribeEvent
    public static void inDamage(LivingIncomingDamageEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            long gameTime = serverPlayer.level().getGameTime();
            serverPlayer.getPersistentData().putLong("SOL:HurtTime", gameTime);
        }
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event){
        Player player = event.getEntity();
        FoodDataAttachment foodData = player.getData(SOLValpotato.FOOD_DATA);
        if (player instanceof ServerPlayer serverPlayer){
            foodData.tick(serverPlayer);

            long hurtTime = serverPlayer.getPersistentData().getLong("SOL:HurtTime");
            long gameTime = serverPlayer.level().getGameTime();
            if (hurtTime != 0 && gameTime-hurtTime>=200 && gameTime % 10 == 0){
                serverPlayer.heal(0.5f);
            }
        }
    }

    @SubscribeEvent
    public static void deathEvent(LivingDeathEvent event){
        if (event.getEntity() instanceof LocalPlayer localPlayer) {
            localPlayer.getData(SOLValpotato.FOOD_DATA).clear(localPlayer);
        }
    }
}
