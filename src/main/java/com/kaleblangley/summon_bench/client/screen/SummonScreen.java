package com.kaleblangley.summon_bench.client.screen;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import com.kaleblangley.summon_bench.network.NetworkHandler;
import com.kaleblangley.summon_bench.network.c2s.SummonEntityC2SPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SummonScreen extends AbstractContainerScreen<SummonMenu> {
    public static final ResourceLocation SLOT = new ResourceLocation(SummonBench.MODID, "textures/gui/summon_slot.png");
    public static final ResourceLocation MOUTH = new ResourceLocation(SummonBench.MODID, "textures/gui/summon_mouth.png");
    private static final int MOUTH_WIDTH = 512;
    private static final int MOUTH_HEIGHT = 512;
    private static final int SLOT_WIDTH = 186;
    private static final int SLOT_HEIGHT = 95;
    private static final float MOUTH_SCALE = 0.4f;
    private static final float SLOT_SCALE = 1.0f;
    private int centerX;
    private int centerY;

    public SummonScreen(SummonMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
//        this.imageWidth = SCREEN_WIDTH / 2;
//        this.imageHeight = SCREEN_HEIGHT / 2;
    }

    @Override
    protected void init() {
        super.init();
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        this.addRenderableWidget(new Button.Builder(
                Component.literal("召唤"),
                button -> {
                    SummonMenu summonMenu = this.menu;
                    ItemStack itemStack = summonMenu.summon.getItem(0);
                    var entries = ConfigLoader.getConfig().getSummonEntity(itemStack);
                    if (entries.isEmpty()) {
                        SummonBench.LOGGER.debug("Can't match any entity by {} in Client Side", itemStack);
                        return;
                    }
                    NetworkHandler.CHANNEL.sendToServer(new SummonEntityC2SPacket(summonMenu.entityPos));
                    this.onClose();
                })
                .pos(centerX - 15, centerY + 12)
                .size(30, 16)
                .build()
        );
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        float mouthX = Math.round(centerX - (float) MOUTH_WIDTH / 2 * MOUTH_SCALE + 5);
        float mouthY = Math.round(centerY - (float) MOUTH_HEIGHT / 2 * MOUTH_SCALE - 30);
        scaleRender(guiGraphics, mouthX, mouthY, MOUTH_SCALE, (x1, y2) -> {
            guiGraphics.blit(MOUTH, x1, y2, 0, 0, MOUTH_WIDTH, MOUTH_HEIGHT, MOUTH_WIDTH, MOUTH_HEIGHT);
        });

        float slotX = Math.round(centerX - (float) SLOT_WIDTH / 2 * SLOT_SCALE + 5);
        float slotY = Math.round(centerY - (float) SLOT_HEIGHT / 2 * SLOT_SCALE + 80);
        scaleRender(guiGraphics, slotX, slotY, SLOT_SCALE, (x1, y2) -> {
            guiGraphics.blit(SLOT, x1, y2, 0, 0, SLOT_WIDTH, SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
        });
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        Component text = Component.literal("即将召唤").append(getSummonName()).append("!");
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, centerY - 5, 0xFFFFFFFF);
    }

    private Component getSummonName() {
        ItemStack itemStack = this.menu.summon.getItem(0);
        var entries = ConfigLoader.getConfig().getSummonEntity(itemStack);
        return entries.isEmpty() ? Component.literal("......") : entries.get(0).getEntity(this.minecraft.level).getName();
    }

    private void scaleRender(GuiGraphics guiGraphics, int x, int y, float scale, ScaleRender renderer) {
        scaleRender(guiGraphics, (float) x, (float) y, scale, renderer);
    }

    private void scaleRender(GuiGraphics guiGraphics, float x, float y, float scale, ScaleRender renderer) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, 1.0F);
        renderer.render(0, 0);
        pose.popPose();
    }

    @FunctionalInterface
    private interface ScaleRender {
        void render(int x, int y);
    }
}
