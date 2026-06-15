package net.r_nik.extrashiny.core.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.r_nik.extrashiny.entity.AuroralArrowEntity;
import net.r_nik.extrashiny.particle.AuroralDustColorTransitionOptions;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    private static final Vector3f FROM = new Vector3f(199f / 255f, 235f / 255f, 136f / 255f);
    private static final Vector3f TO   = new Vector3f(199f / 255f, 112f / 255f, 199f / 255f);
    private static final float SCALE = 1.0f;

    @ModifyArg(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
                    ordinal = 0
            ),
            index = 0
    )
    private ParticleOptions extrashiny$replaceCritParticle(ParticleOptions original) {
        if (original != ParticleTypes.CRIT) return original;

        if (((Object) this) instanceof AuroralArrowEntity) {
            return new AuroralDustColorTransitionOptions(FROM, TO, SCALE);
        }

        return original;
    }
}
