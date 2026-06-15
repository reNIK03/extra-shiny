package net.r_nik.extrashiny.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.effect.ModEffects;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class WisdomXpEvents {


    @SubscribeEvent
    public static void onMobXpDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null) return;

        if (!player.hasEffect(ModEffects.WISDOM.get())) return;

        int amplifier = player.getEffect(ModEffects.WISDOM.get()).getAmplifier();
        float multiplier = 1.0f + (amplifier + 1) * 0.2f;

        int originalXp = event.getDroppedExperience();
        event.setDroppedExperience(Math.round(originalXp * multiplier));
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;

        Player player = event.getPlayer();
        if (player == null) return;

        if (!player.hasEffect(ModEffects.WISDOM.get())) return;

        BlockState state = event.getState();

        int xp = state.getExpDrop(
                event.getLevel(),
                event.getLevel().getRandom(),
                event.getPos(),
                0,
                0
        );

        if (xp <= 0) return;

        int amplifier = player.getEffect(ModEffects.WISDOM.get()).getAmplifier();
        float multiplier = 1.0f + (amplifier + 1) * 0.2f;

        int bonusXp = Math.round(xp * (multiplier - 1.0f));

        if (bonusXp > 0) {
            ExperienceOrb.award(
                    (ServerLevel) event.getLevel(),
                    Vec3.atCenterOf(event.getPos()),
                    bonusXp
            );
        }
    }
}
