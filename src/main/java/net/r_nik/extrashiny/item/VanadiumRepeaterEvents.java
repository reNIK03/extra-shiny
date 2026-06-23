package net.r_nik.extrashiny.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.r_nik.extrashiny.ExtraShiny;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class VanadiumRepeaterEvents {

    private static final Set<Entity> REPEATER_PROJECTILES = Collections.newSetFromMap(new WeakHashMap<>());
    private static Field piercedEntitiesField;

    static {
        try {
            piercedEntitiesField = AbstractArrow.class.getDeclaredField("piercedEntities");
            piercedEntitiesField.setAccessible(true);
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public static void onArrowSpawn(EntityJoinLevelEvent event) {
        Entity e = event.getEntity();
        LivingEntity shooter = null;

        if (e instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof LivingEntity owner)
                shooter = owner;
        } else if (e instanceof FireworkRocketEntity rocket) {
            if (rocket.getOwner() instanceof LivingEntity owner)
                shooter = owner;
        }

        if (shooter == null) return;

        if (shooter.getMainHandItem().getItem() instanceof VanadiumRepeaterItem ||
                shooter.getOffhandItem().getItem() instanceof VanadiumRepeaterItem) {

            REPEATER_PROJECTILES.add(e);
            e.invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {

        Entity src = event.getContainer().getSource().getDirectEntity();

        if (src == null || !REPEATER_PROJECTILES.contains(src)) {
            return;
        }

        LivingEntity target = event.getEntity();

        target.invulnerableTime = 0;
        target.hurtTime = 0;
        target.hurtDuration = 0;

        if (piercedEntitiesField != null) {
            try {
                Set<?> pierced = (Set<?>) piercedEntitiesField.get(src);
                if (pierced != null) {
                    pierced.clear();
                }
            } catch (Exception ignored) {}
        }
    }
}