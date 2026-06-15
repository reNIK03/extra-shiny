package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.item.ModArmorMaterials;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.minecraft.world.item.TieredItem;
import net.r_nik.extrashiny.item.ModToolTiers;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ModItemAttributes {

    private static final UUID VANADIUM_UUID = UUID.fromString("5f4d945d-82df-4c4c-9131-8dfc5e533abc");
    private static final UUID OSMIUM_UUID = UUID.fromString("c1c8e9d0-7a1b-4c3d-b8a4-9a33e3a94a77");
    private static final UUID DAMASK_CRIT_UUID = UUID.fromString("8d7c9f6a-3a16-4b4b-9b67-8c1e4c9c3b21");
    private static final UUID OSMIUM_PIERCING_UUID = UUID.fromString("d2b9f8a1-4e7c-4d5a-9f1b-2c3d4e5f6a7b");

    private static final double DAMASK_CRIT_BONUS = 0.5D;
    private static final double OSMIUM_PIERCING_BONUS = 0.25D; // 25% Piercing

    private static final double VANADIUM_ARMOR_NEGATION = 1.0;
    private static final double VANADIUM_HORSE_NEGATION = 4.0;

    private static final UUID DAMASK_HORSE_COUNTER_THORNS_UUID = UUID.fromString("1f2a4c2e-8f6e-4f0b-9f0a-5f3b0d3b2c11");
    private static final double DAMASK_HORSE_COUNTER_THORNS = 1.0D; // 100%

    @SubscribeEvent
    public static void onItemAttribute(ItemAttributeModifierEvent event) {

        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof ArmorItem armor) {
            if (event.getSlotType() != armor.getType().getSlot()) return;
            if (armor.getMaterial() == ModArmorMaterials.VANADIUM) {
                addVanadiumAttribute(event, VANADIUM_ARMOR_NEGATION);
            }
        }

        if (stack.is(ModItems.VANADIUM_HORSE_ARMOR.get())) {
            if (event.getSlotType() == EquipmentSlot.CHEST) {
                addVanadiumAttribute(event, VANADIUM_HORSE_NEGATION);
            }
        }

        if (stack.is(ModItems.DAMASK_HORSE_ARMOR.get())) {
            if (event.getSlotType() == EquipmentSlot.CHEST) {
                addCounterThorns(event, DAMASK_HORSE_COUNTER_THORNS_UUID, DAMASK_HORSE_COUNTER_THORNS);
            }
        }

        if (event.getSlotType() == EquipmentSlot.MAINHAND && stack.getItem() instanceof TieredItem tiered) {
            if (tiered.getTier() == ModToolTiers.DAMASK) {
                event.addModifier(ModAttributes.CRIT_DAMAGE_BONUS.get(),
                        new AttributeModifier(DAMASK_CRIT_UUID, "Damask crit damage bonus", DAMASK_CRIT_BONUS, AttributeModifier.Operation.ADDITION));
            } else if (tiered.getTier() == ModToolTiers.OSMIUM) {
                event.addModifier(ModAttributes.ARMOR_PIERCING.get(),
                        new AttributeModifier(OSMIUM_PIERCING_UUID, "Osmium armor piercing", OSMIUM_PIERCING_BONUS, AttributeModifier.Operation.ADDITION));
            }
        }

        if (stack.is(ModItems.OSMIUM_HORSE_ARMOR.get())) {
            if (event.getSlotType() == EquipmentSlot.CHEST) {
                addShockAbsorption(event, 0.40D);
            }
        }
    }

    private static void addCounterThorns(ItemAttributeModifierEvent event, UUID uuid, double value) {
        event.addModifier(ModAttributes.COUNTER_THORNS.get(), new AttributeModifier(uuid, "Damask horse counter thorns", value, AttributeModifier.Operation.ADDITION));
    }

    private static void addVanadiumAttribute(ItemAttributeModifierEvent event, double value) {
        event.addModifier(ModAttributes.DAMAGE_NEGATION.get(), new AttributeModifier(VANADIUM_UUID, "Vanadium damage negation", value, AttributeModifier.Operation.ADDITION));
    }

    private static void addShockAbsorption(ItemAttributeModifierEvent event, double value) {
        event.addModifier(ModAttributes.SHOCK_ABSORPTION.get(), new AttributeModifier(OSMIUM_UUID, "Osmium shock absorption", value, AttributeModifier.Operation.ADDITION));
    }
}