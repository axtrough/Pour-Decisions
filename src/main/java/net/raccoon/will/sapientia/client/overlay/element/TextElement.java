package net.raccoon.will.sapientia.client.overlay.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.sapientia.client.overlay.Anchor;

public class TextElement extends GuiElement{
    private Component text;
    private int color;
    private boolean shadow;

    public TextElement(Component text, int color, boolean shadow, Anchor anchor, int offsetX, int offsetY) {
        super(0, 0, anchor, offsetX, offsetY);
        this.text = text;
        this.color = color;
        this.shadow = shadow;
    }

    public void setText(Component text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {
        graphics.drawString(Minecraft.getInstance().font, text, x, y, color, shadow);
    }
}