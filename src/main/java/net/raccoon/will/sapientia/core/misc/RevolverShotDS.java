package net.raccoon.will.sapientia.core.misc;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class RevolverShotDS extends DamageSource {

    public RevolverShotDS(Holder<DamageType> type) {
        super(type);
        this.is(DamageTypeTags.NO_KNOCKBACK);
        this.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity dead) {
        LivingEntity attacker = dead.getKillCredit();
        return Component.literal(dead.getName().getString() + " was shot by" + attacker.getName().getString());
    }
}
