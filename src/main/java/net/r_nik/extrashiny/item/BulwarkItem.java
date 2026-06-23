package net.r_nik.extrashiny.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.ModBlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;

public class BulwarkItem extends ShieldItem {

    public BulwarkItem(Item.Properties properties) {
        super(properties.attributes(
                ItemAttributeModifiers.builder()
                        .add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                                        ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "bulwark_kb_main"),
                                        0.4D,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                                        ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "bulwark_speed_main"),
                                        -0.30D,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                                        ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "bulwark_kb_off"),
                                        0.4D,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.OFFHAND)
                        .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                                        ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "bulwark_speed_off"),
                                        -0.30D,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                EquipmentSlotGroup.OFFHAND)
                        .build()
        ));
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pRepair.is(ModItems.OSMIUM_INGOT.get())) {
            return true;
        }
        return super.isValidRepairItem(pToRepair, pRepair);
    }
}