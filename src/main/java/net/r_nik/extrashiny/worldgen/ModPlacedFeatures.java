package net.r_nik.extrashiny.worldgen;

import net.minecraft.world.level.levelgen.placement.*;
import net.r_nik.extrashiny.ExtraShiny;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> VANADIUM_ORE_PLACED_KEY = registerKey("vanadium_ore_placed");
    public static final ResourceKey<PlacedFeature> LABRADORITE_ORE_UPPER_PLACED_KEY =
            registerKey("labradorite_ore_upper_placed");

    public static final ResourceKey<PlacedFeature> LABRADORITE_ORE_LOWER_PLACED_KEY =
            registerKey("labradorite_ore_lower_placed");

    public static final ResourceKey<PlacedFeature> OSMIUM_ORE_PLACED_KEY = registerKey("osmium_ore_placed");
    public static final ResourceKey<PlacedFeature> OSMIUM_ORE_LARGE_PLACED_KEY = registerKey("osmium_ore_large_placed");
    public static final ResourceKey<PlacedFeature> SPOTTED_BLACKSTONE_PLACED_KEY = registerKey("spotted_blackstone_placed");


    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, VANADIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.VANADIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(60))));

        // Batch 1: triangle, peak at Y=0
        register(context,
                LABRADORITE_ORE_UPPER_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LABRADORITE_ORE_UPPER_KEY),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-32),
                                VerticalAnchor.absolute(32)
                        ),
                        BiomeFilter.biome()
                )
        );

        register(context,
                LABRADORITE_ORE_LOWER_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LABRADORITE_ORE_LOWER_KEY),
                List.of(
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(64)
                        ),
                        BiomeFilter.biome()
                )
        );

        register(context, OSMIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OSMIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(4,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));

        register(context, OSMIUM_ORE_LARGE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OSMIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(256))));

        register(context, SPOTTED_BLACKSTONE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SPOTTED_BLACKSTONE_KEY),
                ModOrePlacement.commonOrePlacement(10,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(10), VerticalAnchor.absolute(117))));


    }




    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExtraShiny.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}