package net.r_nik.extrashiny.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SerrationEnchantment extends Enchantment {
    public SerrationEnchantment() {
        super(Rarity.COMMON, EnchantmentCategory.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
    @Override
    public float getDamageBonus(int level, MobType type) {
        return 1.0F + Math.max(0, level - 1) * 0.5F;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (other == Enchantments.IMPALING) {
            return false;
        }
        return super.checkCompatibility(other);
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }
}