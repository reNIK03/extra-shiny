package net.r_nik.extrashiny.event;

import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import net.r_nik.extrashiny.item.ModItems;

public class ModDispenserBehaviors {

    public static void register() {
        DispenserBlock.registerBehavior(
                ModItems.AURORAL_ARROW.get(),
                new ProjectileDispenseBehavior(ModItems.AURORAL_ARROW.get())
        );
    }
}