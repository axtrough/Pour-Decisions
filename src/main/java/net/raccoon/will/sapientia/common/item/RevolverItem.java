package net.raccoon.will.sapientia.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.raccoon.will.sapientia.core.registry.SapComponents;
import net.raccoon.will.sapientia.core.registry.SapDamageTypes;
import net.raccoon.will.sapientia.core.registry.SapSounds;

import java.util.ArrayList;
import java.util.List;

public class RevolverItem extends Item {
    public RevolverItem(Properties properties) {
        super(properties);
    }

    public List<Integer> getBulletChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.BULLET_CHAMBERS.get(), new ArrayList<>());
    }

    public int getBulletCount(ItemStack stack) {
        return getBulletChambers(stack).size();
    }

    public int getNumChambers(ItemStack stack) {
        return stack.getOrDefault(SapComponents.NUM_CHAMBERS.get(), 6);
    }

    private static int getCurrentChamber(ItemStack stack) {
        return stack.getOrDefault(SapComponents.CURRENT_CHAMBER.get(), -1);
    }

    private static void setCurrentChamber(ItemStack stack, int value) {
        stack.set(SapComponents.CURRENT_CHAMBER.get(), value);
    }

    private void spinCylinder(ItemStack stack, RandomSource random) {
        int chambers = getNumChambers(stack);
        setCurrentChamber(stack, random.nextInt(chambers));
    }

    private void advanceCylinder(ItemStack stack) {
        int chambers = getNumChambers(stack);
        int next = (getCurrentChamber(stack) + 1) % chambers;
        setCurrentChamber(stack, next);
    }

    private boolean rollCylinder(ItemStack stack, Player player) {
        if (player.isCrouching()) {
            spinCylinder(stack, player.getRandom());
            player.playSound(SoundEvents.CHAIN_PLACE);
            player.displayClientMessage(Component.literal("Spinning Cylinder..."), true);
            return true;
        }
        return false;
    }

    private void clampChambers(ItemStack stack) {
        int chambers = getNumChambers(stack);

        List<Integer> bullets = new ArrayList<>(getBulletChambers(stack));
        bullets.removeIf(b -> b >= chambers);
        stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);

        int current = getCurrentChamber(stack);
        if (current >= chambers) setCurrentChamber(stack, 0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        clampChambers(stack);

        List<Integer> bullets = getBulletChambers(stack);
        int current = getCurrentChamber(stack);

        if (bullets.isEmpty()) {
            player.displayClientMessage(Component.literal("No bullets in gun."), true);
            return InteractionResultHolder.success(stack);
        }

        if (rollCylinder(stack, player)) {
            return InteractionResultHolder.success(stack);
        }

        if (bullets.contains(current)) {
            if (!level.isClientSide()) {
                player.hurt(SapDamageTypes.causeRevolverSuicide(player.level().registryAccess()), 10000f);
            }
            player.displayClientMessage(Component.literal("BANG!"), true);
            player.playSound(SapSounds.REVOLVER_SHOOT.get());
            bullets.remove((Integer) current);
            stack.set(SapComponents.BULLET_CHAMBERS.get(), bullets);
        } else {
            player.displayClientMessage(Component.literal("It didn't shoot loser"), true);
            player.playSound(SoundEvents.ARROW_HIT_PLAYER);
        }

        advanceCylinder(stack);
        return InteractionResultHolder.success(stack);
    }
}
