package net.raccoon.will.sapientia.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.raccoon.will.sapientia.client.gui.element.GuiElement;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {
    static final List<GuiElement> ELEMENTS = new ArrayList<>();

    public static void add(GuiElement element) {
        ELEMENTS.add(element);
    }

    public static void remove(GuiElement element) {
        ELEMENTS.remove(element);
    }

    public static void clear() {
        ELEMENTS.clear();
    }

    public static void render(GuiGraphics graphics, int screenWidth, int screenHeight, RenderGuiEvent.Pre event) {
        for (GuiElement element : ELEMENTS) {
            element.render(graphics, screenWidth, screenHeight, event);
        }
    }
}
