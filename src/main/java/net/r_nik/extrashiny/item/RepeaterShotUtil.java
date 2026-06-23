package net.r_nik.extrashiny.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

public class RepeaterShotUtil {

    public static void applyCrossbowArrowSettings(
            AbstractArrow arrow,
            LivingEntity shooter,
            ItemStack crossbow, // Kept to preserve your method signature!
            float velocity,
            float inaccuracy
    ) {
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity, inaccuracy);

        arrow.invulnerableTime = 0;

    }
}