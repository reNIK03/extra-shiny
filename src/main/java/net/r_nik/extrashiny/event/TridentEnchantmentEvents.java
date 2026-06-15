package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.enchant.ModEnchantments;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class TridentEnchantmentEvents {

    private static Field tridentItemField;

    static {
        try {
            tridentItemField = ThrownTrident.class.getDeclaredField("tridentItem");
            tridentItemField.setAccessible(true);
        } catch (Exception e) {
            try {
                tridentItemField = ThrownTrident.class.getDeclaredField("f_37554_");
                tridentItemField.setAccessible(true);
            } catch (Exception ignored) {}
        }
    }

    private static final int FREEZE_DAMAGE_TICKS = 400;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) return;

        Entity directEntity = event.getSource().getDirectEntity();
        ItemStack weaponStack = ItemStack.EMPTY;

        if (directEntity instanceof ThrownTrident trident) {
            try {
                if (tridentItemField != null) {
                    weaponStack = (ItemStack) tridentItemField.get(trident);
                }
            } catch (Exception ignored) {}
        }
        else if (directEntity instanceof net.r_nik.extrashiny.entity.VanadiumPartisanEntity partisan) {
            weaponStack = partisan.getItem();
        }

        if (weaponStack != null && !weaponStack.isEmpty()) {
            int glaciationLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.GLACIATION.get(), weaponStack);

            if (glaciationLevel > 0) {
                int required = target.getTicksRequiredToFreeze();

                int baseTicks = Math.max(target.getTicksFrozen(), required);

                target.setTicksFrozen(baseTicks + FREEZE_DAMAGE_TICKS);
            }
        }
    }
}