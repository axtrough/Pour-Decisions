package net.raccoon.will.pour_decisions.core.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raccoon.will.pour_decisions.core.PourDecisions;

import java.util.function.Supplier;

public class PDParticles {
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, PourDecisions.MODID);

    public static Supplier<SimpleParticleType> MUZZLEFLASH_PARTICLE =
            PARTICLE_TYPES.register("muzzleflash_particle", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
