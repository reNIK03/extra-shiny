package net.r_nik.extrashiny.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class OreTrackerItem extends Item {

    public static final String STORED_ITEM = "StoredItem";

    public OreTrackerItem(Properties props) {
        super(props.stacksTo(1));
    }

    // If you read this, I will make this item craftable in a future

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack tracker = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(tracker);
        }

        if (player.isShiftKeyDown()) {

            ItemStack offhand = player.getOffhandItem();

            if (!hasStoredItem(tracker) && !offhand.isEmpty()) {

                ItemStack sample = offhand.copy();
                sample.setCount(1);

                storeItem(tracker, sample, level);
                offhand.shrink(1);

                player.displayClientMessage(
                        Component.literal("Stored " + sample.getHoverName().getString()),
                        true
                );

                return InteractionResultHolder.success(tracker);
            }

            if (hasStoredItem(tracker)) {
                ItemStack ejected = getStoredItem(tracker, level);

                if (!player.addItem(ejected)) {
                    player.drop(ejected, false);
                }

                clearStoredItem(tracker);

                player.displayClientMessage(
                        Component.literal("Tracker cleared"),
                        true
                );
                return InteractionResultHolder.success(tracker);
            }

            return InteractionResultHolder.pass(tracker);
        }

        if (hasStoredItem(tracker)) {
            CustomData customData = tracker.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            int dist = customData.contains("NearestOreDist") ? customData.copyTag().getInt("NearestOreDist") : -1;

            if (dist >= 0) {
                player.displayClientMessage(
                        Component.literal(
                                "Nearest " +
                                        getStoredItem(tracker, level).getHoverName().getString() +
                                        " at " + dist + " blocks"
                        ),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("No ore detected nearby"),
                        true
                );
            }

            return InteractionResultHolder.success(tracker);
        }

        return InteractionResultHolder.pass(tracker);
    }


    private boolean hasStoredItem(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains(STORED_ITEM);
    }

    private void storeItem(ItemStack tracker, ItemStack sample, Level level) {
        CustomData customData = tracker.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        Tag itemTag = sample.saveOptional(level.registryAccess());
        tag.put(STORED_ITEM, itemTag);

        tracker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private ItemStack getStoredItem(ItemStack tracker, Level level) {
        CustomData customData = tracker.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (!customData.contains(STORED_ITEM)) return ItemStack.EMPTY;

        CompoundTag itemTag = customData.copyTag().getCompound(STORED_ITEM);
        return ItemStack.parseOptional(level.registryAccess(), itemTag);
    }

    private void clearStoredItem(ItemStack tracker) {
        CustomData customData = tracker.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.isEmpty()) return;

        CompoundTag tag = customData.copyTag();
        tag.remove(STORED_ITEM);
        tag.putInt("NearestOreDist", -1);
        tag.putInt("ScanCooldown", 0);

        if (tag.isEmpty()) {
            tracker.remove(DataComponents.CUSTOM_DATA);
        } else {
            tracker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }
}