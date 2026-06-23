package net.r_nik.extrashiny.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class DelayedShotScheduler {

    private record ScheduledShot(
            LivingEntity shooter,
            ItemStack stack,
            float velocity,
            float inaccuracy,
            float angleOffset,
            int ticksLeft
    ) {}

    private static final List<ScheduledShot> queue = new ArrayList<>();

    public static void schedule(
            LivingEntity shooter,
            ItemStack crossbow,
            float velocity,
            float inaccuracy,
            float angleOffset,
            int delayTicks
    ) {
        queue.add(new ScheduledShot(
                shooter,
                crossbow.copy(),
                velocity,
                inaccuracy,
                angleOffset,
                Math.max(1, delayTicks)
        ));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (queue.isEmpty()) return; // Quick optimization

        List<ScheduledShot> remaining = new ArrayList<>();
        Iterator<ScheduledShot> it = queue.iterator();

        while (it.hasNext()) {
            ScheduledShot shot = it.next();
            int left = shot.ticksLeft() - 1;

            if (left > 0) {
                remaining.add(new ScheduledShot(
                        shot.shooter(),
                        shot.stack(),
                        shot.velocity(),
                        shot.inaccuracy(),
                        shot.angleOffset(),
                        left
                ));
            } else {
                LivingEntity shooter = shot.shooter();
                if (shooter != null && shooter.isAlive() && shooter.level() instanceof ServerLevel level) {
                    ItemStack safeStack = shot.stack().copy();

                    if (VanadiumRepeaterItem.isCharged(safeStack)) {

                        RepeaterHelper.fireSingleVolley(
                                level,
                                shooter,
                                safeStack,
                                shot.velocity(),
                                shot.inaccuracy(),
                                shot.angleOffset()
                        );
                    }
                }
            }
        }

        queue.clear();
        queue.addAll(remaining);
    }
}