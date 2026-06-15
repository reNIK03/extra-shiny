package net.r_nik.extrashiny.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.r_nik.extrashiny.entity.AuroralArrowEntity;

public class AuroralArrowItem extends ArrowItem {
    public AuroralArrowItem(Properties props) {
        super(props);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new AuroralArrowEntity(level, shooter);
    }

}
