package net.r_nik.extrashiny.item;

import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.r_nik.extrashiny.util.RadarArrivalGlow;

import java.util.Comparator;
import java.util.List;

public class RecalibratedRadarItem extends Item {

    private static final int R20 = 20;
    private static final int R10 = 10;
    private static final int MAX_SIGNALS = 10;
    private static final int COOLDOWN_TICKS = 20; // 1 second

    public RecalibratedRadarItem(Properties props) {
        super(props);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        int count20 = countHostiles(level, player, R20);
        int count10 = countHostiles(level, player, R10);

        stack.getOrCreateTag().putInt("HostilesNearby20", count20);
        stack.getOrCreateTag().putInt("HostilesNearby10", count10);

        boolean inHotbar = slot >= 0 && slot < 9;
        boolean inOffhand = player.getOffhandItem() == stack;
        boolean inMainhand = player.getMainHandItem() == stack;

        if (inMainhand || inOffhand || isSelected) {
            player.displayClientMessage(
                    Component.literal("Hostile mobs nearby: " + count20),
                    true // action bar
            );
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            int count20 = stack.getOrCreateTag().getInt("HostilesNearby20");

            if (count20 <= 0) {
                return InteractionResultHolder.fail(stack);
            }

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            List<Entity> targets = getNearestHostiles(level, player, R20, MAX_SIGNALS);
            if (!targets.isEmpty() && level instanceof ServerLevel serverLevel) {
                spawnVibrationBursts(serverLevel, player, targets);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.SCULK_CLICKING, SoundSource.PLAYERS, 0.6f, 1.0f);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static int countHostiles(Level level, Player player, int radius) {
        AABB box = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        return level.getEntities(player, box, e -> e.getType().getCategory() == MobCategory.MONSTER).size();
    }

    private static List<Entity> getNearestHostiles(Level level, Player player, int radius, int limit) {
        AABB box = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        return level.getEntities(player, box, e -> e.getType().getCategory() == MobCategory.MONSTER)
                .stream()
                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(player)))
                .limit(limit)
                .toList();
    }

    private static void spawnVibrationBursts(ServerLevel level, Player player, List<Entity> targets) {
        Vec3 start = player.position().add(0, player.getEyeHeight(), 0);

        for (Entity target : targets) {
            Vec3 end = target.position().add(0, target.getBbHeight() * 0.5, 0);

            double dist = player.distanceTo(target);

            int travelTime = Math.max(20, (int)(dist * 1.0));

            VibrationParticleOption opt = new VibrationParticleOption(
                    new EntityPositionSource(target, target.getBbHeight() * 0.5F),
                    travelTime
            );

            level.sendParticles(
                    opt,
                    start.x, start.y, start.z,
                    1,
                    0, 0, 0,
                    0
            );

            RadarArrivalGlow.queue(target, travelTime);

        }
    }
}
