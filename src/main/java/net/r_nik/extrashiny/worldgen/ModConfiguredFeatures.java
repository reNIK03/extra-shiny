package net.r_nik.extrashiny.worldgen;

import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> VANADIUM_ORE_KEY = registerKey("vanadium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LABRADORITE_ORE_UPPER_KEY =
            registerKey("labradorite_ore_upper");

    public static final ResourceKey<ConfiguredFeature<?, ?>> LABRADORITE_ORE_LOWER_KEY =
            registerKey("labradorite_ore_lower");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OSMIUM_ORE_KEY = registerKey("osmium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPOTTED_BLACKSTONE_KEY = registerKey("spotted_blackstone");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest blackstoneReplaceable = new BlockMatchTest(Blocks.BLACKSTONE);

        List<OreConfiguration.TargetBlockState> overworldLabradoriteOres = List.of(
                OreConfiguration.target(stoneReplaceable,
                        ModBlocks.LABRADORITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables,
                        ModBlocks.DEEPSLATE_LABRADORITE_ORE.get().defaultBlockState())
        );

        register(context,
                LABRADORITE_ORE_UPPER_KEY,
                Feature.ORE,
                new OreConfiguration(overworldLabradoriteOres, 5)
        );

        register(context,
                LABRADORITE_ORE_LOWER_KEY,
                Feature.ORE,
                new OreConfiguration(
                        overworldLabradoriteOres,
                        5,
                        0.0f // discard chance on air exposure
                )
        );

        List<OreConfiguration.TargetBlockState> overworldOsmiumOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.OSMIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_OSMIUM_ORE.get().defaultBlockState())
        );

        register(context, OSMIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldOsmiumOres, 9));

        List<OreConfiguration.TargetBlockState> netherSpottedBlackstone = List.of(
                OreConfiguration.target(blackstoneReplaceable, ModBlocks.SPOTTED_BLACKSTONE.get().defaultBlockState())
        );

        register(context, SPOTTED_BLACKSTONE_KEY, Feature.ORE, new OreConfiguration(netherSpottedBlackstone, 10, 1.0f));

        List<OreConfiguration.TargetBlockState> overworldVanadiumOres = List.of(OreConfiguration.target(stoneReplaceable,
                        ModBlocks.VANADIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_VANADIUM_ORE.get().defaultBlockState()));

        register(context, VANADIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldVanadiumOres, 4));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}