package net.r_nik.extrashiny.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OvercapEnchantmentTooltip {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEnchanted()) return;

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (enchants.isEmpty()) return;

        List<Component> tooltip = event.getToolTip();

        Set<String> overcappedNames = new HashSet<>();

        for (var entry : enchants.entrySet()) {
            Enchantment ench = entry.getKey();
            int level = entry.getValue();

            if (level > ench.getMaxLevel()) {
                overcappedNames.add(
                        ench.getFullname(level).getString()
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
