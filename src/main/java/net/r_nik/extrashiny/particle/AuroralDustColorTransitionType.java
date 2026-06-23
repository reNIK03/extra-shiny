package net.r_nik.extrashiny.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AuroralDustColorTransitionType extends ParticleType<AuroralDustColorTransitionOptions> {

    public AuroralDustColorTransitionType() {
        super(true);
    }

    @Override
    public MapCodec<AuroralDustColorTransitionOptions> codec() {
        return AuroralDustColorTransitionOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, AuroralDustColorTransitionOptions> streamCodec() {
        return AuroralDustColorTransitionOptions.STREAM_CODEC;
    }
}