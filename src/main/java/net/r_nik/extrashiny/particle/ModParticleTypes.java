package net.r_nik.extrashiny.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ExtraShiny.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<AuroralDustColorTransitionOptions>> AURORAL_DUST =
            PARTICLE_TYPES.register("auroral_dust", AuroralDustColorTransitionType::new);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AURORAL_TRAIL =
            PARTICLE_TYPES.register("auroral_trail", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AURORAL_BOOM =
            PARTICLE_TYPES.register("auroral_boom", () -> new SimpleParticleType(false));
}