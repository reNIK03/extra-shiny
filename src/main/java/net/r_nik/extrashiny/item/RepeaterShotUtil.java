package net.r_nik.extrashiny.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class RepeaterShotUtil {


    public static void applyCrossbowArrowSettings(
            AbstractArrow arrow,
            LivingEntity shooter,
            ItemStack crossbow,
            float velocity,
            float inaccuracy
    ) {
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity, inaccuracy);


        int pierceLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crossbow);
        if (pierceLevel > 0) {
            arrow.setPierceLevel((byte)pierceLevel);
        }

        arrow.invulnerableTime = 0;

        arrow.setShotFromCrossbow(true);
    }
}
