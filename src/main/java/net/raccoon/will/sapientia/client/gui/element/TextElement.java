package net.raccoon.will.sapientia.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.raccoon.will.sapientia.client.gui.Anchor;

public class TextElement extends GuiElement {
    private Component text;
    private int color;
    private boolean shadow;

    public TextElement(Component text, int color, boolean shadow, Anchor anchor, int offsetX, int offsetY) {
        super(
                text != null ? Minecraft.getInstance().font.width(text) : 0,
                Minecraft.getInstance().font.lineHeight,
                anchor,
                offsetX, offsetY
        );
        this.text = text != null ? text : Component.empty();
        this.color = color;
        this.shadow = shadow;
    }

    public void setText(Component text) {
        this.text = text != null ? text : Component.empty();
        this.width = Minecraft.getInstance().font.width(this.text);
        this.height = Minecraft.getInstance().font.lineHeight;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {
        if (text != null) {
            this.width = Minecraft.getInstance().font.width(text);
            this.height = Minecraft.getInstance().font.lineHeight;
            graphics.drawString(Minecraft.getInstance().font, text, x, y, color, shadow);
        }
    }
}