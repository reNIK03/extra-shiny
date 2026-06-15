package net.r_nik.extrashiny.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.r_nik.extrashiny.ExtraShiny;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.block.entity.RefiningTableEntity;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.sound.ModSounds;


import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ExtraShiny.MOD_ID);

        public static final RegistryObject<Block> VANADIUM_BLOCK = registerBlock("vanadium_block",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(ModSounds.VANADIUM_SOUND_TYPE)));
        public static final RegistryObject<Block> RAW_VANADIUM_BLOCK = registerBlock("raw_vanadium_block",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)));


    public static final RegistryObject<Block> LABRADORITE_BLOCK = registerBlock("labradorite_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(ModSounds.LABRADORITE_SOUND_TYPE)));

    public static final RegistryObject<Block> RAW_OSMIUM_BLOCK = registerBlock("raw_osmium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> LABRADORITE_BRICKS = registerBlock(
            "labradorite_brick",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)
                    .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                    .requiresCorrectToolForDrops())
    );


    public static final RegistryObject<Block> LABRADORITE_PILLAR = registerBlock(
            "labradorite_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.copy(Blocks.QUARTZ_PILLAR)
                            .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Block> LABRADORITE_LAMP = registerBlock(
            "labradorite_lamp",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLOWSTONE)
                    .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Block> VANADIUM_BARS = registerBlock(
            "vanadium_bars",
            () -> new IronBarsBlock(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BARS)
                            .strength(5.0F)
                            .sound(ModSounds.VANADIUM_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
            )
    );

    public static final RegistryObject<Block> OSMIUM_BLOCK = registerBlock("osmium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(ModSounds.OSMIUM_SOUND_TYPE)));


    public static final RegistryObject<Block> OSMIUM_BARS = registerBlock(
            "osmium_bars",
            () -> new IronBarsBlock(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BARS)
                            .strength(5.0F)
                            .sound(ModSounds.OSMIUM_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
            )
    );

    public static final RegistryObject<Block> LEAP_RAIL = BLOCKS.register("leap_rail",
            () -> new LeapRailBlock(Block.Properties.copy(Blocks.POWERED_RAIL)));

    public static final RegistryObject<Block> OSMIUM_SPOTLIGHT = BLOCKS.register("osmium_spotlight",
            () -> new OsmiumSpotlightBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(2.0f, 4.0f)
                    .sound(ModSounds.OSMIUM_SOUND_TYPE)
                    .lightLevel(state -> 15) // Max light level!
                    .noOcclusion()));


    public static final RegistryObject<Block> VANADIUM_ORE = registerBlock("vanadium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0F, 3.0F) // Slower than stone (1.5)
                    .requiresCorrectToolForDrops(), UniformInt.of(4,8)));

    public static final RegistryObject<Block> DEEPSLATE_VANADIUM_ORE = registerBlock("deepslate_vanadium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                    .strength(4.5F, 3.0F) // Slower than regular ore (3.0)
                    .requiresCorrectToolForDrops(), UniformInt.of(4,8)));

    public static final RegistryObject<Block> LABRADORITE_ORE = registerBlock("labradorite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
                    .strength(3.0F, 3.0F) // Slower than stone (1.5)
                    .requiresCorrectToolForDrops(), UniformInt.of(4,8)));

    public static final RegistryObject<Block> DEEPSLATE_LABRADORITE_ORE = registerBlock("deepslate_labradorite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)
                    .strength(4.5F, 3.0F) // Slower than regular ore (3.0)
                    .requiresCorrectToolForDrops(), UniformInt.of(4,8)));

    public static final RegistryObject<Block> OSMIUM_ORE = registerBlock("osmium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops(), UniformInt.of(0, 1)));

    public static final RegistryObject<Block> DEEPSLATE_OSMIUM_ORE = registerBlock("deepslate_osmium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE)
                    .strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops(), UniformInt.of(0, 1)));

    public static final RegistryObject<Block> SPOTTED_BLACKSTONE = registerBlock("spotted_blackstone",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.GILDED_BLACKSTONE)
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops(), UniformInt.of(0, 1)));

    public static final RegistryObject<Block> LABRADORITE_BRICK_STAIRS = registerBlock(
            "labradorite_brick_stairs",
            () -> new StairBlock(
                    () -> LABRADORITE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)
                            .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Block> LABRADORITE_BRICK_SLAB = registerBlock(
            "labradorite_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)
                            .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Block> LABRADORITE_BRICK_WALL = registerBlock(
            "labradorite_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)
                            .sound(ModSounds.LABRADORITE_SOUND_TYPE)
                            .requiresCorrectToolForDrops()
            )
    );



    public static final RegistryObject<Block> REFINING_TABLE = registerBlock(
            "refining_table",
            () -> new RefiningTableBlock(
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                            .strength(3.5F)
                            .sound(SoundType.STONE)
                            .noOcclusion()
            )
    );


    public static final RegistryObject<Block> VANADIUM_INGOT_BLOCK = BLOCKS.register("vanadium_ingot_block",
            () -> new IngotBlock(ModItems.VANADIUM_INGOT, BlockBehaviour.Properties.of().sound(ModSounds.VANADIUM_SOUND_TYPE).mapColor(MapColor.METAL).strength(1.0f, 2.0f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> OSMIUM_INGOT_BLOCK = BLOCKS.register("osmium_ingot_block",
            () -> new IngotBlock(ModItems.OSMIUM_INGOT, BlockBehaviour.Properties.of().sound(ModSounds.OSMIUM_SOUND_TYPE).mapColor(MapColor.METAL).strength(1.0f, 2.0f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> DAMASK_INGOT_BLOCK = BLOCKS.register("damask_ingot_block",
            () -> new IngotBlock(ModItems.DAMASK_INGOT, BlockBehaviour.Properties.of().sound(ModSounds.DAMASK_SOUND_TYPE).mapColor(MapColor.METAL).strength(1.0f, 2.0f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> VANADIUM_BRICKS = registerBlock("vanadium_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.VANADIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> VANADIUM_BRICK_STAIRS = registerBlock("vanadium_brick_stairs",
            () -> new StairBlock(() -> VANADIUM_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.VANADIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> VANADIUM_BRICK_SLAB = registerBlock("vanadium_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.VANADIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> VANADIUM_BRICK_WALL = registerBlock("vanadium_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.VANADIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> CHISELED_VANADIUM_BRICKS = registerBlock("chiseled_vanadium_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.VANADIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> OSMIUM_BRICKS = registerBlock("osmium_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.OSMIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> OSMIUM_BRICK_STAIRS = registerBlock("osmium_brick_stairs",
            () -> new StairBlock(() -> OSMIUM_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.OSMIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> OSMIUM_BRICK_SLAB = registerBlock("osmium_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.OSMIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> OSMIUM_BRICK_WALL = registerBlock("osmium_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.OSMIUM_SOUND_TYPE)));

    public static final RegistryObject<Block> CHISELED_OSMIUM_BRICKS = registerBlock("chiseled_osmium_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).sound(ModSounds.OSMIUM_SOUND_TYPE)));




    public static final RegistryObject<Block> CIMMERIAN_BLOCK = registerBlock("cimmerian_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(ModSounds.CIMMERIAN_SOUND_TYPE)));


    public static final RegistryObject<Block> DAMASK_BLOCK = registerBlock("damask_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).sound(ModSounds.DAMASK_SOUND_TYPE)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name,() -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
