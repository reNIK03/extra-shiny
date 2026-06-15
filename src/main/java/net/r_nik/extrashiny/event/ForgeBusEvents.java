package net.r_nik.extrashiny.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.item.OreTrackerItem;
import net.r_nik.extrashiny.item.OreTrackerUtil;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.ai.TargetVanadiumGolemGoal;

@Mod.EventBusSubscriber(
        modid = ExtraShiny.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)



public class ForgeBusEvents {


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide) return;

        Player player = event.player;

        for (int i = 0; i < 9; i++) {
            scanTracker(player, player.getInventory().getItem(i));
        }

        scanTracker(player, player.getOffhandItem());
    }

    private static void scanTracker(Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof OreTrackerItem)) return;
        if (!stack.hasTag() || !stack.getTag().contains("StoredItem")) return;

        CompoundTag tag = stack.getOrCreateTag();

        int cooldown = tag.getInt("ScanCooldown");
        if (cooldown > 0) {
            tag.putInt("ScanCooldown", cooldown - 1);
            return;
        }

        int dist = OreTrackerUtil.findNearestOreDistance(
                player.level(),
                player.blockPosition(),
                stack
        );

        tag.putInt("NearestOreDist", dist);
        tag.putInt("ScanCooldown", 10);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Monster monster)) return;
        if (monster.level().isClientSide) return;

        boolean alreadyHasGoal = monster.targetSelector.getAvailableGoals()
                .stream()
                .anyMatch(wrapped ->
                        wrapped.getGoal() instanceof TargetVanadiumGolemGoal
                );

        if (alreadyHasGoal) return;

        monster.targetSelector.addGoal(
                3,
                new TargetVanadiumGolemGoal(monster)
        );

    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.isEmpty() || right.isEmpty() || !left.isDamageableItem()) return;

        if (right.is(ModItems.MEMORY_ALLOY.get())) {
            CompoundTag tag = left.getOrCreateTag();

            if (tag.contains("MemoryDurability") && tag.getInt("MemoryDurability") > 0) {
                return;
            }

            ItemStack output = left.copy();
            CompoundTag outputTag = output.getOrCreateTag();

            int extraDura = (int) (left.getMaxDamage() * 0.25f);
            outputTag.putInt("MemoryDurability", extraDura);
            outputTag.putInt("MaxMemoryDurability", extraDura);

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
    }

}
