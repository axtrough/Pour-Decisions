package net.raccoon.will.pour_decisions.registry;

import net.raccoon.will.pour_decisions.client.hud.RevolverHud;
import net.raccoon.will.structura.client.gui.HudManager;

public class PDHudRegistry {

    public static void register() {
        HudManager.add(new RevolverHud());
    }
}
