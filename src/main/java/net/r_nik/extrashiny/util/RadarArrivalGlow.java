package net.r_nik.extrashiny.util;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class RadarArrivalGlow {

    private static final String TEAM_NAME = "extrashiny_teal_glow";
    private static final int GLOW_DURATION_TICKS = 10; // 0.5s

    private static final List<Entry> ARRIVAL_QUEUE = new LinkedList<>();
    private static final List<ActiveGlow> ACTIVE_GLOWS = new LinkedList<>();

    public static void queue(Entity target, int ticksUntilArrival) {
        if (!(target.level() instanceof ServerLevel)) return;

        String prevTeam = target.getTeam() != null ? target.getTeam().getName() : "";
        ARRIVAL_QUEUE.add(new Entry(target.level().dimension().location().toString(), target.getId(), ticksUntilArrival, prevTeam));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        Iterator<Entry> arrivalIt = ARRIVAL_QUEUE.iterator();
        while (arrivalIt.hasNext()) {
            Entry en = arrivalIt.next();
            en.ticks--;

            if (en.ticks > 0) continue;

            arrivalIt.remove();

            ServerLevel level = event.getServer().getLevel(
                    event.getServer().levelKeys().stream()
                            .filter(k -> k.location().toString().equals(en.dimKey))
                            .findFirst().orElse(null)
            );

            if (level == null) continue;

            Entity target = level.getEntity(en.entityId);
            if (target == null) continue;

            applyTealGlow(level, target);

            ACTIVE_GLOWS.add(new ActiveGlow(target, GLOW_DURATION_TICKS, en.prevTeam));
        }

        Iterator<ActiveGlow> activeIt = ACTIVE_GLOWS.iterator();
        while (activeIt.hasNext()) {
            ActiveGlow glow = activeIt.next();
            glow.ticksRemaining--;

            if (glow.ticksRemaining > 0) continue;

            activeIt.remove();

            Entity target = glow.target;
            if (target == null || target.isRemoved()) continue;

            target.setGlowingTag(false);

            if (target.level() instanceof ServerLevel level) {
                Scoreboard board = level.getScoreboard();
                board.removePlayerFromTeam(target.getScoreboardName());

                if (glow.prevTeam != null && !glow.prevTeam.isEmpty()) {
                    PlayerTeam old = board.getPlayerTeam(glow.prevTeam);
                    if (old != null) board.addPlayerToTeam(target.getScoreboardName(), old);
                }
            }
        }
    }

    private static void applyTealGlow(ServerLevel level, Entity target) {
        Scoreboard board = level.getScoreboard();
        PlayerTeam team = board.getPlayerTeam(TEAM_NAME);
        if (team == null) {
            team = board.addPlayerTeam(TEAM_NAME);
            team.setColor(ChatFormatting.AQUA);
        }

        board.addPlayerToTeam(target.getScoreboardName(), team);
        target.setGlowingTag(true);
    }

    private static class Entry {
        final String dimKey;
        final int entityId;
        int ticks;
        final String prevTeam;

        Entry(String dimKey, int entityId, int ticks, String prevTeam) {
            this.dimKey = dimKey;
            this.entityId = entityId;
            this.ticks = ticks;
            this.prevTeam = prevTeam;
        }
    }

    private static class ActiveGlow {
        final Entity target;
        int ticksRemaining;
        final String prevTeam;

        ActiveGlow(Entity target, int ticksRemaining, String prevTeam) {
            this.target = target;
            this.ticksRemaining = ticksRemaining;
            this.prevTeam = prevTeam;
        }
    }
}