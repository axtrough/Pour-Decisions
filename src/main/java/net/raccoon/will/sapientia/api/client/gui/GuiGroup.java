package net.raccoon.will.sapientia.api.client.gui;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class GuiGroup extends GuiElement {
    private final List<GuiElement> children = new ArrayList<>();
    private Layout layout = Layout.HORIZONTAL;
    private int spacing = 2;

    public GuiGroup(Anchor anchor, int offsetX, int offsetY) {
        super(0, 0, anchor, offsetX, offsetY);
    }

    public GuiGroup add(GuiElement element) {
        element.parent = this;
        children.add(element);
        updateChildrenOffsets();
        return this;
    }

    public GuiGroup setLayout(Layout layout) {
        this.layout = layout;
        updateChildrenOffsets();
        return this;
    }

    public GuiGroup setSpacing(int spacing) {
        this.spacing = spacing;
        updateChildrenOffsets();
        return this;
    }

    public GuiGroup setGroupAnchor(ElementAnchor elementAnchor) {
        this.elementAnchor = elementAnchor;
        return this;
    }

    @Override
    public void setOffsetX(int offsetX) {
        super.setOffsetX(offsetX);
        updateChildrenOffsets();
    }


    @Override
    public void setOffsetY(int offsetY) {
        super.setOffsetY(offsetY);
        updateChildrenOffsets();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        for (GuiElement child : children) {
            child.setVisible(visible);
        }
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        for (GuiElement child : children) {
            child.setScale(scale);
        }
    }

    private void updateChildrenOffsets() {
        int x = 0, y = 0;
        for (GuiElement child : children) {
            child.setOffsetX(offsetX + x);
            child.setOffsetY(offsetY + y);
            if (layout == Layout.HORIZONTAL) x += child.width + spacing;
            else y += child.height + spacing;
        }

        // auto-size group
        if (layout == Layout.HORIZONTAL) {
            width = x - spacing;
            height = children.stream().mapToInt(c -> c.height).max().orElse(0);
        } else {
            height = y - spacing;
            width = children.stream().mapToInt(c -> c.width).max().orElse(0);
        }
    }


    public void resetAllChildren() {
        for (GuiElement child : children) {
            child.resetAll();
        }
    }

    @Override
    protected void draw(GuiGraphics graphics) {
        int absX = calculateTopLeftX(graphics.guiWidth());
        int absY = calculateTopLeftY(graphics.guiHeight());

        if (debug) {
            graphics.fill(absX, absY, width, height, 0x4000FF00);
            graphics.renderOutline(absX, absY, width, height, 0xFF00FF00);
        }

        for (GuiElement child : children) {
            child.render(graphics, width, height, null);
        }
    }
}
