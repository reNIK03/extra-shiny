package net.r_nik.extrashiny.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.r_nik.extrashiny.particle.AuroralDustColorTransitionOptions;
import org.joml.Vector3f;

public class AuroralDustColorTransitionParticle extends TextureSheetParticle {

    private final Vector3f from;
    private final Vector3f to;
    private final SpriteSet sprites;

    protected AuroralDustColorTransitionParticle(ClientLevel level,
                                                 double x, double y, double z,
                                                 double xd, double yd, double zd,
                                                 AuroralDustColorTransitionOptions options,
                                                 SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);

        this.sprites = sprites;
        this.from = new Vector3f(options.getFromColor());
        this.to   = new Vector3f(options.getToColor());

        this.xd = xd;
        this.yd = yd;
        this.zd = zd;


        this.lifetime = 5 + this.random.nextInt(5);
        this.quadSize = 0.2F * options.getScale();

        this.setSpriteFromAge(sprites);
        this.setColor(from.x, from.y, from.z);
        this.setAlpha(1.0F);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }


    @Override
    public void tick() {
        super.tick();
        if (this.removed) return;

        float t = Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F);

        this.setColor(
                Mth.lerp(t, from.x, to.x),
                Mth.lerp(t, from.y, to.y),
                Mth.lerp(t, from.z, to.z)
        );

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<AuroralDustColorTransitionOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(AuroralDustColorTransitionOptions options, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new AuroralDustColorTransitionParticle(level, x, y, z, xd, yd, zd, options, sprites);
        }
    }
}
