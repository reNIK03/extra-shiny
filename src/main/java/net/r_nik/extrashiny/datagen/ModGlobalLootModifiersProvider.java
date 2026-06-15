package net.r_nik.extrashiny.datagen;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.loot.AddItemModifier;
import net.r_nik.extrashiny.loot.ReplaceItemModifier;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, ExtraShiny.MOD_ID);
    }

    @Override
    protected void start() {
        add("vanadium_upgrade_from_dungeons", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.50f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_jungle_temples", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.80f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_desert_temples", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/desert_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.80f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_mineshafts", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/abandoned_mineshaft")).build(),
                LootItemRandomChanceCondition.randomChance(0.70f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_shipwreck", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.80f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_weaponsmith", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/village/village_toolsmith")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_toolsmith", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/village/village_weaponsmith")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_underwater_ruin_big", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/underwater_ruin_big")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("vanadium_upgrade_from_underwater_ruin_small", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/underwater_ruin_small")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));


        add("vanadium_upgrade_from_buried_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/buried_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.90f).build()
        }, ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get()));

        add("replace_gold_ingot_desert_temple", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/desert_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_ingot_jungle_temple", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_ingot_simple_dungeon", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.15f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_ingot_mineshaft", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/abandoned_mineshaft")).build(),
                LootItemRandomChanceCondition.randomChance(0.15f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_ingot_shipwreck", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.25f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_nugget_shipwreck", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.25f).build()
        }, net.minecraft.world.item.Items.GOLD_NUGGET, ModItems.OSMIUM_NUGGET.get()));

        add("replace_gold_ingot_buried_treasure", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/buried_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.25f).build()
        }, net.minecraft.world.item.Items.GOLD_INGOT, ModItems.OSMIUM_INGOT.get()));

        add("replace_gold_nugget_underwater_ruin_big", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/underwater_ruin_big")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, net.minecraft.world.item.Items.GOLD_NUGGET, ModItems.OSMIUM_NUGGET.get()));

        add("replace_gold_nugget_underwater_ruin_small", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/underwater_ruin_small")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()
        }, net.minecraft.world.item.Items.GOLD_NUGGET, ModItems.OSMIUM_NUGGET.get()));


        add("memory_trim_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/ancient_city")).build(),
                LootItemRandomChanceCondition.randomChance(0.025f).build()
        }, ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get()));

        add("replace_golden_horse_armor_simple_dungeon", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_desert_temple", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/desert_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_jungle_temple", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_nether_bridge", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_stronghold_corridor", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/stronghold_corridor")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_stronghold_crossing", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/stronghold_crossing")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_end_city", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/end_city_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_village_weaponsmith", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/village/village_weaponsmith")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));

        add("replace_golden_horse_armor_ruined_portal", new ReplaceItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft:chests/ruined_portal")).build(),
                LootItemRandomChanceCondition.randomChance(0.30f).build()
        }, net.minecraft.world.item.Items.GOLDEN_HORSE_ARMOR, ModItems.OSMIUM_HORSE_ARMOR.get()));


    }
}
