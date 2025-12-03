package net.raccoon.will.sapientia.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class BeverageItem extends Item {
    private static final int DRINK_DURATION = 30;
    private final String fluid;
    private final boolean isAlcoholic;

    public BeverageItem(Properties properties, String fluid, boolean isAlcoholic) {
        super(properties);
        this.fluid = fluid;
        this.isAlcoholic = isAlcoholic;
    }

    public String getFluid() {
        return fluid;
    }

    public boolean containsAlcohol() {
        return isAlcoholic;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        super.finishUsingItem(stack, level, entityLiving);
        if (entityLiving instanceof ServerPlayer serverplayer) {
            serverplayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0, false, false));
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            entityLiving.removeEffectsCuredBy(EffectCures.HONEY);
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);

        } else {
            if (entityLiving instanceof Player) {
                Player player = (Player)entityLiving;
                if (!player.hasInfiniteMaterials()) {
                    ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                    if (!player.getInventory().add(itemstack)) {
                        player.drop(itemstack, false);
                    }
                }
            }

            return stack;
        }
    }

    public boolean getsYouDrunk() {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return super.getDrinkingSound();
    }

    @Override
    public SoundEvent getEatingSound() {
        return super.getEatingSound();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
