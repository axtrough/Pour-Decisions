package net.raccoon.will.sapientia.client.gui.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.raccoon.will.sapientia.api.client.gui.GuiElement;
import net.raccoon.will.sapientia.api.client.gui.Anchor;

public class SimpleElement extends GuiElement {
    protected ResourceLocation texture;
    protected final ResourceLocation originalTexture;
    protected final int texWidth, texHeight;

    public SimpleElement(ResourceLocation texture, int width, int height, int texWidth, int texHeight,
                         Anchor anchor, int offsetX, int offsetY) {
        super(width, height, anchor, offsetX, offsetY);
        this.texture = texture;
        this.originalTexture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    public void resetTexture() {
        this.texture = originalTexture;
    }

    public void setTexture(ResourceLocation texture) {
        if (!texture.equals(this.texture)) {
            this.texture = texture;
        }
    }

    @Override
    protected void draw(GuiGraphics graphics) {
        graphics.blit(texture, 0, 0, 0, 0, width, height, texWidth, texHeight);
    }
}
