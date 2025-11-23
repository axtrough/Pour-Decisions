package net.raccoon.will.sapientia.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.raccoon.will.sapientia.core.Sapientia;
import net.raccoon.will.sapientia.core.misc.RevolverSuicideDS;

public class SapDamageTypes {
    public static final ResourceKey<DamageType> REVOLVER =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Sapientia.MODID, "revolver"));


    public static DamageSource causeRevolverSuicide(RegistryAccess registryAccess) {
        Holder<DamageType> holder = registryAccess
                .registry(Registries.DAMAGE_TYPE)
                .get()
                .getHolderOrThrow(REVOLVER);

        return new RevolverSuicideDS(holder);
    }
}
