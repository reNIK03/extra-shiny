package net.r_nik.extrashiny.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.item.MoondialItem;


import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraShiny.MOD_ID);

    public static final RegistryObject<Item> VANADIUM_INGOT = ITEMS.register("vanadium_ingot",
            () -> new ItemNameBlockItem(ModBlocks.VANADIUM_INGOT_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> RAW_VANADIUM = ITEMS.register("raw_vanadium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_NUGGET = ITEMS.register("vanadium_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LABRADORITE = ITEMS.register("labradorite", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> VANADIUM_UPGRADE_SMITHING_TEMPLATE =
            ITEMS.register("vanadium_upgrade_smithing_template", () -> new SmithingTemplateItem(
                    createAppliesToText(),
                    createIngredientsText(),
                    Component.translatable("upgrade." + ExtraShiny.MOD_ID + ".vanadium_upgrade")
                            .withStyle(ChatFormatting.GRAY), // <-- GREY TITLE
                    Component.translatable("item.extrashiny.vanadium_upgrade_smithing_template.base_slot_description"),
                    Component.translatable("item.extrashiny.vanadium_upgrade_smithing_template.addition_slot_description"),
                    createBaseSlotIcons(),
                    createAdditionSlotIcons()
            ));

    public static final RegistryObject<Item> VANADIUM_PARTISAN = ITEMS.register("vanadium_partisan", () -> new VanadiumPartisanItem(new Item.Properties().stacksTo(1).durability(250)));

    public static final RegistryObject<Item> VANADIUM_REPEATER = ITEMS.register("vanadium_repeater", () -> new VanadiumRepeaterItem(new Item.Properties().stacksTo(1).durability(465)));

    public static final RegistryObject<Item> RADAR = ITEMS.register("radar", () -> new RadarItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MOONDIAL = ITEMS.register("moondial", () -> new MoondialItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ORE_TRACKER = ITEMS.register("ore_tracker", () -> new OreTrackerItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> VANADIUM_SWORD = ITEMS.register("vanadium_sword", () -> new SwordItem(ModToolTiers.VANADIUM, 3, -2.2f, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_PICKAXE = ITEMS.register("vanadium_pickaxe", () -> new PickaxeItem(ModToolTiers.VANADIUM, 1, -2.6f, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_AXE = ITEMS.register("vanadium_axe", () -> new AxeItem(ModToolTiers.VANADIUM, 5, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_SHOVEL = ITEMS.register("vanadium_shovel", () -> new ShovelItem(ModToolTiers.VANADIUM, 1.5f, -3f, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_HOE = ITEMS.register("vanadium_hoe", () -> new HoeItem(ModToolTiers.VANADIUM, -3, 0f, new Item.Properties()));

    public static final RegistryObject<Item> VANADIUM_HELMET = ITEMS.register("vanadium_helmet", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_CHESTPLATE = ITEMS.register("vanadium_chestplate", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_LEGGINGS = ITEMS.register("vanadium_leggings", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> VANADIUM_BOOTS = ITEMS.register("vanadium_boots", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> VANADIUM_HORSE_ARMOR = ITEMS.register("vanadium_horse_armor", () -> new HorseArmorItem(10,"vanadium", new Item.Properties()));


    public static final RegistryObject<Item> VANADIUM_GOLEM_SPAWN_EGG = ITEMS.register("vanadium_golem_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.VANADIUM_GOLEM, 0x908789, 0x51515a,
            new Item.Properties()));


    public static final RegistryObject<Item> ENFORCER_SPAWN_EGG = ITEMS.register("enforcer_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.ENFORCER, 0x034150, 0xd1d6b6,
            new Item.Properties()));


    private static Component createAppliesToText() {
        return Component.translatable("item.extrashiny.vanadium_upgrade_smithing_template.applies_to")
                .withStyle(ChatFormatting.BLUE);
    }

    private static Component createIngredientsText() {
        return Component.translatable("item.extrashiny.vanadium_upgrade_smithing_template.ingredients")
                .withStyle(ChatFormatting.BLUE);
    }

    private static List<ResourceLocation> createBaseSlotIcons() {
        return List.of(
                new ResourceLocation("item/empty_armor_slot_helmet"),
                new ResourceLocation("item/empty_armor_slot_chestplate"),
                new ResourceLocation("item/empty_armor_slot_leggings"),
                new ResourceLocation("item/empty_armor_slot_boots"),
                new ResourceLocation("item/empty_slot_sword"),
                new ResourceLocation("item/empty_slot_pickaxe"),
                new ResourceLocation("item/empty_slot_axe"),
                new ResourceLocation("item/empty_slot_shovel"),
                new ResourceLocation("item/empty_slot_hoe"),

                new ResourceLocation(ExtraShiny.MOD_ID,"item/empty_slot_horse_armor"),
                new ResourceLocation(ExtraShiny.MOD_ID,"item/empty_slot_trident"),
                new ResourceLocation(ExtraShiny.MOD_ID,"item/empty_slot_crossbow")
        );
    }

    private static List<ResourceLocation> createAdditionSlotIcons() {
        return List.of(
                new ResourceLocation("item/empty_slot_ingot")
        );
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


    public static final RegistryObject<Item> RAW_OSMIUM = ITEMS.register("raw_osmium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> OSMIUM_INGOT = ITEMS.register("osmium_ingot",
            () -> new ItemNameBlockItem(ModBlocks.OSMIUM_INGOT_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> OSMIUM_HELMET = ITEMS.register("osmium_helmet", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_CHESTPLATE = ITEMS.register("osmium_chestplate", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_LEGGINGS = ITEMS.register("osmium_leggings", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_BOOTS = ITEMS.register("osmium_boots", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> OSMIUM_SWORD = ITEMS.register("osmium_sword", () -> new SwordItem(ModToolTiers.OSMIUM, 1, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_PICKAXE = ITEMS.register("osmium_pickaxe", () -> new PickaxeItem(ModToolTiers.OSMIUM, 0, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_AXE = ITEMS.register("osmium_axe", () -> new AxeItem(ModToolTiers.OSMIUM, 4, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_SHOVEL = ITEMS.register("osmium_shovel", () -> new ShovelItem(ModToolTiers.OSMIUM, 0, -3f, new Item.Properties()));
    public static final RegistryObject<Item> OSMIUM_HOE = ITEMS.register("osmium_hoe", () -> new HoeItem(ModToolTiers.OSMIUM, -1, -3f, new Item.Properties()));

    public static final RegistryObject<Item> MEMORY_ALLOY = ITEMS.register("memory_alloy",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AURORAL_ARROW = ITEMS.register("auroral_arrow",
            () -> new AuroralArrowItem(new Item.Properties()));


    public static final RegistryObject<Item> LEAP_RAIL = ITEMS.register("leap_rail",
            () -> new BlockItem(ModBlocks.LEAP_RAIL.get(), new Item.Properties()));


    public static final RegistryObject<Item> MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("memory_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(
                    new ResourceLocation(ExtraShiny.MOD_ID, "memory")
            )
    );

    public static final RegistryObject<Item> RECALIBRATED_RADAR =
            ITEMS.register("recalibrated_radar", () -> new RecalibratedRadarItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BULWARK = ITEMS.register("bulwark",
            () -> new BulwarkItem(new Item.Properties().durability(1008)));

    public static final RegistryObject<Item> OSMIUM_SPOTLIGHT = ITEMS.register("osmium_spotlight",
            () -> new BlockItem(ModBlocks.OSMIUM_SPOTLIGHT.get(), new Item.Properties()));

    public static final RegistryObject<Item> ANCIENT_LATTICE = ITEMS.register("ancient_lattice", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> CIMMERIAN_HELMET = ITEMS.register("cimmerian_helmet", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CIMMERIAN_CHESTPLATE = ITEMS.register("cimmerian_chestplate", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> CIMMERIAN_LEGGINGS = ITEMS.register("cimmerian_leggings", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> CIMMERIAN_BOOTS = ITEMS.register("cimmerian_boots", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.BOOTS, new Item.Properties()));



    public static final RegistryObject<Item> DAMASK_INGOT = ITEMS.register("damask_ingot",
            () -> new ItemNameBlockItem(ModBlocks.DAMASK_INGOT_BLOCK.get(), new Item.Properties().fireResistant()));


    public static final RegistryObject<Item> OSMIUM_NUGGET = ITEMS.register("osmium_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DAMASK_NUGGET = ITEMS.register("damask_nugget",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DAMASK_SWORD = ITEMS.register("damask_sword", () -> new SwordItem(ModToolTiers.DAMASK, 3, -2.4F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_SHOVEL = ITEMS.register("damask_shovel", () -> new ShovelItem(ModToolTiers.DAMASK, 1.5F, -3.0F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_PICKAXE = ITEMS.register("damask_pickaxe", () -> new PickaxeItem(ModToolTiers.DAMASK, 1, -2.8F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_AXE = ITEMS.register("damask_axe", () -> new AxeItem(ModToolTiers.DAMASK, 5.0F, -3.0F, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_HOE = ITEMS.register("damask_hoe", () -> new HoeItem(ModToolTiers.DAMASK, -3, 0.0F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DAMASK_HELMET = ITEMS.register("damask_helmet", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_CHESTPLATE = ITEMS.register("damask_chestplate", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_LEGGINGS = ITEMS.register("damask_leggings", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> DAMASK_BOOTS = ITEMS.register("damask_boots", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DAMASK_HORSE_ARMOR = ITEMS.register(
            "damask_horse_armor",
            () -> new DamaskHorseArmorItem(
                    18,
                    "damask",
                    new Item.Properties().fireResistant()
            )
    );


    public static final RegistryObject<Item> OSMIUM_HORSE_ARMOR = ITEMS.register(
            "osmium_horse_armor",
            () -> new HorseArmorItem(
                    14,
                    "osmium",
                    new Item.Properties()
            )
    );

}
