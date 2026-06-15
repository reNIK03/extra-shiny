package net.r_nik.extrashiny.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
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
                models().cubeAll(
                        "labradorite_brick",
                        modLoc("block/labradorite_brick")
                )
        );


        stairsBlock(((StairBlock) ModBlocks.LABRADORITE_BRICK_STAIRS.get()), blockTexture(ModBlocks.LABRADORITE_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.LABRADORITE_BRICK_SLAB.get()), blockTexture(ModBlocks.LABRADORITE_BRICKS.get()), blockTexture(ModBlocks.LABRADORITE_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.LABRADORITE_BRICK_WALL.get()), blockTexture(ModBlocks.LABRADORITE_BRICKS.get()));



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

        simpleBlockWithItem(ModBlocks.VANADIUM_BRICKS.get(), models().cubeAll("vanadium_bricks", modLoc("block/vanadium_bricks")));
        stairsBlock(((StairBlock) ModBlocks.VANADIUM_BRICK_STAIRS.get()), blockTexture(ModBlocks.VANADIUM_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.VANADIUM_BRICK_SLAB.get()), blockTexture(ModBlocks.VANADIUM_BRICKS.get()), blockTexture(ModBlocks.VANADIUM_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.VANADIUM_BRICK_WALL.get()), blockTexture(ModBlocks.VANADIUM_BRICKS.get()));
        simpleBlockWithItem(ModBlocks.CHISELED_VANADIUM_BRICKS.get(), models().cubeAll("chiseled_vanadium_bricks", modLoc("block/chiseled_vanadium_bricks")));

        simpleBlockWithItem(ModBlocks.OSMIUM_BRICKS.get(), models().cubeAll("osmium_bricks", modLoc("block/osmium_bricks")));
        stairsBlock(((StairBlock) ModBlocks.OSMIUM_BRICK_STAIRS.get()), blockTexture(ModBlocks.OSMIUM_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.OSMIUM_BRICK_SLAB.get()), blockTexture(ModBlocks.OSMIUM_BRICKS.get()), blockTexture(ModBlocks.OSMIUM_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.OSMIUM_BRICK_WALL.get()), blockTexture(ModBlocks.OSMIUM_BRICKS.get()));
        simpleBlockWithItem(ModBlocks.CHISELED_OSMIUM_BRICKS.get(), models().cubeAll("chiseled_osmium_bricks", modLoc("block/chiseled_osmium_bricks")));

        makeIngotBlock(ModBlocks.VANADIUM_INGOT_BLOCK.get(), "vanadium_ingot");
        makeIngotBlock(ModBlocks.OSMIUM_INGOT_BLOCK.get(), "osmium_ingot");
        makeIngotBlock(ModBlocks.DAMASK_INGOT_BLOCK.get(), "damask_ingot");
    }

    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void makeIngotBlock(Block block, String name) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        for (int layer = 0; layer <= 3; layer++) {
            int templateLayer = layer + 1;

            Integer[] aboveLayers = new Integer[3 - layer];
            for (int i = 0; i < aboveLayers.length; i++) {
                aboveLayers[i] = layer + i + 1;
            }

            for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
                Direction.Axis layerAxis = (layer % 2 != 0) ? (axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X) : axis;

                String templateAxisName = layerAxis == Direction.Axis.X ? "z" : "x";

                String leftModelName = name + "_left_" + templateAxisName + "_layer" + templateLayer;
                var leftModel = models().withExistingParent(leftModelName, modLoc("block/template_ingot_left_" + templateAxisName + "_layer" + templateLayer))
                        .texture("ingot", modLoc("block/" + name));

                String rightModelName = name + "_right_" + templateAxisName + "_layer" + templateLayer;
                var rightModel = models().withExistingParent(rightModelName, modLoc("block/template_ingot_right_" + templateAxisName + "_layer" + templateLayer))
                        .texture("ingot", modLoc("block/" + name));

                if (aboveLayers.length > 0) {
                    builder.part().modelFile(leftModel).addModel()
                            .condition(IngotBlock.AXIS, axis)
                            .condition(IngotBlock.LAYERS, aboveLayers)
                            .end()
                            .part().modelFile(rightModel).addModel()
                            .condition(IngotBlock.AXIS, axis)
                            .condition(IngotBlock.LAYERS, aboveLayers)
                            .end();
                }

                builder.part().modelFile(leftModel).addModel()
                        .condition(IngotBlock.AXIS, axis)
                        .condition(IngotBlock.LAYERS, layer)
                        .condition(IngotBlock.TOP_INGOT, IngotLayer.LEFT, IngotLayer.BOTH)
                        .end();

                builder.part().modelFile(rightModel).addModel()
                        .condition(IngotBlock.AXIS, axis)
                        .condition(IngotBlock.LAYERS, layer)
                        .condition(IngotBlock.TOP_INGOT, IngotLayer.RIGHT, IngotLayer.BOTH)
                        .end();
            }
        }
    }
}

