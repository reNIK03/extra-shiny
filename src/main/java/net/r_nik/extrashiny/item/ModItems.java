package net.r_nik.extrashiny.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.item.MoondialItem;


import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ExtraShiny.MOD_ID);

    public static final DeferredHolder<Item, Item> VANADIUM_INGOT = ITEMS.register("vanadium_ingot",
            () -> new ItemNameBlockItem(ModBlocks.VANADIUM_INGOT_BLOCK.get(), new Item.Properties()));


    public static final DeferredHolder<Item, Item> RAW_VANADIUM = ITEMS.register("raw_vanadium", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> VANADIUM_NUGGET = ITEMS.register("vanadium_nugget", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> LABRADORITE = ITEMS.register("labradorite", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> VANADIUM_UPGRADE_SMITHING_TEMPLATE =
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

    public static final DeferredHolder<Item, Item> VANADIUM_PARTISAN = ITEMS.register("vanadium_partisan", () -> new VanadiumPartisanItem(new Item.Properties().stacksTo(1).durability(250)));

    public static final DeferredHolder<Item, Item> VANADIUM_REPEATER = ITEMS.register("vanadium_repeater", () -> new VanadiumRepeaterItem(new Item.Properties().stacksTo(1).durability(465)));

    public static final DeferredHolder<Item, Item> RADAR = ITEMS.register("radar", () -> new RadarItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> MOONDIAL = ITEMS.register("moondial", () -> new MoondialItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> ORE_TRACKER = ITEMS.register("ore_tracker", () -> new OreTrackerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> VANADIUM_SWORD = ITEMS.register("vanadium_sword", () -> new SwordItem(ModToolTiers.VANADIUM, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.VANADIUM, 3, -2.2f))));
    public static final DeferredHolder<Item, Item> VANADIUM_PICKAXE = ITEMS.register("vanadium_pickaxe", () -> new PickaxeItem(ModToolTiers.VANADIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.VANADIUM, 1, -2.6f))));
    public static final DeferredHolder<Item, Item> VANADIUM_AXE = ITEMS.register("vanadium_axe", () -> new AxeItem(ModToolTiers.VANADIUM, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.VANADIUM, 5, -2.8f))));
    public static final DeferredHolder<Item, Item> VANADIUM_SHOVEL = ITEMS.register("vanadium_shovel", () -> new ShovelItem(ModToolTiers.VANADIUM, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.VANADIUM, 1.5f, -3f))));
    public static final DeferredHolder<Item, Item> VANADIUM_HOE = ITEMS.register("vanadium_hoe", () -> new HoeItem(ModToolTiers.VANADIUM, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.VANADIUM, -3, 0f))));

    public static final DeferredHolder<Item, Item> VANADIUM_HELMET = ITEMS.register("vanadium_helmet", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(35))));
    public static final DeferredHolder<Item, Item> VANADIUM_CHESTPLATE = ITEMS.register("vanadium_chestplate", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(35))));
    public static final DeferredHolder<Item, Item> VANADIUM_LEGGINGS = ITEMS.register("vanadium_leggings", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(35))));
    public static final DeferredHolder<Item, Item> VANADIUM_BOOTS = ITEMS.register("vanadium_boots", () -> new ArmorItem(ModArmorMaterials.VANADIUM, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(35))));


    public static final DeferredHolder<Item, Item> VANADIUM_HORSE_ARMOR = ITEMS.register("vanadium_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.VANADIUM, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties()));


    public static final DeferredHolder<Item, Item> VANADIUM_GOLEM_SPAWN_EGG = ITEMS.register("vanadium_golem_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.VANADIUM_GOLEM, 0x908789, 0x51515a,
            new Item.Properties()));


    public static final DeferredHolder<Item, Item> ENFORCER_SPAWN_EGG = ITEMS.register("enforcer_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.ENFORCER, 0x034150, 0xd1d6b6,
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
                ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),
                ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),

                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID,"item/empty_slot_horse_armor"),
                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID,"item/empty_slot_trident"),
                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID,"item/empty_slot_crossbow")
        );
    }

    private static List<ResourceLocation> createAdditionSlotIcons() {
        return List.of(
                ResourceLocation.withDefaultNamespace("item/empty_slot_ingot")
        );
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


    public static final DeferredHolder<Item, Item> RAW_OSMIUM = ITEMS.register("raw_osmium",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> OSMIUM_INGOT = ITEMS.register("osmium_ingot",
            () -> new ItemNameBlockItem(ModBlocks.OSMIUM_INGOT_BLOCK.get(), new Item.Properties()));


    public static final DeferredHolder<Item, Item> OSMIUM_HELMET = ITEMS.register("osmium_helmet", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(11))));
    public static final DeferredHolder<Item, Item> OSMIUM_CHESTPLATE = ITEMS.register("osmium_chestplate", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(11))));
    public static final DeferredHolder<Item, Item> OSMIUM_LEGGINGS = ITEMS.register("osmium_leggings", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(11))));
    public static final DeferredHolder<Item, Item> OSMIUM_BOOTS = ITEMS.register("osmium_boots", () -> new ArmorItem(ModArmorMaterials.OSMIUM, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(11))));

    public static final DeferredHolder<Item, Item> OSMIUM_SWORD = ITEMS.register("osmium_sword", () -> new SwordItem(ModToolTiers.OSMIUM, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.OSMIUM, 1, -2.4f))));
    public static final DeferredHolder<Item, Item> OSMIUM_PICKAXE = ITEMS.register("osmium_pickaxe", () -> new PickaxeItem(ModToolTiers.OSMIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.OSMIUM, 0, -2.8f))));
    public static final DeferredHolder<Item, Item> OSMIUM_AXE = ITEMS.register("osmium_axe", () -> new AxeItem(ModToolTiers.OSMIUM, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.OSMIUM, 4, -2.8f))));
    public static final DeferredHolder<Item, Item> OSMIUM_SHOVEL = ITEMS.register("osmium_shovel", () -> new ShovelItem(ModToolTiers.OSMIUM, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.OSMIUM, 0, -3f))));
    public static final DeferredHolder<Item, Item> OSMIUM_HOE = ITEMS.register("osmium_hoe", () -> new HoeItem(ModToolTiers.OSMIUM, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.OSMIUM, -1, -3f))));

    public static final DeferredHolder<Item, Item> MEMORY_ALLOY = ITEMS.register("memory_alloy",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> AURORAL_ARROW = ITEMS.register("auroral_arrow",
            () -> new AuroralArrowItem(new Item.Properties()));


    public static final DeferredHolder<Item, Item> LEAP_RAIL = ITEMS.register("leap_rail",
            () -> new BlockItem(ModBlocks.LEAP_RAIL.get(), new Item.Properties()));


    public static final DeferredHolder<Item, Item> MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("memory_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(
                    ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "memory")
            )
    );

    public static final DeferredHolder<Item, Item> RECALIBRATED_RADAR =
            ITEMS.register("recalibrated_radar", () -> new RecalibratedRadarItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> BULWARK = ITEMS.register("bulwark",
            () -> new BulwarkItem(new Item.Properties().durability(1008)));

    public static final DeferredHolder<Item, Item> OSMIUM_SPOTLIGHT = ITEMS.register("osmium_spotlight",
            () -> new BlockItem(ModBlocks.OSMIUM_SPOTLIGHT.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> ANCIENT_LATTICE = ITEMS.register("ancient_lattice", () -> new Item(new Item.Properties()));


    public static final DeferredHolder<Item, Item> CIMMERIAN_HELMET = ITEMS.register("cimmerian_helmet", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(23))));
    public static final DeferredHolder<Item, Item> CIMMERIAN_CHESTPLATE = ITEMS.register("cimmerian_chestplate", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(23))));
    public static final DeferredHolder<Item, Item> CIMMERIAN_LEGGINGS = ITEMS.register("cimmerian_leggings", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(23))));
    public static final DeferredHolder<Item, Item> CIMMERIAN_BOOTS = ITEMS.register("cimmerian_boots", () -> new CimmerianArmorItem(ModArmorMaterials.CIMMERIAN, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(23))));


    public static final DeferredHolder<Item, Item> DAMASK_INGOT = ITEMS.register("damask_ingot",
            () -> new ItemNameBlockItem(ModBlocks.DAMASK_INGOT_BLOCK.get(), new Item.Properties().fireResistant()));


    public static final DeferredHolder<Item, Item> OSMIUM_NUGGET = ITEMS.register("osmium_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> DAMASK_NUGGET = ITEMS.register("damask_nugget",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final DeferredHolder<Item, Item> DAMASK_SWORD = ITEMS.register("damask_sword", () -> new SwordItem(ModToolTiers.DAMASK, new Item.Properties().fireResistant().attributes(SwordItem.createAttributes(ModToolTiers.DAMASK, 3, -2.4F))));
    public static final DeferredHolder<Item, Item> DAMASK_SHOVEL = ITEMS.register("damask_shovel", () -> new ShovelItem(ModToolTiers.DAMASK, new Item.Properties().fireResistant().attributes(ShovelItem.createAttributes(ModToolTiers.DAMASK, 1.5F, -3.0F))));
    public static final DeferredHolder<Item, Item> DAMASK_PICKAXE = ITEMS.register("damask_pickaxe", () -> new PickaxeItem(ModToolTiers.DAMASK, new Item.Properties().fireResistant().attributes(PickaxeItem.createAttributes(ModToolTiers.DAMASK, 1, -2.8F))));
    public static final DeferredHolder<Item, Item> DAMASK_AXE = ITEMS.register("damask_axe", () -> new AxeItem(ModToolTiers.DAMASK, new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(ModToolTiers.DAMASK, 5.0F, -3.0F))));
    public static final DeferredHolder<Item, Item> DAMASK_HOE = ITEMS.register("damask_hoe", () -> new HoeItem(ModToolTiers.DAMASK, new Item.Properties().fireResistant().attributes(HoeItem.createAttributes(ModToolTiers.DAMASK, -3, 0.0F))));

    public static final DeferredHolder<Item, Item> DAMASK_HELMET = ITEMS.register("damask_helmet", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> DAMASK_CHESTPLATE = ITEMS.register("damask_chestplate", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> DAMASK_LEGGINGS = ITEMS.register("damask_leggings", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> DAMASK_BOOTS = ITEMS.register("damask_boots", () -> new DamaskArmorItem(ModArmorMaterials.DAMASK, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    public static final DeferredHolder<Item, Item> DAMASK_HORSE_ARMOR = ITEMS.register("damask_horse_armor",
            () -> new DamaskHorseArmorItem(ModArmorMaterials.DAMASK, new Item.Properties().fireResistant()));


    public static final DeferredHolder<Item, Item> OSMIUM_HORSE_ARMOR = ITEMS.register("osmium_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.OSMIUM, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties()));


}