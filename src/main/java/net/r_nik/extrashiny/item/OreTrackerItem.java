package net.r_nik.extrashiny.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OreTrackerItem extends Item {

    public static final String STORED_ITEM = "StoredItem";

    public OreTrackerItem(Properties props) {
        super(props.stacksTo(1));
    }

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

            if (!OreTrackerUtil.hasStoredItem(tracker) && !offhand.isEmpty()) {

                ItemStack sample = offhand.copy();
                sample.setCount(1);

                storeItem(tracker, sample);
                offhand.shrink(1);

                player.displayClientMessage(
                        Component.literal("Stored " + sample.getHoverName().getString()),
                        true
                );

                return InteractionResultHolder.success(tracker);
            }


            if (OreTrackerUtil.hasStoredItem(tracker)) {
                ItemStack ejected = OreTrackerUtil.getStoredItem(tracker);

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

        if (OreTrackerUtil.hasStoredItem(tracker)) {
            int dist = tracker.getTag().getInt("NearestOreDist");

            if (dist >= 0) {
                player.displayClientMessage(
                        Component.literal(
                                "Nearest " +
                                        OreTrackerUtil.getStoredItem(tracker).getHoverName().getString() +
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
        return stack.hasTag() && stack.getTag().contains(STORED_ITEM);
    }

    private void storeItem(ItemStack tracker, ItemStack sample) {
        CompoundTag tag = tracker.getOrCreateTag();
        CompoundTag itemTag = new CompoundTag();
        sample.copy().save(itemTag);
        tag.put(STORED_ITEM, itemTag);
    }

    private ItemStack getStoredItem(ItemStack tracker) {
        return ItemStack.of(tracker.getTag().getCompound(STORED_ITEM));
    }

    private void clearStoredItem(ItemStack tracker) {
        CompoundTag tag = tracker.getTag();
        if (tag == null) return;

        tag.remove(STORED_ITEM);

        tag.putInt("NearestOreDist", -1);
        tag.putInt("ScanCooldown", 0);
    }


}
