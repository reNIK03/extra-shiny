package net.r_nik.extrashiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.attribute.ModAttributes;

import java.util.UUID;

public class DamaskHorseArmorItem extends HorseArmorItem {

    public DamaskHorseArmorItem(int armorValue, String tierArmor, Properties properties) {
        super(
                armorValue,
                new ResourceLocation(ExtraShiny.MOD_ID,
                        "textures/entity/horse/armor/horse_armor_" + tierArmor + ".png"
                ),
                properties
        );
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));


        return slot == EquipmentSlot.CHEST ? builder.build() : super.getAttributeModifiers(slot, stack);
    }
}