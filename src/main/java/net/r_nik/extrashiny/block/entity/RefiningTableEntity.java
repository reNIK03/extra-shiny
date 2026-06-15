package net.r_nik.extrashiny.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.screen.RefiningTableMenu;
import net.minecraft.sounds.SoundSource;
import net.r_nik.extrashiny.sound.ModSounds;


import javax.annotation.Nullable;
import java.util.*;

import static net.r_nik.extrashiny.block.entity.ModBlockEntities.REFINING_TABLE;

public class RefiningTableEntity extends BlockEntity implements MenuProvider {

    private static final String TAG_OVERCAP_LIST = "ExtraShinyOvercaps";

    private Set<Enchantment> getTableOvercaps(ItemStack stack) {
        Set<Enchantment> result = new HashSet<>();

        if (!stack.hasTag()) return result;
        var tag = stack.getTag();
        if (!tag.contains(TAG_OVERCAP_LIST)) return result;

        var list = tag.getList(TAG_OVERCAP_LIST, 8);
        for (int i = 0; i < list.size(); i++) {
            var id = list.getString(i);
            Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(id));
            if (ench != null) result.add(ench);
        }
        return result;
    }

    private void markTableOvercap(ItemStack stack, Enchantment ench) {
        var tag = stack.getOrCreateTag();
        var list = tag.contains(TAG_OVERCAP_LIST)
                ? tag.getList(TAG_OVERCAP_LIST, 8)
                : new ListTag();

        String id = ForgeRegistries.ENCHANTMENTS.getKey(ench).toString();

        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equals(id)) return;
        }

        list.add(StringTag.valueOf(id));
        tag.put(TAG_OVERCAP_LIST, list);
    }

    private boolean isForeignOvercap(Enchantment ench, int level, ItemStack stack) {
        if (level <= ench.getMaxLevel()) return false;
        return !getTableOvercaps(stack).contains(ench);
    }


    public static final int SLOT_LABRADORITE = 0;
    public static final int SLOT_LAPIS = 1;
    public static final int SLOT_GOLD = 2;
    public static final int SLOT_ITEM = 3;
    private static final int SLOT_COUNT = 4;

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case SLOT_LABRADORITE -> stack.is(ModItems.LABRADORITE.get());
                case SLOT_LAPIS -> stack.is(Items.LAPIS_LAZULI);
                case SLOT_GOLD -> stack.is(Items.GOLD_INGOT);
                case SLOT_ITEM -> isValidRefinable(stack);
                default -> false;
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public RefiningTableEntity(BlockPos pos, BlockState state) {
        super(REFINING_TABLE.get(), pos, state);
    }


    private boolean isValidRefinable(ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (stack.getItem() instanceof EnchantedBookItem) return false;

        return stack.isEnchanted() || stack.isEnchantable();
    }

    private static final String TAG_OVERCAP_USED = "ExtraShinyOvercapUsed";

    private boolean isOvercapUsed(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(TAG_OVERCAP_USED);
    }

    private void markOvercapUsed(ItemStack stack) {
        stack.getOrCreateTag().putBoolean(TAG_OVERCAP_USED, true);
    }

    public boolean canRefine() {
        ItemStack item = itemHandler.getStackInSlot(SLOT_ITEM);
        if (!isValidRefinable(item)) return false;


        if (!itemHandler.getStackInSlot(SLOT_GOLD).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LAPIS).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LABRADORITE).isEmpty()) return false;

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);
        if (enchants.isEmpty()) return false;

        boolean hasUpgradeableEnchant = false;

        for (var entry : enchants.entrySet()) {
            Enchantment ench = entry.getKey();
            int level = entry.getValue();

            if (ench.getMaxLevel() == 1) continue;

            if (level > ench.getMaxLevel()) continue;

            if (level < ench.getMaxLevel()) {
                hasUpgradeableEnchant = true;
            }
        }

        return hasUpgradeableEnchant;
    }


    public boolean canOvercapRefine() {
        ItemStack item = itemHandler.getStackInSlot(SLOT_ITEM);

        cleanupOvercapDataIfNeeded(item);

        if (!isValidRefinable(item)) return false;

        if (isOvercapUsed(item)) return false;

        if (itemHandler.getStackInSlot(SLOT_GOLD).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LAPIS).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LABRADORITE).isEmpty()) return false;

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);
        if (enchants.isEmpty()) return false;

        boolean hasValidCandidate = false;

        for (var entry : enchants.entrySet()) {
            Enchantment ench = entry.getKey();
            int level = entry.getValue();

            if (ench.getMaxLevel() == 1) continue;

            if (level < ench.getMaxLevel()) return false;

            if (!isForeignOvercap(ench, level, item)) {
                hasValidCandidate = true;
            }
        }

        return hasValidCandidate;
    }



    public LazyOptional<IItemHandler> getItemHandler() {
        return lazyItemHandler;
    }


    public void refineNormal(RandomSource random) {
        if (!canRefine()) return;
        doRefine(random, false);
    }

    public void refineOvercap(RandomSource random) {
        if (!canOvercapRefine()) return;
        doRefine(random, true);
    }

    private void doRefine(RandomSource random, boolean overcap) {
        ItemStack item = itemHandler.getStackInSlot(SLOT_ITEM);
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item);

        List<Enchantment> candidates = new ArrayList<>();

        for (var entry : enchants.entrySet()) {
            Enchantment ench = entry.getKey();
            int level = entry.getValue();

            if (ench.getMaxLevel() == 1) continue;

            if (!overcap && level < ench.getMaxLevel()) {
                candidates.add(ench);
            }

            if (overcap && level >= ench.getMaxLevel() && !isForeignOvercap(ench, level, item)) {
                candidates.add(ench);
            }
        }

        if (candidates.isEmpty()) return;

        Enchantment chosen = candidates.get(random.nextInt(candidates.size()));
        enchants.put(chosen, enchants.get(chosen) + 1);
        EnchantmentHelper.setEnchantments(enchants, item);

        consume(SLOT_LAPIS);
        consume(SLOT_LABRADORITE);

        if (overcap) {
            consume(SLOT_GOLD);
            markOvercapUsed(item);
            markTableOvercap(item, chosen);
        }

        if (level != null && !level.isClientSide) {
            level.playSound(
                    null,
                    worldPosition,
                    overcap
                            ? ModSounds.GOLDEN_REFINE.get()
                            : ModSounds.NORMAL_REFINE.get(),
                    SoundSource.BLOCKS,
                    1.0F,
                    0.9F + random.nextFloat() * 0.2F
            );
        }

        setChanged();
    }

    private void cleanupOvercapDataIfNeeded(ItemStack stack) {
        if (!stack.hasTag()) return;

        CompoundTag tag = stack.getTag();

        if (!tag.contains(TAG_OVERCAP_USED)) return;

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        Set<Enchantment> tableOvercaps = getTableOvercaps(stack);

        boolean stillHasValidOvercap = false;

        for (Enchantment ench : tableOvercaps) {
            int level = enchants.getOrDefault(ench, 0);
            if (level > ench.getMaxLevel()) {
                stillHasValidOvercap = true;
                break;
            }
        }

        if (!stillHasValidOvercap) {
            tag.remove(TAG_OVERCAP_USED);
            tag.remove(TAG_OVERCAP_LIST);

            if (tag.isEmpty()) {
                stack.setTag(null);
            }
        }
    }


    private void consume(int slot) {
        itemHandler.extractItem(slot, 1, false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("Inventory"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.extrashiny.refining_table");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RefiningTableMenu(id, inventory, this);
    }
}
