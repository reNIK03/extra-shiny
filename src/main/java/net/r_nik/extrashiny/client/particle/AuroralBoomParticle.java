package net.r_nik.extrashiny.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class AuroralBoomParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected AuroralBoomParticle(ClientLevel level,
                                  double x, double y, double z,
                                  double xd, double yd, double zd,
                                  SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.sprites = sprites;
        this.lifetime = 16;
        this.quadSize = 0.8F;
        this.setAlpha(1.0F);
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.removed) return;
        this.setAlpha(1.0F);
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new AuroralBoomParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}
