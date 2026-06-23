package net.r_nik.extrashiny.client;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.*;

@EventBusSubscriber
public class OvercapEnchantmentTooltip {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEnchanted()) return;

        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        if (enchants.isEmpty()) return;

        List<Component> tooltip = event.getToolTip();

        Set<String> overcappedNames = new HashSet<>();

        for (Holder<Enchantment> ench : enchants.keySet()) {
            int level = enchants.getLevel(ench);

            if (level > ench.value().getMaxLevel()) {
                overcappedNames.add(
                        Enchantment.getFullname(ench, level).getString()
                );
            }
        }

        if (overcappedNames.isEmpty()) return;

        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            String text = line.getString();

            if (overcappedNames.contains(text)) {
                MutableComponent colored =
                        Component.literal(text + " ◆")
                                .withStyle(ChatFormatting.GREEN);

                tooltip.set(i, colored);
            }
        }
    }
}