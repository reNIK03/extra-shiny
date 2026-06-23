package net.r_nik.extrashiny.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.item.OreTrackerItem;
import net.r_nik.extrashiny.item.OreTrackerUtil;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.ai.TargetVanadiumGolemGoal;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class ForgeBusEvents {


    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        for (int i = 0; i < 9; i++) {
            scanTracker(player, player.getInventory().getItem(i));
        }

        scanTracker(player, player.getOffhandItem());
    }

    private static void scanTracker(Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof OreTrackerItem)) return;

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains("StoredItem")) return;

        int cooldown = tag.getInt("ScanCooldown");
        if (cooldown > 0) {
            int finalCooldown = cooldown;
            stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY,
                    data -> data.update(t -> t.putInt("ScanCooldown", finalCooldown - 1)));
            return;
        }

        int dist = OreTrackerUtil.findNearestOreDistance(
                player.level(),
                player.blockPosition(),
                stack
        );

        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> data.update(t -> {
            t.putInt("NearestOreDist", dist);
            t.putInt("ScanCooldown", 10);
        }));
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
            CompoundTag tag = left.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

            if (tag.contains("MemoryDurability") && tag.getInt("MemoryDurability") > 0) {
                return;
            }

            ItemStack output = left.copy();

            int extraDura = (int) (left.getMaxDamage() * 0.25f);
            output.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> data.update(t -> {
                t.putInt("MemoryDurability", extraDura);
                t.putInt("MaxMemoryDurability", extraDura);
            }));

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
    }

}