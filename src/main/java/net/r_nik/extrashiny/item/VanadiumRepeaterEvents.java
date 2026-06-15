package net.r_nik.extrashiny.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Set;

@Mod.EventBusSubscriber
public class VanadiumRepeaterEvents {

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
        }
        else if (e instanceof FireworkRocketEntity rocket) {
            if (rocket.getOwner() instanceof LivingEntity owner)
                shooter = owner;
        }

        if (shooter == null) return;

        if (shooter.getMainHandItem().getItem() instanceof VanadiumRepeaterItem ||
                shooter.getOffhandItem().getItem() instanceof VanadiumRepeaterItem) {

            e.getPersistentData().putBoolean("VanadiumRepeater_NoIFrames", true);
            e.getPersistentData().putBoolean("VanadiumRepeater_OverridePiercing", true);

            e.invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {



        Entity src = event.getSource().getDirectEntity();
        if (src == null) return;

        boolean isRepeaterProjectile =
                src.getPersistentData().getBoolean("VanadiumRepeater_NoIFrames");

        if (!isRepeaterProjectile)
            return;



        if (!src.getPersistentData().getBoolean("VanadiumRepeater_NoIFrames"))
            return;

        LivingEntity target = event.getEntity();

        target.invulnerableTime = 0;
        target.hurtTime = 0;
        target.hurtDuration = 0;

        if (src.getPersistentData().getBoolean("VanadiumRepeater_OverridePiercing")) {
            try {
                Set<?> pierced = (Set<?>) piercedEntitiesField.get(src);
                pierced.clear();
            } catch (Exception ignored) {}
        }
    }
}
