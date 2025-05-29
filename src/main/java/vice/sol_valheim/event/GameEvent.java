package vice.sol_valheim.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import vice.sol_valheim.SOLValheim;
import vice.sol_valheim.attchment.FoodData;
import vice.sol_valheim.attchment.FoodDataAttachment;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = SOLValheim.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvent {
    @SubscribeEvent
    public static void tooltipsEvent(ItemTooltipEvent event) {
        List<Component> toolTips = event.getToolTip();
        ItemStack itemStack = event.getItemStack();
        FoodProperties foodProperties = itemStack.getFoodProperties(event.getEntity());
        if (foodProperties != null) {
            List<Component> list = new ArrayList<>();
            list.add(Component.literal("❤ %.1f Heart".formatted(((float) foodProperties.nutrition()) / 2)).withStyle(ChatFormatting.RED));
            list.add(Component.literal("☀ %s Regen".formatted(foodProperties.nutrition())).withStyle(ChatFormatting.DARK_RED));
            list.add(Component.literal("⌚ %s Minute".formatted((float) foodProperties.nutrition() / 10)).withStyle(ChatFormatting.GOLD));
            toolTips.addAll(1, list);
        }
    }

    @SubscribeEvent
    public static void tooltipsEvent(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        FoodData.FoodInfo info = FoodData.getInfo(id);
        if (item instanceof PotionItem) return;
        if (info != null && stack.getFoodProperties(player) != null) {
            FoodDataAttachment foodData = player.getData(SOLValheim.FOOD_DATA);
            if (foodData.isFull()){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event){
        Player player = event.getEntity();
        FoodDataAttachment foodData = player.getData(SOLValheim.FOOD_DATA);
        if (!player.level().isClientSide){
            foodData.tick();
        }
    }
}
