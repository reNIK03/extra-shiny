package net.r_nik.extrashiny.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
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
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;



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
                if (shooter != null && shooter.level() instanceof ServerLevel level) {
                    ItemStack safeStack = shot.stack().copy();
                    if (!RepeaterHelper.getProjectiles(safeStack).isEmpty()) {

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
