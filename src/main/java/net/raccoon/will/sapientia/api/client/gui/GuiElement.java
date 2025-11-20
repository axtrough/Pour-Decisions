package net.raccoon.will.sapientia.api.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

//made by will >:3

public abstract class GuiElement {
    protected int width, height, offsetX, offsetY;
    protected Anchor anchor;
    protected float scale = 1.0f;

    protected final int originalWidth, originalHeight, originalOffsetX, originalOffsetY;
    protected final float originalScale;

    public GuiElement(int width, int height, Anchor anchor, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.anchor = anchor;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        this.originalWidth = width;
        this.originalHeight = height;
        this.originalOffsetX = offsetX;
        this.originalOffsetY = offsetY;
        this.originalScale = scale;
    }

    public void updateSize() {
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void resetScale() {
        this.scale = originalScale;
    }

    public void resetOffset() {
        this.offsetX = originalOffsetX;
        this.offsetY = originalOffsetY;
    }

    public void resetSize() {
        this.width = originalWidth;
        this.height = originalHeight;
    }

    public void resetAll() {
        resetOffset();
        resetScale();
        resetSize();
    }

    protected abstract void draw(GuiGraphics graphics);
    public void render(GuiGraphics graphics, int screenWidth, int screenHeight, RenderGuiEvent.Pre event) {
        updateSize();
        int x = calculateTopLeftX(screenWidth);
        int y = calculateTopLeftY(screenHeight);

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1);

        draw(graphics);
        graphics.pose().popPose();
    }

    //don't talk to me about it.
    protected int calculateTopLeftX(int screenWidth) {
        int anchorX = switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> offsetX;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> screenWidth / 2 + offsetX;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> screenWidth - offsetX;
        };

        int anchorOffsetX = switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> 0;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> (int) (width * scale / 2f);
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> (int) (width * scale);
        };

        return anchorX - anchorOffsetX;
    }

    protected int calculateTopLeftY(int screenHeight) {
        int anchorY = switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> offsetY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> screenHeight / 2 + offsetY;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenHeight - offsetY;
        };

        int anchorOffsetY = switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> (int) (height * scale / 2f);
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> (int) (height * scale);
        };

        return anchorY - anchorOffsetY;
    }
}
