package net.r_nik.extrashiny.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {

    // Vanadium
    public static final Tier VANADIUM = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1621,
            10.0F,
            3.0F,
            16,
            () -> Ingredient.of(ModItems.VANADIUM_INGOT.get())
    );

    // Osmium
    public static final Tier OSMIUM = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            157,
            9.0F,
            1.0F,
            18,
            () -> Ingredient.of(ModItems.OSMIUM_INGOT.get())
    );

    // Damask
    public static final Tier DAMASK = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2031,
            9.0F,
            3.0F,
            15,
            () -> Ingredient.of(ModItems.DAMASK_INGOT.get())
    );
}