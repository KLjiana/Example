package com.kaleblangley.summon_bench.client.screen;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SummonScreen extends AbstractContainerScreen<SummonMenu> {
    public static final ResourceLocation SCREEN = new ResourceLocation(SummonBench.MODID, "textures/gui/summon.png");
    private static final int SCREEN_WIDTH = 512;
    private static final int SCREEN_HEIGHT = 512;
    private static final float SCALE = 0.5f;
    private int centerX;
    private int centerY;

    public SummonScreen(SummonMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
//        this.imageWidth = 256;
//        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        this.addRenderableWidget(new Button.Builder(
                Component.literal("召唤"),
                button -> {

                })
                .pos(centerX - 20, centerY + 60)
                .width(40)
                .build()
        );
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        float x = centerX - 256 * SCALE + 5;
        float y = centerY - 256 * SCALE;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(SCALE, SCALE, 1.0F);
        guiGraphics.blit(SCREEN, 0, 0, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        pose.popPose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        String text = "即将召唤%s!".formatted("kubejs");
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, centerY + 40, 0xFFFFFFFF);
    }
}
