package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.effect.ModEffects;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class PhantasmEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {

            if (attacker.hasEffect(ModEffects.PHANTASM.get())) {
                int amplifier = attacker.getEffect(ModEffects.PHANTASM.get()).getAmplifier();

                double radius = 5.0 + (amplifier * 2.0);

                AABB box = attacker.getBoundingBox().inflate(radius);
                long entityCount = attacker.level().getEntitiesOfClass(
                        LivingEntity.class,
                        box,
                        e -> e != attacker && e.distanceTo(attacker) <= radius
                ).size();

                if (entityCount > 0) {
                    float multiplier = 1.0f + (entityCount * 0.05f);
                    event.setAmount(event.getAmount() * multiplier);
                }
            }
        }
    }
}