package net.r_nik.extrashiny.core.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int extrashiny$interceptDamage(int amount, int originalAmount, RandomSource random, ServerPlayer user) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!stack.hasTag() || !stack.getTag().contains("MemoryDurability")) {
            return amount;
        }

        int memoryDura = stack.getTag().getInt("MemoryDurability");

        if (memoryDura > 0) {
            if (amount <= memoryDura) {
                stack.getTag().putInt("MemoryDurability", memoryDura - amount);
                return 0;
            } else {
                stack.getTag().putInt("MemoryDurability", 0);
                return amount - memoryDura;
            }
        }

        return amount;
    }
}