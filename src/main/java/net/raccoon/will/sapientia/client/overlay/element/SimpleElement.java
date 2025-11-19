package net.raccoon.will.sapientia.client.overlay.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.raccoon.will.sapientia.client.overlay.Anchor;

public class SimpleElement extends GuiElement {
    protected final ResourceLocation texture;
    protected final int texWidth, texHeight;

    public SimpleElement(ResourceLocation texture, int width, int height, int texWidth, int texHeight,
                         Anchor anchor, int offsetX, int offsetY) {
        super(width, height, anchor, offsetX, offsetY);
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {
        graphics.blit(texture, x, y, 0, 0, width, height, texWidth, texHeight);
    }
}
