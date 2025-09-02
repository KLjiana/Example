package com.kaleblangley.summon_bench.client.screen;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.common.config.SummonConfig;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import com.kaleblangley.summon_bench.network.NetworkHandler;
import com.kaleblangley.summon_bench.network.c2s.SummonEntityC2SPacket;
import com.kaleblangley.summon_bench.network.s2c.ConfigSyncS2CPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SummonScreen extends AbstractContainerScreen<SummonMenu> {
    public static final ResourceLocation SLOT = new ResourceLocation(SummonBench.MODID, "textures/gui/summon_slot.png");
    public static final ResourceLocation MOUTH = new ResourceLocation(SummonBench.MODID, "textures/gui/summon_mouth.png");
    private static final int SCREEN_WIDTH = 512;
    private static final int SCREEN_HEIGHT = 512;
    private static final float MOUTH_SCALE = 0.4f;
    private static final float SLOT_SCALE = 0.5f;
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
        float mouthX = Math.round(centerX - (float) SCREEN_WIDTH / 2 * MOUTH_SCALE + 5);
        float mouthY = Math.round(centerY - (float) SCREEN_HEIGHT / 2 * MOUTH_SCALE - 30);
        scaleRender(guiGraphics, mouthX, mouthY, MOUTH_SCALE, (x1, y2) -> {
            guiGraphics.blit(MOUTH, x1, y2, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        });

        float slotX = Math.round(centerX - (float) SCREEN_WIDTH / 2 * SLOT_SCALE + 5);
        float slotY = Math.round(centerY - (float) SCREEN_HEIGHT / 2 * SLOT_SCALE);
        scaleRender(guiGraphics, slotX, slotY, SLOT_SCALE, (x1, y2) -> {
            guiGraphics.blit(SLOT, x1, y2, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        });
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        String text = "即将召唤%s!".formatted(getSummonName());
        guiGraphics.drawCenteredString(font, text, centerX, centerY - 5, 0xFFFFFFFF);
    }

//    @Override
//    protected void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {
//        int i = slot.x;
//        int j = slot.y;
//        ItemStack itemstack = slot.getItem();
//        boolean flag = false;
//        boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
//        ItemStack itemstack1 = this.menu.getCarried();
//        String s = null;
//        if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemstack.isEmpty()) {
//            itemstack = itemstack.copyWithCount(itemstack.getCount() / 2);
//        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
//            if (this.quickCraftSlots.size() == 1) {
//                return;
//            }
//
//            if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) {
//                flag = true;
//                int k = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
//                int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
//                int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack1) + l;
//                if (i1 > k) {
//                    i1 = k;
//                    String var10000 = ChatFormatting.YELLOW.toString();
//                    s = var10000 + k;
//                }
//
//                itemstack = itemstack1.copyWithCount(i1);
//            } else {
//                this.quickCraftSlots.remove(slot);
//                this.recalculateQuickCraftRemaining();
//            }
//        }
//
//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
//        if (itemstack.isEmpty() && slot.isActive()) {
//            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
//            if (pair != null) {
//                TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
//                guiGraphics.blit(i, j, 0, 8, 8, textureatlassprite);
//                flag1 = true;
//            }
//        }
//
//        if (!flag1) {
//            if (flag) {
//                guiGraphics.fill(i, j, i + 8, j + 8, -2130706433);
//            }
//
//            ItemStack finalItemstack = itemstack;
//            scaleRender(guiGraphics, i, j, (x, y) -> guiGraphics.renderItem(finalItemstack, x, y, slot.x + slot.y * this.imageWidth));
//            guiGraphics.renderItemDecorations(this.font, itemstack, i, j, s);
//        }
//
//        guiGraphics.pose().popPose();
//    }

    private String getSummonName() {
        return "kubejs";
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
