package net.raccoon.will.pour_decisions.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class MuzzleFlashProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public MuzzleFlashProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }


    @Override
    public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level,
                                             double x, double y, double z, double vx, double vy, double vz) {
        return new MuzzleFlashParticle(level, x, y, z, spriteSet);
    }
}
