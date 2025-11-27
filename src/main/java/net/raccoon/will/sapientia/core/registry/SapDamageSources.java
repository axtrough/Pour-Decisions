package net.raccoon.will.sapientia.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SapDamageSources {
    public static final ResourceKey<DamageType> REVOLVER =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("sapientia", "revolver"));

    public static DamageSource revolver(Level level, @Nullable Entity shooter) {
        Holder<DamageType> typeHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(REVOLVER);

        return new RevolverDamageSource(typeHolder, shooter);
    }


    public static class RevolverDamageSource extends DamageSource {
        public RevolverDamageSource(Holder<DamageType> type, @Nullable Entity shooter) {
            super(type, shooter);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(LivingEntity victim) {
            LivingEntity killer = victim.getKillCredit();
            String base = "death.attack.sapientia.revolver"; // hardcoded for safety
            String playerKey = base + ".player";
            String selfKey = base + ".self";

            // suicide
            if (killer != null && killer == victim) {
                return Component.translatable(selfKey, victim.getDisplayName());
            }

            // killed
            if (killer != null) {
                return Component.translatable(playerKey, victim.getDisplayName(), killer.getDisplayName());
            }

            //generic fallback
            return Component.translatable(base, victim.getDisplayName());
        }
    }
}
