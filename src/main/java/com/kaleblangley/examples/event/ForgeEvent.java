package com.kaleblangley.examples.event;

import com.kaleblangley.examples.Examples;
import com.kaleblangley.examples.screen.ChunkShowScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Examples.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event){
        Button button = new Button.Builder(CommonComponents.EMPTY, ibutton -> Minecraft.getInstance().setScreen(new ChunkShowScreen())).pos(0, 0).build();
        if (event.getScreen() instanceof InventoryScreen){
            event.addListener(button);
        }
    }
}
