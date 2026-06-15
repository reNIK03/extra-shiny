package net.r_nik.extrashiny.core.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import net.r_nik.extrashiny.event.ModCombatEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {

    @Inject(method = "doPostHurt", at = @At("HEAD"), cancellable = true)
    private void extrashiny$disableVanillaThornsIfDamaskActive(LivingEntity wearer, Entity attacker, int level, CallbackInfo ci) {
        if (ModCombatEvents.getCounterThornsPctFromArmor(wearer) > 0.0D) {
            ci.cancel();
        }
    }
}
