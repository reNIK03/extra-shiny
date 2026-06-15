package net.r_nik.extrashiny.datagen.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials;
import net.r_nik.extrashiny.trim.ExtraShinyTrimPatterns;

public class ExtraShinySpriteSourceProvider extends SpriteSourceProvider {

    public ExtraShinySpriteSourceProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, ExtraShiny.MOD_ID);
    }

    @Override
    protected void addSources() {

        this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
                .addSource(
                        BlueprintTrims.patternPermutationsOfVanillaMaterials(
                                new ResourceKey[]{
                                        ExtraShinyTrimPatterns.CIMMERIAN,
                                        ExtraShinyTrimPatterns.MEMORY,
                                        ExtraShinyTrimPatterns.DAMASK
                                }
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

        this.atlas(BLOCKS_ATLAS)
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
