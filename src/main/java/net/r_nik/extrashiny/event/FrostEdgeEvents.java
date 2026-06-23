package net.r_nik.extrashiny.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent; // Updated import
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.enchant.ModEnchantments;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class FrostEdgeEvents {

    private static final int FREEZE_TICKS_PER_LEVEL = 200;

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        // Direct access to getSource()
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        ItemStack weapon = livingAttacker.getMainHandItem();
        if (!(weapon.getItem() instanceof SwordItem)) return;

        var registry = target.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var frostEdgeHolder = registry.getHolder(ModEnchantments.FROST_EDGE);

        if (frostEdgeHolder.isEmpty()) return;

        int level = EnchantmentHelper.getItemEnchantmentLevel(frostEdgeHolder.get(), weapon);
        if (level <= 0) return;

        int add = FREEZE_TICKS_PER_LEVEL * level;
        int required = target.getTicksRequiredToFreeze();
        int max = required + add;

        int newTicksFrozen = Math.min(target.getTicksFrozen() + add, max);
        target.setTicksFrozen(newTicksFrozen);
    }
}