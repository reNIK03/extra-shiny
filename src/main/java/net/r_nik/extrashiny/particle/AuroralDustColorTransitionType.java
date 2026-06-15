package net.r_nik.extrashiny.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleType;

public class AuroralDustColorTransitionType extends ParticleType<AuroralDustColorTransitionOptions> {

    public AuroralDustColorTransitionType() {
        super(true, AuroralDustColorTransitionOptions.DESERIALIZER);
    }


    @Override
    public Codec<AuroralDustColorTransitionOptions> codec() {
        return AuroralDustColorTransitionOptions.CODEC;
    }
}
