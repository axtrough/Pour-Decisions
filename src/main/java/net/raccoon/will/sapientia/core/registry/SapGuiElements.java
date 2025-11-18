package net.raccoon.will.sapientia.core.registry;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.client.overlay.Anchor;
import net.raccoon.will.sapientia.client.overlay.GuiManager;
import net.raccoon.will.sapientia.client.overlay.element.GuiElement;
import net.raccoon.will.sapientia.client.overlay.element.ItemElement;
import net.raccoon.will.sapientia.client.overlay.element.TextElement;

import java.util.ArrayList;
import java.util.List;

public class SapGuiElements {
    public static final List<GuiElement> ELEMENTS = new ArrayList<>();

    public static final TextElement TEXT_TEST = register(
            new TextElement(
                    Component.literal("Hello Will >:3"),
                    0xd185d6, false,
                    Anchor.TOP_LEFT,
                    10, 10));

    public static final ItemElement ITEM_TEST = register(
            new ItemElement(
                    new ItemStack(SapItems.HOME_RUNE.get()),
                    16, 16,
                    Anchor.TOP_RIGHT,
                    10, 10));

    private static <T extends GuiElement> T register(T element) {
        ELEMENTS.add(element);
        return element;
    }

    public static void init() {
        ELEMENTS.forEach(GuiManager::add);
    }
}
