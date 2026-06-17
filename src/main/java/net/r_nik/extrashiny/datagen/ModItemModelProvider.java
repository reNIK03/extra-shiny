package net.r_nik.extrashiny.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.item.ModItems;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>,Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = ExtraShiny.MOD_ID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + itemRegistryObject.getId().getPath();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraShiny.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.VANADIUM_INGOT);
        simpleItem(ModItems.VANADIUM_NUGGET);
        simpleItem(ModItems.RAW_VANADIUM);

        simpleItem(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE);

        handheldItem(ModItems.VANADIUM_SWORD);
        handheldItem(ModItems.VANADIUM_PICKAXE);
        handheldItem(ModItems.VANADIUM_AXE);
        handheldItem(ModItems.VANADIUM_SHOVEL);
        handheldItem(ModItems.VANADIUM_HOE);

        trimmedArmorItem(ModItems.VANADIUM_HELMET);
        trimmedArmorItem(ModItems.VANADIUM_CHESTPLATE);
        trimmedArmorItem(ModItems.VANADIUM_LEGGINGS);
        trimmedArmorItem(ModItems.VANADIUM_BOOTS);

        HorseArmorItem(ModItems.VANADIUM_HORSE_ARMOR);
        HorseArmorItem(ModItems.OSMIUM_HORSE_ARMOR);
        HorseArmorItem(ModItems.DAMASK_HORSE_ARMOR);

        simpleItem(ModItems.LABRADORITE);

        withExistingParent(ModItems.VANADIUM_GOLEM_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ENFORCER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        blockItem(ModBlocks.LABRADORITE_BRICKS);
        blockItem(ModBlocks.LABRADORITE_BRICK_STAIRS);
        blockItem(ModBlocks.LABRADORITE_BRICK_SLAB);

        blockItem(ModBlocks.OSMIUM_ORE);
        blockItem(ModBlocks.DEEPSLATE_OSMIUM_ORE);
        blockItem(ModBlocks.SPOTTED_BLACKSTONE);

        wallBlockItem(
                ModBlocks.LABRADORITE_BRICK_WALL,
                ModBlocks.LABRADORITE_BRICKS
        );

        simpleItem(ModItems.MEMORY_ALLOY);
        simpleItem(ModItems.OSMIUM_INGOT);

        trimmedArmorItem(ModItems.OSMIUM_HELMET);
        trimmedArmorItem(ModItems.OSMIUM_CHESTPLATE);
        trimmedArmorItem(ModItems.OSMIUM_LEGGINGS);
        trimmedArmorItem(ModItems.OSMIUM_BOOTS);

        handheldItem(ModItems.OSMIUM_SWORD);
        handheldItem(ModItems.OSMIUM_PICKAXE);
        handheldItem(ModItems.OSMIUM_AXE);
        handheldItem(ModItems.OSMIUM_SHOVEL);
        handheldItem(ModItems.OSMIUM_HOE);

        blockItem(ModBlocks.VANADIUM_BRICKS);
        blockItem(ModBlocks.VANADIUM_BRICK_STAIRS);
        blockItem(ModBlocks.VANADIUM_BRICK_SLAB);
        wallBlockItem(
                ModBlocks.VANADIUM_BRICK_WALL,
                ModBlocks.VANADIUM_BRICKS
        );
        blockItem(ModBlocks.CHISELED_VANADIUM_BRICKS);

        blockItem(ModBlocks.OSMIUM_BRICKS);
        blockItem(ModBlocks.OSMIUM_BRICK_STAIRS);
        blockItem(ModBlocks.OSMIUM_BRICK_SLAB);
        wallBlockItem(
                ModBlocks.OSMIUM_BRICK_WALL,
                ModBlocks.OSMIUM_BRICKS
        );
        blockItem(ModBlocks.CHISELED_OSMIUM_BRICKS);

        simpleItem(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE);

        simpleItem(ModItems.ANCIENT_LATTICE);

        simpleItem(ModItems.RAW_OSMIUM);


        trimmedArmorItem(ModItems.CIMMERIAN_HELMET);
        trimmedArmorItem(ModItems.CIMMERIAN_CHESTPLATE);
        trimmedArmorItem(ModItems.CIMMERIAN_LEGGINGS);
        trimmedArmorItem(ModItems.CIMMERIAN_BOOTS);



        simpleItem(ModItems.DAMASK_INGOT);

        simpleItem(ModItems.OSMIUM_NUGGET);
        simpleItem(ModItems.DAMASK_NUGGET);


        trimmedArmorItem(ModItems.DAMASK_HELMET);
        trimmedArmorItem(ModItems.DAMASK_CHESTPLATE);
        trimmedArmorItem(ModItems.DAMASK_LEGGINGS);
        trimmedArmorItem(ModItems.DAMASK_BOOTS);

        handheldItem(ModItems.DAMASK_SWORD);
        handheldItem(ModItems.DAMASK_PICKAXE);
        handheldItem(ModItems.DAMASK_AXE);
        handheldItem(ModItems.DAMASK_SHOVEL);
        handheldItem(ModItems.DAMASK_HOE);
    }

    private void entityItem(RegistryObject<Item> item) {
        getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(ExtraShiny.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder HorseArmorItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(ExtraShiny.MOD_ID, "item/" + item.getId().getPath()));
    }


    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld")).texture("layer0", new ResourceLocation(ExtraShiny.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void blockItem(RegistryObject<? extends net.minecraft.world.level.block.Block> block) {
        withExistingParent(
                block.getId().getPath(),
                modLoc("block/" + block.getId().getPath())
        );
    }

    private void wallBlockItem(
            RegistryObject<? extends net.minecraft.world.level.block.Block> wall,
            RegistryObject<? extends net.minecraft.world.level.block.Block> baseBlock
    ) {
        withExistingParent(
                wall.getId().getPath(),
                mcLoc("block/wall_inventory")
        ).texture(
                "wall",
                modLoc("block/" + baseBlock.getId().getPath())
        );
    }

}
