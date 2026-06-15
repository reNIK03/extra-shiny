package net.r_nik.extrashiny.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.block.entity.RefiningTableEntity;
import net.r_nik.extrashiny.item.ModItems;

public class RefiningTableMenu extends AbstractContainerMenu {

    private final RefiningTableEntity blockEntity;
    private final Level level;


    public RefiningTableEntity getBlockEntity() {
        return blockEntity;
    }

    public RefiningTableMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public RefiningTableMenu(int id, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.REFINING_TABLE_MENU.get(), id);

        this.blockEntity = (RefiningTableEntity) entity;
        this.level = inv.player.level();

        IItemHandler handler = blockEntity.getItemHandler()
                .orElseThrow(() -> new IllegalStateException("RefiningTable item handler missing"));

        this.addSlot(new SlotItemHandler(handler,
                RefiningTableEntity.SLOT_LABRADORITE,
                93, 51));

        this.addSlot(new SlotItemHandler(handler,
                RefiningTableEntity.SLOT_LAPIS,
                139, 51));

        this.addSlot(new SlotItemHandler(handler,
                RefiningTableEntity.SLOT_GOLD,
                116, 10));

        this.addSlot(new SlotItemHandler(handler,
                RefiningTableEntity.SLOT_ITEM,
                116, 36));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(
                        inv,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(
                    inv,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (player.level().isClientSide) return;

        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = this.getSlot(slot).getItem();

            if (!stack.isEmpty()) {
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }

                this.getSlot(slot).set(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (!slot.hasItem()) return empty;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < 4) {
            if (!this.moveItemStackTo(stack, 4, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            slot.setChanged();
        }

        else {
            boolean moved = false;

            if (stack.is(ModItems.LABRADORITE.get())) {
                moved = this.moveItemStackTo(stack,
                        RefiningTableEntity.SLOT_LABRADORITE,
                        RefiningTableEntity.SLOT_LABRADORITE + 1,
                        false);
            }

            else if (stack.is(Items.LAPIS_LAZULI)) {
                moved = this.moveItemStackTo(stack,
                        RefiningTableEntity.SLOT_LAPIS,
                        RefiningTableEntity.SLOT_LAPIS + 1,
                        false);
            }

            else if (stack.is(Items.GOLD_INGOT)) {
                moved = this.moveItemStackTo(stack,
                        RefiningTableEntity.SLOT_GOLD,
                        RefiningTableEntity.SLOT_GOLD + 1,
                        false);
            }

            else if (this.getSlot(RefiningTableEntity.SLOT_ITEM).mayPlace(stack)) {
                moved = this.moveItemStackTo(stack,
                        RefiningTableEntity.SLOT_ITEM,
                        RefiningTableEntity.SLOT_ITEM + 1,
                        false);
            }

            if (!moved) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, stack);
        return original;
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(
                ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player,
                ModBlocks.REFINING_TABLE.get()
        );
    }

    public boolean canNormalRefine() {
        if (!getSlot(RefiningTableEntity.SLOT_GOLD).getItem().isEmpty()) {
            return false;
        }

        return blockEntity.canRefine()
                && !canOvercapRefine();
    }

    public boolean canOvercapRefine() {
        return blockEntity.canOvercapRefine();
    }


    public void refine(boolean overcap) {
        if (level.isClientSide) return;

        if (overcap) {
            if (!blockEntity.canOvercapRefine()) return;
            blockEntity.refineOvercap(level.random);
        } else {
            if (!blockEntity.canRefine()) return;
            blockEntity.refineNormal(level.random);
        }
    }
}
