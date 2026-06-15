package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.enchant.ModEnchantments;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class FrostEdgeEvents {

    private static final int FREEZE_TICKS_PER_LEVEL = 200;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        ItemStack weapon = livingAttacker.getMainHandItem();
        if (!(weapon.getItem() instanceof SwordItem)) return;

        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FROST_EDGE.get(), weapon);
        if (level <= 0) return;

        int add = FREEZE_TICKS_PER_LEVEL * level;

        int required = target.getTicksRequiredToFreeze();
        int max = required + add;

        int newTicksFrozen = Math.min(target.getTicksFrozen() + add, max);
        target.setTicksFrozen(newTicksFrozen);
    }
}