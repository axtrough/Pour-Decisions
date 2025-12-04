package net.raccoon.will.pour_decisions.client.input;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class PDKeybinds {
    public static KeyMapping RELOAD_KEY;

    public static void register(RegisterKeyMappingsEvent event) {
        RELOAD_KEY = new KeyMapping(
                "key.pour_decisions.reload",
                GLFW.GLFW_KEY_R,
                "key.categories.gameplay"
        );
        event.register(RELOAD_KEY);
    }
}