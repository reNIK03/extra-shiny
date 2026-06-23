package net.r_nik.extrashiny.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.block.IngotBlock;
import net.r_nik.extrashiny.block.IngotLayer;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.VANADIUM_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_VANADIUM_BLOCK.get());

        this.dropSelf(ModBlocks.LABRADORITE_BLOCK.get());
        this.dropSelf(ModBlocks.LABRADORITE_BRICKS.get());
        this.dropSelf(ModBlocks.LABRADORITE_PILLAR.get());
        this.dropSelf(ModBlocks.LABRADORITE_LAMP.get());
        this.dropSelf(ModBlocks.LABRADORITE_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.LABRADORITE_BRICK_SLAB.get());
        this.dropSelf(ModBlocks.LABRADORITE_BRICK_WALL.get());
        this.dropSelf(ModBlocks.RAW_OSMIUM_BLOCK.get());

        this.add(ModBlocks.VANADIUM_INGOT_BLOCK.get(), block -> createIngotBlockDrop(block, ModItems.VANADIUM_INGOT.get()));
        this.add(ModBlocks.OSMIUM_INGOT_BLOCK.get(), block -> createIngotBlockDrop(block, ModItems.OSMIUM_INGOT.get()));
        this.add(ModBlocks.DAMASK_INGOT_BLOCK.get(), block -> createIngotBlockDrop(block, ModItems.DAMASK_INGOT.get()));

        this.dropSelf(ModBlocks.OSMIUM_BLOCK.get());

        this.dropSelf(ModBlocks.LEAP_RAIL.get());

        this.dropSelf(ModBlocks.CIMMERIAN_BLOCK.get());

        this.dropSelf(ModBlocks.DAMASK_BLOCK.get());

        this.dropSelf(ModBlocks.REFINING_TABLE.get());
        this.dropSelf(ModBlocks.VANADIUM_BARS.get());

        this.dropSelf(ModBlocks.OSMIUM_BARS.get());

        this.dropSelf(ModBlocks.OSMIUM_SPOTLIGHT.get());

        this.dropSelf(ModBlocks.VANADIUM_BRICKS.get());
        this.dropSelf(ModBlocks.VANADIUM_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.VANADIUM_BRICK_SLAB.get());
        this.dropSelf(ModBlocks.VANADIUM_BRICK_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_VANADIUM_BRICKS.get());

        this.dropSelf(ModBlocks.OSMIUM_BRICKS.get());
        this.dropSelf(ModBlocks.OSMIUM_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.OSMIUM_BRICK_SLAB.get());
        this.dropSelf(ModBlocks.OSMIUM_BRICK_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_OSMIUM_BRICKS.get());


        this.add(ModBlocks.VANADIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.VANADIUM_ORE.get(), ModItems.RAW_VANADIUM.get()));
        this.add(ModBlocks.DEEPSLATE_VANADIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_VANADIUM_ORE.get(), ModItems.RAW_VANADIUM.get()));

        this.add(ModBlocks.LABRADORITE_ORE.get(),
                block -> createOreDrop(ModBlocks.LABRADORITE_ORE.get(), ModItems.LABRADORITE.get()));
        this.add(ModBlocks.DEEPSLATE_LABRADORITE_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_LABRADORITE_ORE.get(), ModItems.LABRADORITE.get()));

        this.add(ModBlocks.OSMIUM_ORE.get(), block -> createOreDrop(ModBlocks.OSMIUM_ORE.get(), ModItems.RAW_OSMIUM.get()));
        this.add(ModBlocks.DEEPSLATE_OSMIUM_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_OSMIUM_ORE.get(), ModItems.RAW_OSMIUM.get()));

        this.add(ModBlocks.SPOTTED_BLACKSTONE.get(), block -> createSpottedBlackstoneDrop(block));

    }

    protected LootTable.Builder createOreDrop(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,1)))
                                .apply(ApplyBonusCount.addOreBonusCount(registries.lookup(Registries.ENCHANTMENT).get().getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createSpottedBlackstoneDrop(Block block) {
        return createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block,
                        LootItem.lootTableItem(ModItems.OSMIUM_NUGGET.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(registries.lookup(Registries.ENCHANTMENT).get().getOrThrow(Enchantments.FORTUNE)))));

    }

    protected LootTable.Builder createIngotBlockDrop(Block block, Item drop) {
        LootTable.Builder builder = LootTable.lootTable();
        for (int layers = 0; layers <= 3; layers++) {
            for (IngotLayer topIngot : IngotLayer.values()) {
                int count = 2 * layers + (topIngot == IngotLayer.BOTH ? 2 : 1);

                builder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drop)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly((float) count))))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(IngotBlock.LAYERS, layers)
                                        .hasProperty(IngotBlock.TOP_INGOT, topIngot)))
                );
            }
        }
        return builder;
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .collect(java.util.stream.Collectors.toList());
    }

}
