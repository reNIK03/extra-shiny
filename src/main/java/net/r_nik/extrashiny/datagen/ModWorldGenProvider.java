package net.r_nik.extrashiny.datagen;

import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials;
import net.r_nik.extrashiny.trim.ExtraShinyTrimPatterns;
import net.r_nik.extrashiny.worldgen.ModBiomeModifiers;
import net.r_nik.extrashiny.worldgen.ModConfiguredFeatures;
import net.r_nik.extrashiny.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.TRIM_MATERIAL, ExtraShinyTrimMaterials::bootstrap)
            .add(Registries.TRIM_PATTERN, ExtraShinyTrimPatterns::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ExtraShiny.MOD_ID));
    }
}