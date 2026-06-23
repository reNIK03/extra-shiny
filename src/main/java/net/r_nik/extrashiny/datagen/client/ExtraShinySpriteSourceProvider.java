package net.r_nik.extrashiny.datagen.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials;
import net.r_nik.extrashiny.trim.ExtraShinyTrimPatterns;

import java.util.concurrent.CompletableFuture;

public class ExtraShinySpriteSourceProvider extends SpriteSourceProvider {

    public ExtraShinySpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, ExtraShiny.MOD_ID, helper);
    }

    @Override
    protected void gather() {

        this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
                .addSource(
                        BlueprintTrims.patternPermutationsOfVanillaMaterials(
                                ExtraShinyTrimPatterns.CIMMERIAN,
                                ExtraShinyTrimPatterns.MEMORY,
                                ExtraShinyTrimPatterns.DAMASK
                        )
                )
                .addSource(
                        BlueprintTrims.materialPatternPermutations(
                                ExtraShinyTrimMaterials.VANADIUM,
                                ExtraShinyTrimMaterials.VANADIUM_DARKER,
                                ExtraShinyTrimMaterials.LABRADORITE,
                                ExtraShinyTrimMaterials.OSMIUM,
                                ExtraShinyTrimMaterials.OSMIUM_DARKER,
                                ExtraShinyTrimMaterials.CIMMERIAN,
                                ExtraShinyTrimMaterials.CIMMERIAN_DARKER,
                                ExtraShinyTrimMaterials.DAMASK,
                                ExtraShinyTrimMaterials.DAMASK_DARKER
                        )
                );

        this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
                .addSource(
                        BlueprintTrims.materialPermutationsForItemLayers(
                                ExtraShinyTrimMaterials.VANADIUM,
                                ExtraShinyTrimMaterials.VANADIUM_DARKER,
                                ExtraShinyTrimMaterials.LABRADORITE,
                                ExtraShinyTrimMaterials.OSMIUM,
                                ExtraShinyTrimMaterials.OSMIUM_DARKER,
                                ExtraShinyTrimMaterials.CIMMERIAN,
                                ExtraShinyTrimMaterials.CIMMERIAN_DARKER,
                                ExtraShinyTrimMaterials.DAMASK,
                                ExtraShinyTrimMaterials.DAMASK_DARKER
                        )
                );
    }
}