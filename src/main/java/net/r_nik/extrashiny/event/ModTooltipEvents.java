package net.r_nik.extrashiny.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.item.ModArmorMaterials;
import net.r_nik.extrashiny.item.ModItems;

import java.util.Collection;
import java.util.Locale;

@Mod.EventBusSubscriber
public class ModTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.is(ModItems.DAMASK_HORSE_ARMOR.get())) {
            addPercentAttributeLineForSlot(
                    event,
                    stack,
                    EquipmentSlot.CHEST,
                    ModAttributes.COUNTER_THORNS.get(),
                    "Counter Thorns",
                    ChatFormatting.BLUE
            );
            return;
        }

        if (!(stack.getItem() instanceof ArmorItem armor))
            return;

        if (armor.getMaterial() == ModArmorMaterials.OSMIUM) {
            int percent = 15;

            event.getToolTip().add(
                    Component.literal("+" + percent + "% Shock Absorption")
                            .withStyle(ChatFormatting.BLUE)
            );
            return;
        }

        if (armor.getMaterial() == ModArmorMaterials.CIMMERIAN) {
            addPercentAttributeLine(
                    event,
                    stack,
                    armor,
                    ModAttributes.DAMAGE_REBOUND.get(),
                    "Damage Rebound",
                    ChatFormatting.BLUE
            );
            return;
        }

        if (armor.getMaterial() == ModArmorMaterials.DAMASK) {
            addPercentAttributeLine(
                    event,
                    stack,
                    armor,
                    ModAttributes.COUNTER_THORNS.get(),
                    "Counter Thorns",
                    ChatFormatting.BLUE
            );
        }
    }


    private static void addPercentAttributeLine(
            ItemTooltipEvent event,
            ItemStack stack,
            ArmorItem armor,
            Attribute attribute,
            String label,
            ChatFormatting color
    ) {
        addPercentAttributeLineForSlot(event, stack, armor.getType().getSlot(), attribute, label, color);
    }


    private static void addPercentAttributeLineForSlot(
            ItemTooltipEvent event,
            ItemStack stack,
            EquipmentSlot slot,
            Attribute attribute,
            String label,
            ChatFormatting color
    ) {
        final String attrKey = attribute.getDescriptionId();
        final String localizedName = Component.translatable(attrKey).getString();

        event.getToolTip().removeIf(c -> {
            String s = c.getString();
            boolean mentionsAttr = s.contains(localizedName) || s.contains(attrKey);
            boolean isPercentLine = s.contains("%");
            return mentionsAttr && !isPercentLine;
        });

        Collection<AttributeModifier> mods = stack.getAttributeModifiers(slot).get(attribute);
        if (mods == null || mods.isEmpty()) return;

        double total = 0.0D;
        for (AttributeModifier mod : mods) {
            total += mod.getAmount();
        }

        double percent = total * 100.0;

        String percentText = (percent % 1.0 == 0.0)
                ? String.valueOf((int) percent)
                : String.format(Locale.ROOT, "%.1f", percent);

        event.getToolTip().add(
                Component.literal("+" + percentText + "% " + label)
                        .withStyle(color)
        );
    }
}