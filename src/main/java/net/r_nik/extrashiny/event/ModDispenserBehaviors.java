package net.r_nik.extrashiny.event;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.r_nik.extrashiny.entity.AuroralArrowEntity;
import net.r_nik.extrashiny.item.ModItems;

public class ModDispenserBehaviors {

    public static void register() {
        DispenserBlock.registerBehavior(ModItems.AURORAL_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                AuroralArrowEntity arrow = new AuroralArrowEntity(level, position.x(), position.y(), position.z());

                arrow.pickup = AbstractArrow.Pickup.ALLOWED;

                arrow.setCritArrow(true);

                return arrow;
            }
        });
    }
}