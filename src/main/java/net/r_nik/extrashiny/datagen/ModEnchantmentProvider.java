package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.r_nik.extrashiny.enchant.ModEnchantments;

public class ModEnchantmentProvider {

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        // ==========================================
        // SERRATION (Fully Data-Driven Damage Bonus!)
        // ==========================================
        context.register(ModEnchantments.SERRATION, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        10, // Weight (10 = Common)
                        5,  // Max Level
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(21, 11),
                        1,  // Anvil Cost
                        EquipmentSlotGroup.MAINHAND
                ))
                // LevelBasedValue.perLevel(base, increment): Matches your "1.0F + (level - 1) * 0.5F" perfectly!
                .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(1.0F, 0.5F)))
                .build(ModEnchantments.SERRATION.location())
        );

        // ==========================================
        // FROST EDGE
        // ==========================================
        context.register(ModEnchantments.FROST_EDGE, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        2, // Weight (2 = Rare)
                        2, // Max Level
                        Enchantment.dynamicCost(10, 20),
                        Enchantment.dynamicCost(60, 0),
                        4,
                        EquipmentSlotGroup.MAINHAND
                )).build(ModEnchantments.FROST_EDGE.location())
        );

        // ==========================================
        // GLACIATION
        // ==========================================
        context.register(ModEnchantments.GLACIATION, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        1, // Weight (1 = Very Rare)
                        1, // Max Level
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        8,
                        EquipmentSlotGroup.MAINHAND
                )).build(ModEnchantments.GLACIATION.location())
        );
    }
}