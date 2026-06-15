package net.r_nik.extrashiny.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ExtraShiny.MOD_ID);

    public static final RegistryObject<ParticleType<AuroralDustColorTransitionOptions>> AURORAL_DUST =
            PARTICLE_TYPES.register("auroral_dust", AuroralDustColorTransitionType::new);

    public static final RegistryObject<SimpleParticleType> AURORAL_TRAIL =
            PARTICLE_TYPES.register("auroral_trail", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> AURORAL_BOOM =
            PARTICLE_TYPES.register("auroral_boom", () -> new SimpleParticleType(false));
}
