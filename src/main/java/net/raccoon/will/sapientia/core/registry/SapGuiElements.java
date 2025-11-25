package net.raccoon.will.sapientia.core.registry;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.raccoon.will.sapientia.api.client.gui.*;
import net.raccoon.will.sapientia.client.gui.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SapGuiElements {
    public static final List<GuiElement> ELEMENTS = new ArrayList<>();
    private static TextElement gunText;
    private static ItemElement bulletIcon;
    private static GuiGroup gunInfo;

    public static TextElement gunText() {
        if (gunText == null) {
            gunText = create(() -> new TextElement(
                    Component.literal(""), 0xd185d6, true,
                    Anchor.BOTTOM_CENTER, -121, 6
            ));
        }
        return gunText;
    }

    public static ItemElement bulletIcon() {
        if (bulletIcon == null) {
            bulletIcon = create(() -> new ItemElement(
                    new ItemStack(SapItems.BULLET.get()), 16, 16,
                    Anchor.BOTTOM_CENTER, -102, 3
            ));
        }
        return bulletIcon;
    }

//    public static GuiGroup gunInfo() {
//        if (gunInfo == null) {
//            gunInfo = createGroup(() -> new GuiGroup(Anchor.BOTTOM_CENTER, 100, 6)
//                    .setGroupAnchor(ElementAnchor.BOTTOM_RIGHT)
//                    .setLayout(Layout.HORIZONTAL)
//                    .setSpacing(2)
//                    .add(gunText())
//                    .add(bulletIcon()));
//        }
//        return gunInfo;
//    }

    public static void init() {
        bulletIcon();
        gunText();
    }


    private static <T extends GuiElement> T create(Supplier<T> supplier) {
        T element = supplier.get();
        ELEMENTS.add(element);
        GuiManager.add(element);
        return element;
    }

    private static <T extends GuiElement> T createChild(Supplier<T> supplier) {
        T element = supplier.get();
        ELEMENTS.add(element);
        return element;
    }

    private static <T extends GuiElement> T createGroup(Supplier<T> supplier) {
        T element = supplier.get();
        ELEMENTS.add(element);
        GuiManager.add(element);
        return element;
    }

    public static List<GuiElement> all() {
        return ELEMENTS;
    }
}
