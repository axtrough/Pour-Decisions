package net.raccoon.will.sapientia.core.registry;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.api.client.gui.GuiElement;
import net.raccoon.will.sapientia.api.client.gui.Anchor;
import net.raccoon.will.sapientia.api.client.gui.GuiManager;
import net.raccoon.will.sapientia.client.gui.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SapGuiElements {
    public static final List<GuiElement> ELEMENTS = new ArrayList<>();
    private static TextElement textTest;
    private static ItemElement itemTest;

    public static TextElement textHeld() {
        if (textTest == null)
            textTest = create(() -> new TextElement(
                    Component.literal("Cheese balls loves my balls"),
                    0xd185d6, true,
                    Anchor.TOP_CENTER, 0, 26));
        return textTest;
    }

    public static ItemElement itemHeld() {
        if (itemTest == null)
            itemTest = create(() -> new ItemElement(
                    new ItemStack(SapItems.HOME_RUNE.get()),
                    16, 16,
                    Anchor.TOP_CENTER, 0, 10));
        return itemTest;
    }

    public static void init() {
        itemHeld();
        textHeld();
    }

    //----------------------------------------------
    public static List<GuiElement> all() {
        return ELEMENTS;
    }

    private static void register(GuiElement element) {
        ELEMENTS.add(element);
        GuiManager.add(element);
    }

    private static <T extends GuiElement> T create(Supplier<T> supplier) {
        T element = supplier.get();
        ELEMENTS.add(element);
        GuiManager.add(element);
        return element;
    }
}
