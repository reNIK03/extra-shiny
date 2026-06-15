package net.r_nik.extrashiny.core.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.r_nik.extrashiny.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {

    @Inject(method = "getExplosionKnockbackAfterDampener", at = @At("HEAD"), cancellable = true)
    private static void extrashiny_virtualBlastProtection(LivingEntity entity, double damage, CallbackInfoReturnable<Double> cir) {

        int blastProtLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, entity);

        boolean hasBulwark = entity.getMainHandItem().is(ModItems.BULWARK.get()) ||
                entity.getOffhandItem().is(ModItems.BULWARK.get());

        if (hasBulwark) {
            if (entity.isBlocking() && entity.getUseItem().is(ModItems.BULWARK.get())) {
                blastProtLevel += 6;
            } else {
                blastProtLevel += 3;
            }
        }

        blastProtLevel = Math.min(blastProtLevel, 6);

        if (blastProtLevel > 0) {
            damage -= damage * (double)((float)blastProtLevel * 0.15F);
        }

        cir.setReturnValue(damage);
    }
}