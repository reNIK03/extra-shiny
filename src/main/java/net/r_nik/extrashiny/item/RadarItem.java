package net.r_nik.extrashiny.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class RadarItem extends Item {

    public RadarItem(Properties props) {
        super(props);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (level.isClientSide) return;

        if (entity instanceof Player player) {

            int r20 = 20;
            AABB box20 = new AABB(
                    player.getX() - r20, player.getY() - r20, player.getZ() - r20,
                    player.getX() + r20, player.getY() + r20, player.getZ() + r20
            );
            int count20 = level.getEntities(player, box20,
                    e -> e.getType().getCategory() == MobCategory.MONSTER).size();

            int r10 = 10;
            AABB box10 = new AABB(
                    player.getX() - r10, player.getY() - r10, player.getZ() - r10,
                    player.getX() + r10, player.getY() + r10, player.getZ() + r10
            );
            int count10 = level.getEntities(player, box10,
                    e -> e.getType().getCategory() == MobCategory.MONSTER).size();

            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = customData.copyTag();

            int oldCount20 = tag.contains("HostilesNearby20") ? tag.getInt("HostilesNearby20") : -1;
            int oldCount10 = tag.contains("HostilesNearby10") ? tag.getInt("HostilesNearby10") : -1;

            if (count20 != oldCount20 || count10 != oldCount10) {
                tag.putInt("HostilesNearby20", count20);
                tag.putInt("HostilesNearby10", count10);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            int count = customData.contains("HostilesNearby20") ? customData.copyTag().getInt("HostilesNearby20") : 0;

            player.displayClientMessage(
                    Component.literal("Hostile mobs nearby: " + count),
                    true
            );
        }

        return InteractionResultHolder.success(stack);
    }
}