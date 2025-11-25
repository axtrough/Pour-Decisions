package net.raccoon.will.sapientia.api.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

//made by will >:3

public abstract class GuiElement {
    protected int width, height, offsetX, offsetY;
    protected Anchor anchor;
    protected float scale = 1.0f;
    protected float alpha = 1.0f;
    protected float targetAlpha = 1.0f;
    protected boolean visible = true;
    protected ElementAnchor elementAnchor = ElementAnchor.TOP_LEFT;
    protected GuiGroup parent = null;
    protected boolean debug = true;

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

    public void setScale(float scale) {
        this.scale = scale; }

    public void setDebug (boolean debug) {
        this.debug = debug; }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX; }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY; }

    public void setVisible(boolean visible) {
        this.visible = visible; }

    public void setAlpha(float alpha) {
        this.alpha = Math.min(1.0f, Math.max(0.0f, alpha)); }

    public void setElementAnchor (ElementAnchor elementAnchor) {
        this.elementAnchor = elementAnchor; }

    public boolean isVisible() { return visible; }
    public void updateSize() {}

    public boolean isChildElement() {
        return parent != null;
    }

    public float getAlpha() { return alpha; }
    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
    public int getOriginalOffsetX() { return originalOffsetX; }
    public int getOriginalOffsetY() { return originalOffsetY; }

    public void resetScale() { this.scale = originalScale; }
    public void resetSize() { this.width = originalWidth; this.height = originalHeight; }
    public void resetOffset() { this.offsetX = originalOffsetX; this.offsetY = originalOffsetY; }
    public void resetAll() { resetOffset(); resetScale(); resetSize(); }

    public void fadeTo(float target, float fadeDurationSeconds, float deltaSeconds) {
        this.targetAlpha = target;
        float speed = deltaSeconds / fadeDurationSeconds;
        this.alpha += (targetAlpha - this.alpha) * speed;
        if (Math.abs(targetAlpha - this.alpha) < 0.01f) {
            this.alpha = targetAlpha;
        }
    }

    public void render(GuiGraphics graphics, int screenWidth, int screenHeight, RenderGuiEvent.Pre event) {
        if (!visible) return;
        updateSize();
        int x = calculateTopLeftX(screenWidth);
        int y = calculateTopLeftY(screenHeight);

        graphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scale, scale, 1);

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        draw(graphics);

        if (debug) {
            graphics.fill(0, 0, width, height, 0x40FF0000);
            graphics.renderOutline(0, 0, width, height, 0xFFFF0000);
        }
        graphics.pose().popPose();
    }

    protected abstract void draw(GuiGraphics graphics);

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
