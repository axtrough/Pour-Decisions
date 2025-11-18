package net.raccoon.will.sapientia.client.overlay.element;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.sapientia.client.overlay.Anchor;

//made by will >:3

public abstract class GuiElement {
    protected int width;
    protected int height;
    protected int offsetX;
    protected int offsetY;
    protected Anchor anchor;

    public GuiElement(int width, int height, Anchor anchor, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.anchor = anchor;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void render(GuiGraphics graphics, int screenWidth, int screenHeight, RenderGuiEvent.Pre event) {
        int x = calculateTopLeftX(screenWidth);
        int y = calculateTopLeftY(screenHeight);
        draw(graphics, x, y);
    }

    protected abstract void draw(GuiGraphics graphics, int x, int y);

    //don't talk to me about it.
    protected int calculateTopLeftX(int screenWidth) {
        int anchorX = switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> offsetX;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> (screenWidth / 2) + offsetX;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> screenWidth - offsetX;
        };

        int anchorPixelOffsetX = switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> 0;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> width / 2;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> width;
        };

        return anchorX - anchorPixelOffsetX;
    }

    protected int calculateTopLeftY(int screenHeight) {
        int anchorY = switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> offsetY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> (screenHeight / 2) + offsetY;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenHeight - offsetY;
        };

        int anchorPixelOffsetY = switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> height / 2;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> height;
        };

        return anchorY - anchorPixelOffsetY;
    }

}
