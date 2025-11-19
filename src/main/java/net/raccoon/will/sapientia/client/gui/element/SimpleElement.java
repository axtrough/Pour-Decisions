package net.raccoon.will.sapientia.client.gui.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.raccoon.will.sapientia.client.gui.Anchor;

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
        graphics.pose().pushPose();
        float scaleX = (float) width / (float) texWidth;
        float scaleY = (float) height / (float) texHeight;

        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(scaleX, scaleY, 1);

        graphics.blit(texture, 0, 0, 0, 0, texWidth, texHeight, texWidth, texHeight);

        graphics.pose().popPose();
    }
}
