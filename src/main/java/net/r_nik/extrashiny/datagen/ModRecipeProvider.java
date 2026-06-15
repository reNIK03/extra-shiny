package net.r_nik.extrashiny.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.item.ModItems;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> VANADIUM_SMELTABLES =
            List.of(ModItems.RAW_VANADIUM.get(),
                    ModBlocks.VANADIUM_ORE.get(), ModBlocks.DEEPSLATE_VANADIUM_ORE.get());

    private static final List<ItemLike> OSMIUM_SMELTABLES = List.of(
            ModItems.RAW_OSMIUM.get(),
            ModBlocks.OSMIUM_ORE.get(),
            ModBlocks.DEEPSLATE_OSMIUM_ORE.get()
    );

    private static final List<ItemLike> LABRADORITE_SMELTABLES = List.of(
            ModBlocks.LABRADORITE_ORE.get(),
            ModBlocks.DEEPSLATE_LABRADORITE_ORE.get()
    );

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        buildVanadiumRecipes(pWriter);
        buildOsmiumRecipes(pWriter);
        buildDamaskRecipes(pWriter);
        buildCimmerianRecipes(pWriter);
        buildLabradoriteRecipes(pWriter);
        buildMiscRecipes(pWriter);
        buildTemplatesAndTrims(pWriter);
    }

    private void buildVanadiumRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, VANADIUM_SMELTABLES, RecipeCategory.MISC, ModItems.VANADIUM_INGOT.get(), 0.3f, 200, "vanadium_ingot");
        oreBlasting(pWriter, VANADIUM_SMELTABLES, RecipeCategory.MISC, ModItems.VANADIUM_INGOT.get(), 0.3f, 100, "vanadium_ingot");

        smithingVanadium(pWriter, Items.IRON_SWORD, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_SWORD.get());
        smithingVanadium(pWriter, Items.IRON_PICKAXE, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_PICKAXE.get());
        smithingVanadium(pWriter, Items.IRON_AXE, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_AXE.get());
        smithingVanadium(pWriter, Items.IRON_SHOVEL, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_SHOVEL.get());
        smithingVanadium(pWriter, Items.IRON_HOE, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_HOE.get());
        smithingVanadium(pWriter, Items.TRIDENT, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_PARTISAN.get());
        smithingVanadium(pWriter, Items.CROSSBOW, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_REPEATER.get());

        smithingVanadium(pWriter, Items.IRON_HELMET, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_HELMET.get());
        smithingVanadium(pWriter, Items.IRON_CHESTPLATE, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_CHESTPLATE.get());
        smithingVanadium(pWriter, Items.IRON_LEGGINGS, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_LEGGINGS.get());
        smithingVanadium(pWriter, Items.IRON_BOOTS, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_BOOTS.get());
        smithingVanadium(pWriter, Items.IRON_HORSE_ARMOR, ModItems.VANADIUM_INGOT.get(), ModItems.VANADIUM_HORSE_ARMOR.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.VANADIUM_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.VANADIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.VANADIUM_INGOT.get()), has(ModItems.VANADIUM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_VANADIUM_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.RAW_VANADIUM.get())
                .unlockedBy(getHasName(ModItems.RAW_VANADIUM.get()), has(ModItems.RAW_VANADIUM.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VANADIUM_INGOT.get(), 9)
                .requires(ModBlocks.VANADIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BLOCK.get()), has(ModBlocks.VANADIUM_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_VANADIUM.get(), 9)
                .requires(ModBlocks.RAW_VANADIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RAW_VANADIUM_BLOCK.get()), has(ModBlocks.RAW_VANADIUM_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VANADIUM_NUGGET.get(), 9)
                .requires(ModItems.VANADIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.VANADIUM_INGOT.get()), has(ModItems.VANADIUM_INGOT.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":vanadium_nuggets_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VANADIUM_INGOT.get())
                .pattern("NNN").pattern("NNN").pattern("NNN")
                .define('N', ModItems.VANADIUM_NUGGET.get())
                .unlockedBy(getHasName(ModItems.VANADIUM_NUGGET.get()), has(ModItems.VANADIUM_NUGGET.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":vanadium_ingot_from_nuggets");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.VANADIUM_BARS.get(), 16)
                .pattern("III").pattern("III")
                .define('I', ModItems.VANADIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.VANADIUM_INGOT.get()), has(ModItems.VANADIUM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICKS.get(), 4)
                .pattern("ID").pattern("DI")
                .define('I', ModItems.VANADIUM_INGOT.get())
                .define('D', Items.DEEPSLATE)
                .unlockedBy(getHasName(ModItems.VANADIUM_INGOT.get()), has(ModItems.VANADIUM_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_STAIRS.get(), 4)
                .pattern("B  ").pattern("BB ").pattern("BBB")
                .define('B', ModBlocks.VANADIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_SLAB.get(), 6)
                .pattern("BBB")
                .define('B', ModBlocks.VANADIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_WALL.get(), 6)
                .pattern("BBB").pattern("BBB")
                .define('B', ModBlocks.VANADIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_VANADIUM_BRICKS.get(), 1)
                .pattern("S").pattern("S")
                .define('S', ModBlocks.VANADIUM_BRICK_SLAB.get())
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICK_SLAB.get()), has(ModBlocks.VANADIUM_BRICK_SLAB.get()))
                .save(pWriter);

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.VANADIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_STAIRS.get(), 1)
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":vanadium_bricks_to_stairs_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.VANADIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_SLAB.get(), 2)
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":vanadium_bricks_to_slab_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.VANADIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.VANADIUM_BRICK_WALL.get(), 1)
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":vanadium_bricks_to_wall_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.VANADIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_VANADIUM_BRICKS.get(), 1)
                .unlockedBy(getHasName(ModBlocks.VANADIUM_BRICKS.get()), has(ModBlocks.VANADIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":vanadium_bricks_to_chiseled_stonecutter");
    }

    private void buildOsmiumRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, OSMIUM_SMELTABLES, RecipeCategory.MISC, ModItems.OSMIUM_INGOT.get(), 0.7f, 200, "osmium_ingot");
        oreBlasting(pWriter, OSMIUM_SMELTABLES, RecipeCategory.MISC, ModItems.OSMIUM_INGOT.get(), 0.7f, 100, "osmium_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OSMIUM_HELMET.get())
                .pattern("LLL").pattern("L L")
                .define('L', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OSMIUM_CHESTPLATE.get())
                .pattern("L L").pattern("LLL").pattern("LLL")
                .define('L', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OSMIUM_LEGGINGS.get())
                .pattern("LLL").pattern("L L").pattern("L L")
                .define('L', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OSMIUM_BOOTS.get())
                .pattern("L L").pattern("L L")
                .define('L', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.OSMIUM_SWORD.get())
                .pattern("L").pattern("L").pattern("S")
                .define('L', ModItems.OSMIUM_INGOT.get()).define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OSMIUM_PICKAXE.get())
                .pattern("LLL").pattern(" S ").pattern(" S ")
                .define('L', ModItems.OSMIUM_INGOT.get()).define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OSMIUM_AXE.get())
                .pattern("LL").pattern("LS").pattern(" S")
                .define('L', ModItems.OSMIUM_INGOT.get()).define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OSMIUM_SHOVEL.get())
                .pattern("L").pattern("S").pattern("S")
                .define('L', ModItems.OSMIUM_INGOT.get()).define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.OSMIUM_HOE.get())
                .pattern("LL").pattern(" S").pattern(" S")
                .define('L', ModItems.OSMIUM_INGOT.get()).define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BULWARK.get())
                .pattern("OQO").pattern("OBO").pattern(" O ")
                .define('O', ModItems.OSMIUM_INGOT.get())
                .define('B', ModBlocks.OSMIUM_BLOCK.get())
                .define('Q', Blocks.QUARTZ_BLOCK)
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BLOCK.get()), has(ModBlocks.OSMIUM_BLOCK.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.OSMIUM_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_OSMIUM_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.RAW_OSMIUM.get())
                .unlockedBy(getHasName(ModItems.RAW_OSMIUM.get()), has(ModItems.RAW_OSMIUM.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_OSMIUM.get(), 9)
                .requires(ModBlocks.RAW_OSMIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RAW_OSMIUM_BLOCK.get()), has(ModBlocks.RAW_OSMIUM_BLOCK.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":raw_osmium_from_block");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OSMIUM_INGOT.get(), 9)
                .requires(ModBlocks.OSMIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BLOCK.get()), has(ModBlocks.OSMIUM_BLOCK.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OSMIUM_NUGGET.get(), 9)
                .requires(ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":osmium_nuggets_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OSMIUM_INGOT.get())
                .pattern("NNN").pattern("NNN").pattern("NNN")
                .define('N', ModItems.OSMIUM_NUGGET.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_NUGGET.get()), has(ModItems.OSMIUM_NUGGET.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":osmium_ingot_from_nuggets");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.OSMIUM_BARS.get(), 16)
                .pattern("III").pattern("III")
                .define('I', ModItems.OSMIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.OSMIUM_SPOTLIGHT.get())
                .pattern("III").pattern("TNT").pattern("NNN")
                .define('I', ModItems.OSMIUM_INGOT.get())
                .define('N', ModItems.OSMIUM_NUGGET.get())
                .define('T', Items.TORCH)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICKS.get(), 4)
                .pattern("ID").pattern("DI")
                .define('I', ModItems.OSMIUM_INGOT.get())
                .define('D', Items.DEEPSLATE)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_STAIRS.get(), 4)
                .pattern("B  ").pattern("BB ").pattern("BBB")
                .define('B', ModBlocks.OSMIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_SLAB.get(), 6)
                .pattern("BBB")
                .define('B', ModBlocks.OSMIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_WALL.get(), 6)
                .pattern("BBB").pattern("BBB")
                .define('B', ModBlocks.OSMIUM_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_OSMIUM_BRICKS.get(), 1)
                .pattern("S").pattern("S")
                .define('S', ModBlocks.OSMIUM_BRICK_SLAB.get())
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICK_SLAB.get()), has(ModBlocks.OSMIUM_BRICK_SLAB.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModBlocks.LEAP_RAIL.get(), 6)
                .pattern("O O")
                .pattern("OSO")
                .pattern("ORO")
                .define('O', ModItems.OSMIUM_INGOT.get())
                .define('S', Items.STICK)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get()))
                .save(pWriter);

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.OSMIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_STAIRS.get(), 1)
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":osmium_bricks_to_stairs_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.OSMIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_SLAB.get(), 2)
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":osmium_bricks_to_slab_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.OSMIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.OSMIUM_BRICK_WALL.get(), 1)
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":osmium_bricks_to_wall_stonecutter");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.OSMIUM_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_OSMIUM_BRICKS.get(), 1)
                .unlockedBy(getHasName(ModBlocks.OSMIUM_BRICKS.get()), has(ModBlocks.OSMIUM_BRICKS.get())).save(pWriter, ExtraShiny.MOD_ID + ":osmium_bricks_to_chiseled_stonecutter");
    }

    private void buildDamaskRecipes(Consumer<FinishedRecipe> pWriter) {
        smithingDamask(pWriter, Items.DIAMOND_SWORD, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_SWORD.get());
        smithingDamask(pWriter, Items.DIAMOND_PICKAXE, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_PICKAXE.get());
        smithingDamask(pWriter, Items.DIAMOND_AXE, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_AXE.get());
        smithingDamask(pWriter, Items.DIAMOND_SHOVEL, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_SHOVEL.get());
        smithingDamask(pWriter, Items.DIAMOND_HOE, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_HOE.get());

        smithingDamask(pWriter, Items.DIAMOND_HELMET, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_HELMET.get());
        smithingDamask(pWriter, Items.DIAMOND_CHESTPLATE, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_CHESTPLATE.get());
        smithingDamask(pWriter, Items.DIAMOND_LEGGINGS, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_LEGGINGS.get());
        smithingDamask(pWriter, Items.DIAMOND_BOOTS, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_BOOTS.get());
        smithingDamask(pWriter, Items.DIAMOND_HORSE_ARMOR, ModItems.DAMASK_INGOT.get(), ModItems.DAMASK_HORSE_ARMOR.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DAMASK_INGOT.get(), 1)
                .requires(Items.NETHERITE_SCRAP, 4)
                .requires(ModItems.OSMIUM_INGOT.get(), 4)
                .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                .save(pWriter, ExtraShiny.MOD_ID + ":damask_ingot_from_scraps_and_osmium");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DAMASK_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.DAMASK_INGOT.get())
                .unlockedBy(getHasName(ModItems.DAMASK_INGOT.get()), has(ModItems.DAMASK_INGOT.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DAMASK_INGOT.get(), 9)
                .requires(ModBlocks.DAMASK_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.DAMASK_BLOCK.get()), has(ModBlocks.DAMASK_BLOCK.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DAMASK_NUGGET.get(), 9)
                .requires(ModItems.DAMASK_INGOT.get())
                .unlockedBy(getHasName(ModItems.DAMASK_INGOT.get()), has(ModItems.DAMASK_INGOT.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":damask_nuggets_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DAMASK_INGOT.get())
                .pattern("NNN").pattern("NNN").pattern("NNN")
                .define('N', ModItems.DAMASK_NUGGET.get())
                .unlockedBy(getHasName(ModItems.DAMASK_NUGGET.get()), has(ModItems.DAMASK_NUGGET.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":damask_ingot_from_nuggets");
    }

    private void buildCimmerianRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CIMMERIAN_HELMET.get())
                .pattern("LLL").pattern("L L")
                .define('L', ModItems.ANCIENT_LATTICE.get())
                .unlockedBy(getHasName(ModItems.ANCIENT_LATTICE.get()), has(ModItems.ANCIENT_LATTICE.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CIMMERIAN_CHESTPLATE.get())
                .pattern("L L").pattern("LLL").pattern("LLL")
                .define('L', ModItems.ANCIENT_LATTICE.get())
                .unlockedBy(getHasName(ModItems.ANCIENT_LATTICE.get()), has(ModItems.ANCIENT_LATTICE.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CIMMERIAN_LEGGINGS.get())
                .pattern("LLL").pattern("L L").pattern("L L")
                .define('L', ModItems.ANCIENT_LATTICE.get())
                .unlockedBy(getHasName(ModItems.ANCIENT_LATTICE.get()), has(ModItems.ANCIENT_LATTICE.get())).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CIMMERIAN_BOOTS.get())
                .pattern("L L").pattern("L L")
                .define('L', ModItems.ANCIENT_LATTICE.get())
                .unlockedBy(getHasName(ModItems.ANCIENT_LATTICE.get()), has(ModItems.ANCIENT_LATTICE.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANCIENT_LATTICE.get(), 2)
                .requires(Items.ECHO_SHARD, 2)
                .requires(ModItems.OSMIUM_INGOT.get(), 2)
                .requires(Items.PHANTOM_MEMBRANE, 3)
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                .save(pWriter, ExtraShiny.MOD_ID + ":ancient_lattice_shapeless");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CIMMERIAN_BLOCK.get())
                .pattern("SSS").pattern("SSS").pattern("SSS")
                .define('S', ModItems.ANCIENT_LATTICE.get())
                .unlockedBy(getHasName(ModItems.ANCIENT_LATTICE.get()), has(ModItems.ANCIENT_LATTICE.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANCIENT_LATTICE.get(), 9)
                .requires(ModBlocks.CIMMERIAN_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.CIMMERIAN_BLOCK.get()), has(ModBlocks.CIMMERIAN_BLOCK.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":ancient_lattice_from_cimmerian_block");
    }

    private void buildLabradoriteRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, LABRADORITE_SMELTABLES, RecipeCategory.MISC, ModItems.LABRADORITE.get(), 0.5f, 200, "labradorite");
        oreBlasting(pWriter, LABRADORITE_SMELTABLES, RecipeCategory.MISC, ModItems.LABRADORITE.get(), 0.5f, 100, "labradorite");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LABRADORITE_BLOCK.get())
                .pattern("LLL").pattern("LLL").pattern("LLL")
                .define('L', ModItems.LABRADORITE.get())
                .unlockedBy(getHasName(ModItems.LABRADORITE.get()), has(ModItems.LABRADORITE.get())).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LABRADORITE.get(), 9)
                .requires(ModBlocks.LABRADORITE_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BLOCK.get()), has(ModBlocks.LABRADORITE_BLOCK.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICKS.get(), 1)
                .pattern("LL").pattern("LL")
                .define('L', ModItems.LABRADORITE.get())
                .unlockedBy(getHasName(ModItems.LABRADORITE.get()), has(ModItems.LABRADORITE.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_SLAB.get(), 6)
                .pattern("BBB")
                .define('B', ModBlocks.LABRADORITE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_STAIRS.get(), 4)
                .pattern("B  ").pattern("BB ").pattern("BBB")
                .define('B', ModBlocks.LABRADORITE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_WALL.get(), 6)
                .pattern("BBB").pattern("BBB")
                .define('B', ModBlocks.LABRADORITE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_PILLAR.get(), 2)
                .pattern("B").pattern("B")
                .define('B', ModBlocks.LABRADORITE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.LABRADORITE_LAMP.get())
                .pattern(" L ").pattern("LGL").pattern(" L ")
                .define('L', ModItems.LABRADORITE.get()).define('G', Items.GLOWSTONE)
                .unlockedBy(getHasName(ModItems.LABRADORITE.get()), has(ModItems.LABRADORITE.get())).save(pWriter);

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.LABRADORITE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_SLAB.get(), 2)
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":labradorite_bricks_to_slab");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.LABRADORITE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_STAIRS.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":labradorite_bricks_to_stairs");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.LABRADORITE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_BRICK_WALL.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":labradorite_bricks_to_wall");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.LABRADORITE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.LABRADORITE_PILLAR.get())
                .unlockedBy(getHasName(ModBlocks.LABRADORITE_BRICKS.get()), has(ModBlocks.LABRADORITE_BRICKS.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":labradorite_bricks_to_pillar");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.AURORAL_ARROW.get(), 4)
                .requires(Items.ARROW)
                .requires(ModItems.LABRADORITE.get())
                .unlockedBy(getHasName(ModItems.LABRADORITE.get()), has(ModItems.LABRADORITE.get())).save(pWriter);
    }

    private void buildMiscRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RADAR.get())
                .pattern(" X ").pattern("XRX").pattern(" X ")
                .define('X', ModItems.VANADIUM_INGOT.get()).define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.VANADIUM_INGOT.get()), has(ModItems.VANADIUM_INGOT.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RECALIBRATED_RADAR.get())
                .pattern(" E ").pattern("ERE").pattern(" E ")
                .define('E', Items.ECHO_SHARD).define('R', ModItems.RADAR.get())
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD)).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REFINING_TABLE.get())
                .pattern("LLL").pattern("BOB").pattern("OOO")
                .define('L', Items.LAPIS_LAZULI)
                .define('B', ModItems.LABRADORITE.get())
                .define('O', Items.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.LABRADORITE.get()), has(ModItems.LABRADORITE.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEMORY_ALLOY.get(), 1)
                .pattern("VOV").pattern("OQO").pattern("VOV")
                .define('V', ModItems.VANADIUM_NUGGET.get())
                .define('O', ModItems.OSMIUM_NUGGET.get())
                .define('Q', Items.QUARTZ)
                .unlockedBy(getHasName(ModItems.VANADIUM_NUGGET.get()), has(ModItems.VANADIUM_NUGGET.get())).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MOONDIAL.get())
                .pattern(" O ").pattern("ORO").pattern(" O ")
                .define('O', ModItems.OSMIUM_INGOT.get()).define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.OSMIUM_INGOT.get()), has(ModItems.OSMIUM_INGOT.get())).save(pWriter);
    }

    private void buildTemplatesAndTrims(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("#S#").pattern("#C#").pattern("###")
                .define('#', ModItems.VANADIUM_INGOT.get())
                .define('S', ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get())
                .define('C', Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()),
                        has(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(pWriter, ExtraShiny.MOD_ID + ":vanadium_template_duplication");

        SmithingTrimRecipeBuilder.smithingTrim(
                        Ingredient.of(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemTags.TRIMMABLE_ARMOR),
                        Ingredient.of(ItemTags.TRIM_MATERIALS),
                        RecipeCategory.MISC
                )
                .unlocks("has_memory_template", has(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get()))
                .save(pWriter, new ResourceLocation(ExtraShiny.MOD_ID, "memory_trim"));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer,  ExtraShiny.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    private void smithingDamask(Consumer<FinishedRecipe> writer, Item base, Item addition, Item result) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        RecipeCategory.MISC,
                        result
                )
                .unlocks("has_" + getItemName(addition), has(addition))
                .save(writer, ExtraShiny.MOD_ID + ":damask_upgrade_" + getItemName(result));
    }

    private void smithingVanadium(Consumer<FinishedRecipe> writer, Item base, Item addition, Item result) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        RecipeCategory.MISC,
                        result
                )
                .unlocks("has_" + getItemName(addition), has(addition))
                .save(writer, ExtraShiny.MOD_ID + ":vanadium_upgrade_" + getItemName(result));
    }
}