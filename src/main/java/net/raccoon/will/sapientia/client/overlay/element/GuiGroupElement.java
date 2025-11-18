package net.raccoon.will.sapientia.client.overlay.element;

import net.minecraft.client.gui.GuiGraphics;
import net.raccoon.will.sapientia.client.overlay.Anchor;

import java.util.ArrayList;
import java.util.List;

public class GuiGroupElement extends GuiElement{
    private final List<GuiElement> children = new ArrayList<>();

    public GuiGroupElement(int width, int height, Anchor anchor, int offsetX, int offsetY) {
        super(width, height, anchor, offsetX, offsetY);
    }

    public void addChild(GuiElement child) {
        children.add(child);
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {
        for (GuiElement child : children) {

            int childX = x + child.calculateTopLeftX(this.width);
            int childY = y + child.calculateTopLeftY(this.height);

            child.draw(graphics, childX, childY);
        }
    }
}
