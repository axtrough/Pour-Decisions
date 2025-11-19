package net.raccoon.will.sapientia.core.registry;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.client.overlay.Anchor;
import net.raccoon.will.sapientia.client.overlay.GuiManager;
import net.raccoon.will.sapientia.client.overlay.element.GuiElement;
import net.raccoon.will.sapientia.client.overlay.element.ItemElement;
import net.raccoon.will.sapientia.client.overlay.element.SimpleElement;
import net.raccoon.will.sapientia.client.overlay.element.TextElement;

import java.util.ArrayList;
import java.util.List;

public class SapGuiElements {
    public static final List<GuiElement> ELEMENTS = new ArrayList<>();

    public static final TextElement TEXT_TEST = register(
            new TextElement(
                    Component.literal("Home Rune"),
                    0xd185d6, true,
                    Anchor.TOP_LEFT,
                    27, 10));

    public static final ItemElement ITEM_TEST = register(
            new ItemElement(
                    new ItemStack(SapItems.HOME_RUNE.get()),
                    16, 16,
                    Anchor.TOP_LEFT,
                    10, 10));

    public static final SimpleElement SIMPLE_ELEMENT = register(
            new SimpleElement(
                    ResourceLocation.fromNamespaceAndPath("sapientia", "textures/gui/home_rune.png"),
                    32, 32, 16, 16,
                    Anchor.TOP_CENTER, 0, 10
    ));

    private static <T extends GuiElement> T register(T element) {
        ELEMENTS.add(element);
        return element;
    }

    public static void init() {
        ELEMENTS.forEach(GuiManager::add);
    }
}
