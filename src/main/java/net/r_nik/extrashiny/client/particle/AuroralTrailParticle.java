package net.r_nik.extrashiny.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class AuroralTrailParticle extends TextureSheetParticle {

    private static final Vector3f FROM = new Vector3f(199f/255f, 235f/255f, 136f/255f);
    private static final Vector3f TO   = new Vector3f(199f/255f, 112f/255f, 199f/255f);
    private final float startSize;
    private final float endSize;
    private final float maxAlpha;
    private final SpriteSet sprites;

    protected AuroralTrailParticle(ClientLevel level, double x, double y, double z,
                                   double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.lifetime = 20;

        this.startSize = 0.18F + this.random.nextFloat() * 0.05F;
        this.endSize   = this.startSize * (1.35F + this.random.nextFloat() * 0.25F);
        this.quadSize  = startSize;

        this.maxAlpha = 0.35F;
        this.xd *= 0.03;
        this.yd *= 0.03;
        this.zd *= 0.03;
        this.setSpriteFromAge(this.sprites);
        this.setAlpha(maxAlpha);
        this.setColor(FROM.x, FROM.y, FROM.z);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.removed) return;

        float t = Mth.clamp((float) this.age / (float) this.lifetime, 0f, 1f);
        this.setColor(
                Mth.lerp(t, FROM.x, TO.x),
                Mth.lerp(t, FROM.y, TO.y),
                Mth.lerp(t, FROM.z, TO.z)
        );
        this.quadSize = Mth.lerp(t, startSize, endSize);
        float a;
        if (t < 0.10f) {
            a = maxAlpha * (t / 0.10f);
        } else if (t > 0.85f) {
            a = maxAlpha * ((1.0f - t) / 0.15f);
        } else {
            a = maxAlpha;
        }
        this.setAlpha(Mth.clamp(a, 0f, maxAlpha));
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            if (this.sprites == null) return null;
            return new AuroralTrailParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}
