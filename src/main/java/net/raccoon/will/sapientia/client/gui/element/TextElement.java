package net.raccoon.will.sapientia.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.raccoon.will.sapientia.api.client.gui.GuiElement;
import net.raccoon.will.sapientia.api.client.gui.Anchor;

public class TextElement extends GuiElement {
    private final Component originalText;
    private Component text;

    private final int originalColor;
    private boolean shadow;
    private int color;

    protected boolean dirty = true;
    protected int cachedHeight;
    protected int cachedWidth;

    public TextElement(Component text, int color, boolean shadow, Anchor anchor, int offsetX, int offsetY) {
        super(0, 0, anchor, offsetX, offsetY);
        this.originalText = text;
        this.originalColor = color;
        this.color = color;
        this.shadow = shadow;
        setText(text);
    }

    public void resetText() {
        setText(originalText);
    }

    public void resetColor() {
        this.color = originalColor;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        resetText();
        resetColor();
    }

    public void setText(Component text) {
        if (this.text == null || !this.text.equals(text)) {
            this.text = text != null ? text : Component.empty();
            dirty = true;
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    @Override
    public void updateSize() {
        if (dirty) {
            cachedWidth = Minecraft.getInstance().font.width(text);
            cachedHeight = Minecraft.getInstance().font.lineHeight;
            width = cachedWidth;
            height = cachedHeight;
            dirty = false;
        }
    }

    @Override
    protected void draw(GuiGraphics graphics) {
        if (text != null && !text.getString().isEmpty()) {
            graphics.drawString(Minecraft.getInstance().font, text, 0, 0, color, shadow);
        }
    }
}