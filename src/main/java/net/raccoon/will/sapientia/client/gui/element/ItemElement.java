package net.raccoon.will.sapientia.client.gui.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.client.gui.Anchor;

public class ItemElement extends GuiElement {
    private ItemStack item;

    public ItemElement(ItemStack stack, int width, int height, Anchor anchor, int offsetX, int offsetY) {
        super(width, height, anchor, offsetX, offsetY);
        this.item = stack;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
    }

    @Override
    protected void draw(GuiGraphics graphics, int x, int y) {
        if (item != null && !item.isEmpty()) {
            graphics.renderItem(item, x, y);
        }
    }
}