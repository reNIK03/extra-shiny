package net.r_nik.extrashiny.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.IngotBlock;
import net.r_nik.extrashiny.block.IngotLayer;
import net.r_nik.extrashiny.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExtraShiny.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.VANADIUM_BLOCK);
        blockWithItem(ModBlocks.RAW_VANADIUM_BLOCK);
        blockWithItem(ModBlocks.VANADIUM_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_VANADIUM_ORE);

        simpleBlockWithItem(
                ModBlocks.REFINING_TABLE.get(),
                models().cubeBottomTop(
                        "refining_table",
                        modLoc("block/refining_table_side"),
                        modLoc("block/refining_table_bottom"),
                        modLoc("block/refining_table_top")
                )
        );

        blockWithItem(ModBlocks.LABRADORITE_LAMP);

        simpleBlockWithItem(
                ModBlocks.LABRADORITE_BRICKS.get(),
                models().cubeAll("labradorite_brick", modLoc("block/labradorite_brick"))
        );

        stairsBlock((StairBlock) ModBlocks.LABRADORITE_BRICK_STAIRS.get(), modLoc("block/labradorite_brick"));
        slabBlock((SlabBlock) ModBlocks.LABRADORITE_BRICK_SLAB.get(), modLoc("block/labradorite_brick"), modLoc("block/labradorite_brick"));
        wallBlock((WallBlock) ModBlocks.LABRADORITE_BRICK_WALL.get(), modLoc("block/labradorite_brick"));

        blockWithItem(ModBlocks.LABRADORITE_BLOCK);
        blockWithItem(ModBlocks.LABRADORITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_LABRADORITE_ORE);

        axisBlock(
                (RotatedPillarBlock) ModBlocks.LABRADORITE_PILLAR.get(),
                modLoc("block/labradorite_pillar"),
                modLoc("block/labradorite_pillar_top")
        );
        itemModels().getBuilder("labradorite_pillar")
                .parent(models().getExistingFile(modLoc("block/labradorite_pillar")));

        blockWithItem(ModBlocks.OSMIUM_BLOCK);
        blockWithItem(ModBlocks.CIMMERIAN_BLOCK);
        blockWithItem(ModBlocks.DAMASK_BLOCK);
        blockWithItem(ModBlocks.RAW_OSMIUM_BLOCK);
        blockWithItem(ModBlocks.OSMIUM_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_OSMIUM_ORE);
        blockWithItem(ModBlocks.SPOTTED_BLACKSTONE);

        registerBrickSet(ModBlocks.VANADIUM_BRICKS, ModBlocks.VANADIUM_BRICK_STAIRS, ModBlocks.VANADIUM_BRICK_SLAB, ModBlocks.VANADIUM_BRICK_WALL, ModBlocks.CHISELED_VANADIUM_BRICKS, "vanadium_bricks", "chiseled_vanadium_bricks");
        registerBrickSet(ModBlocks.OSMIUM_BRICKS, ModBlocks.OSMIUM_BRICK_STAIRS, ModBlocks.OSMIUM_BRICK_SLAB, ModBlocks.OSMIUM_BRICK_WALL, ModBlocks.CHISELED_OSMIUM_BRICKS, "osmium_bricks", "chiseled_osmium_bricks");

        makeIngotBlock(ModBlocks.VANADIUM_INGOT_BLOCK.get(), "vanadium_ingot");
        makeIngotBlock(ModBlocks.OSMIUM_INGOT_BLOCK.get(), "osmium_ingot");
        makeIngotBlock(ModBlocks.DAMASK_INGOT_BLOCK.get(), "damask_ingot");
    }

    private void registerBrickSet(DeferredHolder<Block, ?> base, DeferredHolder<Block, ?> stairs, DeferredHolder<Block, ?> slab, DeferredHolder<Block, ?> wall, DeferredHolder<Block, ?> chiseled, String name, String chiseledName) {
        simpleBlockWithItem(base.get(), models().cubeAll(name, modLoc("block/" + name)));
        stairsBlock((StairBlock) stairs.get(), modLoc("block/" + name));
        slabBlock((SlabBlock) slab.get(), modLoc("block/" + name), modLoc("block/" + name));
        wallBlock((WallBlock) wall.get(), modLoc("block/" + name));
        simpleBlockWithItem(chiseled.get(), models().cubeAll(chiseledName, modLoc("block/" + chiseledName)));
    }

    private void blockWithItem(DeferredHolder<Block, ?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void makeIngotBlock(Block block, String name) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        for (int layer = 0; layer <= 3; layer++) {
            int templateLayer = layer + 1;
            Integer[] aboveLayers = new Integer[3 - layer];
            for (int i = 0; i < aboveLayers.length; i++) aboveLayers[i] = layer + i + 1;

            for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
                Direction.Axis layerAxis = (layer % 2 != 0) ? (axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X) : axis;
                String templateAxisName = layerAxis == Direction.Axis.X ? "z" : "x";
                var leftModel = models().withExistingParent(name + "_left_" + templateAxisName + "_layer" + templateLayer, modLoc("block/template_ingot_left_" + templateAxisName + "_layer" + templateLayer)).texture("ingot", modLoc("block/" + name));
                var rightModel = models().withExistingParent(name + "_right_" + templateAxisName + "_layer" + templateLayer, modLoc("block/template_ingot_right_" + templateAxisName + "_layer" + templateLayer)).texture("ingot", modLoc("block/" + name));

                if (aboveLayers.length > 0) {
                    builder.part().modelFile(leftModel).addModel().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, aboveLayers).end()
                            .part().modelFile(rightModel).addModel().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, aboveLayers).end();
                }
                builder.part().modelFile(leftModel).addModel().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer).condition(IngotBlock.TOP_INGOT, IngotLayer.LEFT, IngotLayer.BOTH).end();
                builder.part().modelFile(rightModel).addModel().condition(IngotBlock.AXIS, axis).condition(IngotBlock.LAYERS, layer).condition(IngotBlock.TOP_INGOT, IngotLayer.RIGHT, IngotLayer.BOTH).end();
            }
        }
    }
}