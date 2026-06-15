package net.r_nik.extrashiny.datagen;

import com.teamabnormals.blueprint.core.other.tags.BlueprintTrimMaterialTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials;

import java.util.concurrent.CompletableFuture;

public class ModTrimMaterialTagProvider extends TagsProvider<TrimMaterial> {

    public ModTrimMaterialTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookup,
            ExistingFileHelper helper
    ) {
        super(output, Registries.TRIM_MATERIAL, lookup, ExtraShiny.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlueprintTrimMaterialTags.GENERATES_OVERRIDES)
                .add(
                        ExtraShinyTrimMaterials.VANADIUM,
                        ExtraShinyTrimMaterials.VANADIUM_DARKER,
                        ExtraShinyTrimMaterials.LABRADORITE,
                        ExtraShinyTrimMaterials.OSMIUM,
                        ExtraShinyTrimMaterials.OSMIUM_DARKER,
                        ExtraShinyTrimMaterials.CIMMERIAN,
                        ExtraShinyTrimMaterials.CIMMERIAN_DARKER,
                        ExtraShinyTrimMaterials.DAMASK,
                        ExtraShinyTrimMaterials.DAMASK_DARKER
                );
    }

}
