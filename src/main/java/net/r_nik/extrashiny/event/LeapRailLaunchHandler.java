package net.r_nik.extrashiny.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class LeapRailLaunchHandler {

    private static final double LIFT_Y = 0.28D;
    private static final double JUMP_Y = 0.65D;
    private static final double MAX_UP_Y = 0.85D;
    private static final int FORCE_TICKS = 4;

    private static final class LaunchData {
        int delayTicks;
        int forceTicks;
        final double vx;
        final double vz;

        LaunchData(int delayTicks, int forceTicks, double vx, double vz) {
            this.delayTicks = delayTicks;
            this.forceTicks = forceTicks;
            this.vx = vx;
            this.vz = vz;
        }
    }

    private static final Map<UUID, LaunchData> PENDING = new HashMap<>();

    private LeapRailLaunchHandler() {}

    public static void armLaunch(AbstractMinecart cart, double vx, double vz) {
        PENDING.put(cart.getUUID(), new LaunchData(1, FORCE_TICKS, vx, vz));
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;
        if (PENDING.isEmpty()) return;

        Iterator<Map.Entry<UUID, LaunchData>> it = PENDING.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, LaunchData> entry = it.next();
            LaunchData data = entry.getValue();

            Entity entity = serverLevel.getEntity(entry.getKey());
            if (!(entity instanceof AbstractMinecart cart)) {
                it.remove();
                continue;
            }

            if (data.delayTicks > 0) {
                data.delayTicks--;
                continue;
            }

            if (data.forceTicks <= 0) {
                it.remove();
                continue;
            }

            cart.setPos(cart.getX(), cart.getY() + LIFT_Y, cart.getZ());
            cart.setOnGround(false);

            Vec3 current = cart.getDeltaMovement();
            double newY = Math.min(MAX_UP_Y, Math.max(current.y, 0.0D) + JUMP_Y);

            cart.setDeltaMovement(data.vx, newY, data.vz);
            cart.resetFallDistance();
            cart.hasImpulse = true;

            cart.setHurtTime(0);
            cart.setDamage(0.0F);

            data.forceTicks--;
        }
    }
}