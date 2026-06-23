package net.r_nik.extrashiny.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.enchant.ModEnchantments;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class TridentEnchantmentEvents {

    private static final int FREEZE_DAMAGE_TICKS = 400;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        Entity directEntity = event.getSource().getDirectEntity();
        ItemStack weaponStack = ItemStack.EMPTY;

        // Goodbye Reflection!
        if (directEntity instanceof ThrownTrident trident) {
            weaponStack = trident.getWeaponItem();
        }
        else if (directEntity instanceof net.r_nik.extrashiny.entity.VanadiumPartisanEntity partisan) {
            weaponStack = partisan.getItem();
        }

        if (weaponStack != null && !weaponStack.isEmpty()) {
            var registry = target.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            var glaciationHolder = registry.getHolder(ModEnchantments.GLACIATION);

            if (glaciationHolder.isEmpty()) return;

            int glaciationLevel = weaponStack.getEnchantmentLevel(glaciationHolder.get());

            if (glaciationLevel > 0) {
                int required = target.getTicksRequiredToFreeze();
                int baseTicks = Math.max(target.getTicksFrozen(), required);
                target.setTicksFrozen(baseTicks + FREEZE_DAMAGE_TICKS);
            }
        }
    }
}