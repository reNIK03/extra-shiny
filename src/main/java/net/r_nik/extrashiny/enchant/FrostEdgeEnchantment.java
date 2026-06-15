package net.r_nik.extrashiny.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FrostEdgeEnchantment extends Enchantment {

    public FrostEdgeEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (other == Enchantments.FIRE_ASPECT) return false;
        return super.checkCompatibility(other);
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }
}