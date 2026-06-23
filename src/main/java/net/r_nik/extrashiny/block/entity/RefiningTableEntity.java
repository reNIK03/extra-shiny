package net.r_nik.extrashiny.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.screen.RefiningTableMenu;
import net.r_nik.extrashiny.sound.ModSounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.r_nik.extrashiny.block.entity.ModBlockEntities.REFINING_TABLE;

public class RefiningTableEntity extends BlockEntity implements MenuProvider {

    private static final String TAG_OVERCAP_LIST = "ExtraShinyOvercaps";
    private static final String TAG_OVERCAP_USED = "ExtraShinyOvercapUsed";

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

    public RefiningTableEntity(BlockPos pos, BlockState state) {
        super(REFINING_TABLE.get(), pos, state);
    }

    private Set<Holder<Enchantment>> getTableOvercaps(ItemStack stack) {
        Set<Holder<Enchantment>> result = new HashSet<>();
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        if (customData.isEmpty()) return result;

        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_OVERCAP_LIST)) return result;
        if (level == null) return result;

        Registry<Enchantment> enchRegistry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var list = tag.getList(TAG_OVERCAP_LIST, 8);

        for (int i = 0; i < list.size(); i++) {
            var id = list.getString(i);
            var key = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(id));
            enchRegistry.getHolder(key).ifPresent(result::add);
        }
        return result;
    }

    private void markTableOvercap(ItemStack stack, Holder<Enchantment> ench) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        var list = tag.contains(TAG_OVERCAP_LIST)
                ? tag.getList(TAG_OVERCAP_LIST, 8)
                : new ListTag();

        String id = ench.unwrapKey().map(k -> k.location().toString()).orElse("");
        if (id.isEmpty()) return;

        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equals(id)) return;
        }

        list.add(StringTag.valueOf(id));
        tag.put(TAG_OVERCAP_LIST, list);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private boolean isOvercapUsed(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains(TAG_OVERCAP_USED) && customData.copyTag().getBoolean(TAG_OVERCAP_USED);
    }

    private void markOvercapUsed(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        tag.putBoolean(TAG_OVERCAP_USED, true);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private void cleanupOvercapDataIfNeeded(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.isEmpty()) return;

        CompoundTag tag = customData.copyTag();
        if (!tag.contains(TAG_OVERCAP_USED)) return;

        var enchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        Set<Holder<Enchantment>> tableOvercaps = getTableOvercaps(stack);

        boolean stillHasValidOvercap = false;
        for (Holder<Enchantment> ench : tableOvercaps) {
            int lvl = enchants.getLevel(ench);
            if (lvl > ench.value().getMaxLevel()) {
                stillHasValidOvercap = true;
                break;
            }
        }

        if (!stillHasValidOvercap) {
            tag.remove(TAG_OVERCAP_USED);
            tag.remove(TAG_OVERCAP_LIST);
            if (tag.isEmpty()) {
                stack.remove(DataComponents.CUSTOM_DATA);
            } else {
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }

    private boolean isForeignOvercap(Holder<Enchantment> ench, int level, ItemStack stack) {
        if (level <= ench.value().getMaxLevel()) return false;
        return !getTableOvercaps(stack).contains(ench);
    }

    private boolean isValidRefinable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() instanceof EnchantedBookItem) return false;
        return stack.isEnchanted() || stack.isEnchantable();
    }

    public boolean canRefine() {
        ItemStack item = itemHandler.getStackInSlot(SLOT_ITEM);
        if (!isValidRefinable(item)) return false;
        if (!itemHandler.getStackInSlot(SLOT_GOLD).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LAPIS).isEmpty()) return false;
        if (itemHandler.getStackInSlot(SLOT_LABRADORITE).isEmpty()) return false;

        var enchants = item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchants.isEmpty()) return false;

        boolean hasUpgradeableEnchant = false;
        for (var entry : enchants.entrySet()) {
            Holder<Enchantment> ench = entry.getKey();
            int level = entry.getIntValue();
            if (ench.value().getMaxLevel() == 1) continue;
            if (level > ench.value().getMaxLevel()) continue;
            if (level < ench.value().getMaxLevel()) {
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

        var enchants = item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchants.isEmpty()) return false;

        boolean hasValidCandidate = false;
        for (var entry : enchants.entrySet()) {
            Holder<Enchantment> ench = entry.getKey();
            int level = entry.getIntValue();
            if (ench.value().getMaxLevel() == 1) continue;
            if (level < ench.value().getMaxLevel()) return false;
            if (!isForeignOvercap(ench, level, item)) {
                hasValidCandidate = true;
            }
        }
        return hasValidCandidate;
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
        var enchants = item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        List<Holder<Enchantment>> candidates = new ArrayList<>();

        for (var entry : enchants.entrySet()) {
            Holder<Enchantment> ench = entry.getKey();
            int level = entry.getIntValue();
            if (ench.value().getMaxLevel() == 1) continue;
            if (!overcap && level < ench.value().getMaxLevel()) candidates.add(ench);
            if (overcap && level >= ench.value().getMaxLevel() && !isForeignOvercap(ench, level, item)) candidates.add(ench);
        }

        if (candidates.isEmpty()) return;

        Holder<Enchantment> chosen = candidates.get(random.nextInt(candidates.size()));

        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchants);
        mutable.set(chosen, enchants.getLevel(chosen) + 1);
        item.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());

        consume(SLOT_LAPIS);
        consume(SLOT_LABRADORITE);

        if (overcap) {
            consume(SLOT_GOLD);
            markOvercapUsed(item);
            markTableOvercap(item, chosen);
        }

        if (level != null && !level.isClientSide) {
            level.playSound(null, worldPosition,
                    overcap ? ModSounds.GOLDEN_REFINE.get() : ModSounds.NORMAL_REFINE.get(),
                    SoundSource.BLOCKS, 1.0F, 0.9F + random.nextFloat() * 0.2F);
        }

        setChanged();
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    private void consume(int slot) {
        itemHandler.extractItem(slot, 1, false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("Inventory", itemHandler.serializeNBT(registries));
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.extrashiny.refining_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RefiningTableMenu(id, inventory, this);
    }
}