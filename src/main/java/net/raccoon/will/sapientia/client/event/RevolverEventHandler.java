package net.raccoon.will.sapientia.client.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class RevolverEventHandler {
    private static final Map<UUID, Boolean> shotFlag = new HashMap<>();

    public static void onRevolverShoot(Player player, ItemStack stack) {
        shotFlag.put(player.getUUID(), true);
    }

    public static boolean didShoot(Player player) {
        return shotFlag.getOrDefault(player.getUUID(), false);
    }

    public static void clearShoot(Player player) {
        shotFlag.put(player.getUUID(), false);
    }
}
