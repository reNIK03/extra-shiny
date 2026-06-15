package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, ExtraShiny.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.VANADIUM_BARS.get())
                .add(ModBlocks.OSMIUM_BARS.get());

        this.tag(BlockTags.RAILS)
                .add(ModBlocks.LEAP_RAIL.get());

        this.tag(BlockTags.WALLS)
                .add(
                        ModBlocks.LABRADORITE_BRICK_WALL.get(),
                        ModBlocks.VANADIUM_BRICK_WALL.get(),
                        ModBlocks.OSMIUM_BRICK_WALL.get()
                );

        this.tag(BlockTags.BEACON_BASE_BLOCKS)
                .add(
                        ModBlocks.VANADIUM_BLOCK.get(),
                        ModBlocks.OSMIUM_BLOCK.get(),
                        ModBlocks.DAMASK_BLOCK.get()
                );

        this.tag(Tags.Blocks.ORES)
                .add(
                        ModBlocks.VANADIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_VANADIUM_ORE.get(),
                        ModBlocks.LABRADORITE_ORE.get(),
                        ModBlocks.DEEPSLATE_LABRADORITE_ORE.get(),
                        ModBlocks.OSMIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_OSMIUM_ORE.get(),
                        ModBlocks.SPOTTED_BLACKSTONE.get()
                );

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(
                        ModBlocks.VANADIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_VANADIUM_ORE.get(),
                        ModBlocks.VANADIUM_BARS.get(),
                        ModBlocks.OSMIUM_BARS.get(),
                        ModBlocks.LABRADORITE_ORE.get(),
                        ModBlocks.LABRADORITE_BRICKS.get(),
                        ModBlocks.LABRADORITE_PILLAR.get(),
                        ModBlocks.LABRADORITE_LAMP.get(),
                        ModBlocks.LABRADORITE_BRICK_STAIRS.get(),
                        ModBlocks.LABRADORITE_BRICK_SLAB.get(),
                        ModBlocks.LABRADORITE_BRICK_WALL.get(),
                        ModBlocks.CIMMERIAN_BLOCK.get(),
                        ModBlocks.DEEPSLATE_LABRADORITE_ORE.get(),
                        ModBlocks.VANADIUM_BRICKS.get(),
                        ModBlocks.VANADIUM_BRICK_STAIRS.get(),
                        ModBlocks.VANADIUM_BRICK_SLAB.get(),
                        ModBlocks.VANADIUM_BRICK_WALL.get(),
                        ModBlocks.CHISELED_VANADIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_BRICK_STAIRS.get(),
                        ModBlocks.OSMIUM_BRICK_SLAB.get(),
                        ModBlocks.OSMIUM_BRICK_WALL.get(),
                        ModBlocks.CHISELED_OSMIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_OSMIUM_ORE.get(),
                        ModBlocks.SPOTTED_BLACKSTONE.get(),
                        ModBlocks.VANADIUM_INGOT_BLOCK.get(),
                        ModBlocks.VANADIUM_BLOCK.get(),
                        ModBlocks.OSMIUM_BLOCK.get(),
                        ModBlocks.OSMIUM_INGOT_BLOCK.get()
                );

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DAMASK_BLOCK.get(),
                        ModBlocks.DAMASK_INGOT_BLOCK.get()
                        );


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        ModBlocks.VANADIUM_BLOCK.get(),
                        ModBlocks.OSMIUM_BLOCK.get(),
                        ModBlocks.RAW_VANADIUM_BLOCK.get(),
                        ModBlocks.RAW_OSMIUM_BLOCK.get(),
                        ModBlocks.DAMASK_INGOT_BLOCK.get(),
                        ModBlocks.VANADIUM_INGOT_BLOCK.get(),
                        ModBlocks.OSMIUM_INGOT_BLOCK.get(),
                        ModBlocks.VANADIUM_ORE.get(),
                        ModBlocks.VANADIUM_BARS.get(),
                        ModBlocks.OSMIUM_BARS.get(),
                        ModBlocks.DEEPSLATE_VANADIUM_ORE.get(),
                        ModBlocks.LABRADORITE_ORE.get(),
                        ModBlocks.DEEPSLATE_LABRADORITE_ORE.get(),
                        ModBlocks.LABRADORITE_BLOCK.get(),
                        ModBlocks.LABRADORITE_BRICKS.get(),
                        ModBlocks.LABRADORITE_PILLAR.get(),
                        ModBlocks.LABRADORITE_LAMP.get(),
                        ModBlocks.LABRADORITE_BRICK_STAIRS.get(),
                        ModBlocks.LABRADORITE_BRICK_SLAB.get(),
                        ModBlocks.LABRADORITE_BRICK_WALL.get(),
                        ModBlocks.CIMMERIAN_BLOCK.get(),
                        ModBlocks.DAMASK_BLOCK.get(),
                        ModBlocks.REFINING_TABLE.get(),
                        ModBlocks.VANADIUM_BRICKS.get(),
                        ModBlocks.VANADIUM_BRICK_STAIRS.get(),
                        ModBlocks.VANADIUM_BRICK_SLAB.get(),
                        ModBlocks.VANADIUM_BRICK_WALL.get(),
                        ModBlocks.CHISELED_VANADIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_BRICK_STAIRS.get(),
                        ModBlocks.OSMIUM_BRICK_SLAB.get(),
                        ModBlocks.OSMIUM_BRICK_WALL.get(),
                        ModBlocks.CHISELED_OSMIUM_BRICKS.get(),
                        ModBlocks.OSMIUM_ORE.get(),
                        ModBlocks.DEEPSLATE_OSMIUM_ORE.get(),
                        ModBlocks.SPOTTED_BLACKSTONE.get()

                );
    }
}