package com.kaleblangley.examples.event;

import com.kaleblangley.examples.Examples;
import com.kaleblangley.examples.screen.ColorPickerScreen;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Examples.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void hsvColor(RenderLevelStageEvent event){
//        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
//            Tesselator tesselator = Tesselator.getInstance();
//            BufferBuilder builder = tesselator.getBuilder();
//            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//            builder.vertex(0,0,0).color(1f,1f,1f,1f);
//            builder.vertex(0.5,0,0).color(1f,1f,1f,1f);
//            builder.vertex(0.5,0.5,0).color(0f,1f,0f,1f);
//            builder.vertex(0,0.5,0).color(0f,0f,0f,1f);
//            tesselator.end();
//        }
    }

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event){
        Button button = new Button.Builder(CommonComponents.EMPTY, ibutton -> Minecraft.getInstance().setScreen(new ColorPickerScreen())).pos(0, 0).build();
        if (!(event.getScreen() instanceof ColorPickerScreen)){
            event.addListener(button);
        }
    }
}
