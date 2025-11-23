package net.raccoon.will.sapientia.core.misc;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class RevolverSuicideDS extends DamageSource {

    public RevolverSuicideDS(Holder<DamageType> type) {
        super(type);
        this.is(DamageTypeTags.NO_KNOCKBACK);
        this.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity dead) {
        return Component.literal(dead.getName().getString() + " shot himself");
    }
}
