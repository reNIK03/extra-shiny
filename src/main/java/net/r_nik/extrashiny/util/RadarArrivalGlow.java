package net.r_nik.extrashiny.util;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber
public class RadarArrivalGlow {

    private static final String TEAM_NAME = "extrashiny_teal_glow";
    private static final int GLOW_DURATION_TICKS = 10; // 0.5s

    private static final List<Entry> QUEUE = new LinkedList<>();

    public static void queue(Entity target, int ticksUntilArrival) {
        if (!(target.level() instanceof ServerLevel)) return;

        String prevTeam = target.getTeam() != null ? target.getTeam().getName() : "";
        QUEUE.add(new Entry(target.level().dimension().location().toString(), target.getId(), ticksUntilArrival, prevTeam));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Iterator<Entry> it = QUEUE.iterator();
        while (it.hasNext()) {
            Entry en = it.next();
            en.ticks--;

            if (en.ticks > 0) continue;

            ServerLevel level = e.getServer().getLevel(e.getServer().levelKeys().stream()
                    .filter(k -> k.location().toString().equals(en.dimKey))
                    .findFirst().orElse(null));

            if (level == null) { it.remove(); continue; }

            Entity target = level.getEntity(en.entityId);
            if (target == null) { it.remove(); continue; }

            applyTealGlow(level, target, en.prevTeam);
            it.remove();
        }
    }

    private static void applyTealGlow(ServerLevel level, Entity target, String prevTeam) {
        Scoreboard board = level.getScoreboard();
        PlayerTeam team = board.getPlayerTeam(TEAM_NAME);
        if (team == null) {
            team = board.addPlayerTeam(TEAM_NAME);
            team.setColor(ChatFormatting.AQUA);
        }

        target.getPersistentData().putString("extrashiny_prev_team", prevTeam);
        target.getPersistentData().putInt("extrashiny_glow_ticks", GLOW_DURATION_TICKS);

        board.addPlayerToTeam(target.getScoreboardName(), team);

        target.setGlowingTag(true);
    }

    @Mod.EventBusSubscriber
    public static class RestoreHandler {
        @SubscribeEvent
        public static void onServerEntityTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent e) {
            if (e.getEntity().level().isClientSide) return;

            var ent = e.getEntity();
            var tag = ent.getPersistentData();
            if (!tag.contains("extrashiny_glow_ticks")) return;

            int t = tag.getInt("extrashiny_glow_ticks") - 1;
            if (t > 0) {
                tag.putInt("extrashiny_glow_ticks", t);
                return;
            }

            tag.remove("extrashiny_glow_ticks");
            ent.setGlowingTag(false);

            String prev = tag.getString("extrashiny_prev_team");
            tag.remove("extrashiny_prev_team");

            Scoreboard board = ent.level().getScoreboard();
            board.removePlayerFromTeam(ent.getScoreboardName());

            if (!prev.isEmpty()) {
                PlayerTeam old = board.getPlayerTeam(prev);
                if (old != null) board.addPlayerToTeam(ent.getScoreboardName(), old);
            }
        }
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
}
