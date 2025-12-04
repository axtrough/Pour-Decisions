package net.raccoon.will.pour_decisions.common.item;

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
import net.raccoon.will.pour_decisions.core.registry.PDComponents;

public class BeverageItem extends Item {
    private static final int DRINK_DURATION = 30;
    private final String fluid;
    private final boolean isAlcoholic;
    private final int fluidUses;

    public BeverageItem(Properties properties, String fluid, boolean isAlcoholic, int fluidUses) {
        super(properties);
        this.fluid = fluid;
        this.isAlcoholic = isAlcoholic;
        this.fluidUses = fluidUses;
    }

    public String getFluid() {
        return fluid;
    }

    public int getFluidUses() {
        return fluidUses;
    }

    public boolean containsAlcohol() {
        return isAlcoholic;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Integer uses = stack.get(PDComponents.BEVERAGE_USES.get());
        if (uses == null) {
            uses = fluidUses;
            stack.set(PDComponents.BEVERAGE_USES.get(), uses);
        }

        uses--;
        if (uses > 0) {
            stack.set(PDComponents.BEVERAGE_USES.get(), uses);
        } else {
            stack = ItemStack.EMPTY;
        }

        if (entityLiving instanceof ServerPlayer serverplayer) {
            if (containsAlcohol()) {
                serverplayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0, false, false));
            }
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            entityLiving.removeEffectsCuredBy(EffectCures.HONEY);
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }

        return stack;
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        Integer uses = stack.get(PDComponents.BEVERAGE_USES.get());
        if (uses == null) return 0;

        return (int) (13.0F * (uses / (float) fluidUses));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x5c0000;
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
