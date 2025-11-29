package net.raccoon.will.sapientia.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class MuzzleFlashParticle extends TextureSheetParticle {

    protected MuzzleFlashParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.pickSprite(spriteSet);

        this.lifetime = 3;
        this.quadSize = 0.2f;
        this.gravity = 0;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) this.age / (float) this.lifetime);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

