package net.raccoon.will.pour_decisions.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.raccoon.will.pour_decisions.core.registry.PDComponents;

import javax.annotation.Nullable;

public class HomeRuneItem extends Item {
    protected final int maxCooldown;

    public HomeRuneItem(Properties properties, int maxCooldown) {
        super(properties);
        this.maxCooldown = maxCooldown;
    }

    private void saveRunePos(ItemStack stack, BlockPos pos) {
        stack.set(PDComponents.RUNE_POS.get(), pos);
    }

    @Nullable
    private BlockPos loadRunePos(ItemStack stack) {
        return stack.get(PDComponents.RUNE_POS.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && getCooldown(stack) <= 0) {
            if (player.isShiftKeyDown()) {
                saveRunePos(stack, player.blockPosition());
                level.playSound(null, player.blockPosition(), SoundEvents.CAT_HISS, SoundSource.PLAYERS, 1f, 1f);
                player.displayClientMessage(Component.literal("§aRune point set!"), true);
            } else {
                BlockPos pos = loadRunePos(stack);
                if (pos == null) {
                    player.displayClientMessage(Component.literal("§cNo rune set!"), true);
                    return InteractionResultHolder.fail(stack);
                }
                int max = 2400;
                setCooldown(stack, max);

                level.playSound(null, player.blockPosition(), SoundEvents.END_GATEWAY_SPAWN, SoundSource.PLAYERS, 1f, 1f);
                player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            int cd = getCooldown(stack);

            if (cd > 0) {
                setCooldown(stack, cd - 1);
            }
        }
    }

    public static int getCooldown(ItemStack stack) {
        return stack.getOrDefault(PDComponents.COOLDOWN.get(), 0);
    }

    public static void setCooldown(ItemStack stack, int value) {
        stack.set(PDComponents.COOLDOWN.get(), value);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCooldown(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int cd = getCooldown(stack);
        return Mth.clamp(Math.round((float)cd * 13.0F / (float)this.maxCooldown),0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x7A4736;
    }
}