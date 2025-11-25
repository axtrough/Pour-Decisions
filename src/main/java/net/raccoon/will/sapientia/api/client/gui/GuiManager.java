package net.raccoon.will.sapientia.api.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {
    static final List<GuiElement> ELEMENTS = new ArrayList<>();
    public static boolean DEBUG_RENDER_TIME = false;

    public static void add(GuiElement element) {
        ELEMENTS.add(element);
    }

    public static void remove(GuiElement element) {
        ELEMENTS.remove(element);
    }

    public static void clear() {
        ELEMENTS.clear();
    }

    public static void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, RenderGuiEvent.Pre event) {
        long start = 0;
        if (DEBUG_RENDER_TIME) start = System.nanoTime();

        // actual rendering
        for (GuiElement element : ELEMENTS) {
            if (!element.isChildElement()) {
                element.render(guiGraphics, screenWidth, screenHeight, event);
            }
        }

        if (DEBUG_RENDER_TIME) {
            long end = System.nanoTime();
            System.out.println("Render took: " + (end - start) / 1_000_000.0 + " ms");
        }
    }
}
