package net.raccoon.will.sapientia.client.overlay.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.raccoon.will.sapientia.client.overlay.Anchor;

public class ProgressBarElement  extends GuiElement{
    private final ResourceLocation texture;
    private final int texWidth, texHeight;
    private final int emptyU, emptyV; //pixel coords for the empty bar
    private final int fullU, fullV; //pixel coords for the full bar
    private float progress;

    //barWidth, barHeight explains itself

    public ProgressBarElement(ResourceLocation texture, int barWidth, int barHeight, int texWidth, int texHeight, int emptyU, int emptyV, int fullU, int fullV,
                              Anchor anchor, int offsetX, int offsetY) {
        super(barWidth, barHeight, anchor, offsetX, offsetY);
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.emptyU = emptyU;
        this.emptyV = emptyV;
        this.fullU = fullU;
        this.fullV = fullV;
    }

    public void setProgress(float progress) {
        this.progress = Math.min(1.0F, Math.max(0.0F, progress));
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {

        int filledWidth = (int) (width * progress);

        graphics.blit(texture, x, y, emptyU, emptyV, width, height, texWidth, texHeight);

        if (filledWidth > 0) {
            graphics.blit(texture, x, y, fullU, fullV, filledWidth, height, texWidth, texHeight);
        }
    }
}
