package com.kaleblangley.count_craft.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class DrawableCount implements IDrawable {
    private final int count;
    private int width;
    private int height;

    public DrawableCount(int count) {
        this.count = count;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
        Font font = Minecraft.getInstance().font;
        String text = String.valueOf(count);

        guiGraphics.pose().translate(0.0F, 0.0F, 0.0F);
        guiGraphics.drawString(font, text, xOffset + 19 - 2 - font.width(text), yOffset + 6 + 3, 16777215, true);
    }
}
