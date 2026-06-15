package net.r_nik.extrashiny.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.List;

public class ModToolTiers {

    public static final Tier VANADIUM = TierSortingRegistry.registerTier(
            new ForgeTier(
                    3,
                    1621,
                    10.0F,
                    3.0F,
                    16,
                    null, // Keep as null to avoid breaking vanilla tiers
                    () -> Ingredient.of(ModItems.VANADIUM_INGOT.get())
            ),
            new ResourceLocation(ExtraShiny.MOD_ID, "vanadium"),
            List.of(Tiers.DIAMOND),
            List.of(Tiers.NETHERITE)
    );

    public static final Tier OSMIUM = TierSortingRegistry.registerTier(
            new ForgeTier(
                    2,
                    157,
                    9.0F,
                    1.0F,
                    18,
                    null, // Changed from BlockTags.NEEDS_STONE_TOOL
                    () -> Ingredient.of(ModItems.OSMIUM_INGOT.get())
            ),
            new ResourceLocation(ExtraShiny.MOD_ID, "osmium"),
            List.of(Tiers.IRON), // Adjusted so it sits parallel/above Iron, not Gold
            List.of(Tiers.DIAMOND)
    );

    public static final Tier DAMASK = TierSortingRegistry.registerTier(
            new ForgeTier(
                    4,
                    2031,
                    9.0F,
                    3.0F,
                    15,
                    null, // Changed from BlockTags.NEEDS_DIAMOND_TOOL
                    () -> Ingredient.of(ModItems.DAMASK_INGOT.get())
            ),
            new ResourceLocation(ExtraShiny.MOD_ID, "damask"),
            List.of(Tiers.NETHERITE),
            List.of()
    );
}