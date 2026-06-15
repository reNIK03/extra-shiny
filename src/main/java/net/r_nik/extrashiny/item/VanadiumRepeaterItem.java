package net.r_nik.extrashiny.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class VanadiumRepeaterItem extends CrossbowItem {

    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;

    public VanadiumRepeaterItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairMaterial) {
        return repairMaterial.is(ModItems.VANADIUM_INGOT.get()) || super.isValidRepairItem(toRepair, repairMaterial);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return getRepeaterChargeTime(stack) + 3;
    }

    public int getRepeaterChargeTime(ItemStack stack) {
        int quick = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        int base = 60;
        int duration = base - (quick * 10);
        return Math.max(1, duration);
    }

    private float getCustomPowerForTime(int useTime, ItemStack stack) {
        float f = (float) useTime / (float) getRepeaterChargeTime(stack);
        return f > 1.0F ? 1.0F : f;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);

            net.minecraft.sounds.SoundEvent startSound = this.getRepeaterStartSound(quickCharge);
            net.minecraft.sounds.SoundEvent midSound = quickCharge == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;

            float f = (float) (stack.getUseDuration() - count) / (float) getRepeaterChargeTime(stack);

            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), startSound, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && midSound != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), midSound, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int useTime = this.getUseDuration(stack) - timeLeft;
        float power = getCustomPowerForTime(useTime, stack);

        if (power >= 1.0F && !isCharged(stack) && tryLoadRepeaterProjectiles(entityLiving, stack)) {
            setCharged(stack, true);
            SoundSource soundSource = entityLiving instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundSource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    private net.minecraft.sounds.SoundEvent getRepeaterStartSound(int quickCharge) {
        switch (quickCharge) {
            case 1:  return net.minecraft.sounds.SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:  return net.minecraft.sounds.SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:  return net.minecraft.sounds.SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default: return net.minecraft.sounds.SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    private boolean tryLoadRepeaterProjectiles(LivingEntity entity, ItemStack crossbow) {
        int multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow) > 0 ? 3 : 1;
        boolean isCreative = entity instanceof Player player && player.getAbilities().instabuild;

        ItemStack ammoToLoad = entity.getProjectile(crossbow);
        ItemStack baseAmmo = ammoToLoad.copy();

        if (ammoToLoad.isEmpty() && isCreative) {
            baseAmmo = new ItemStack(Items.ARROW);
            ammoToLoad = baseAmmo;
        }

        if (ammoToLoad.isEmpty()) {
            return false;
        }

        CompoundTag tag = crossbow.getOrCreateTag();
        ListTag listTag = new ListTag();

        for (int i = 0; i < multishot; i++) {
            ItemStack singleShot = baseAmmo.copy();
            singleShot.setCount(1);

            CompoundTag projTag = new CompoundTag();
            singleShot.save(projTag);
            listTag.add(projTag);
        }

        tag.put("ChargedProjectiles", listTag);

        if (!isCreative) {
            ammoToLoad.shrink(1);
            if (ammoToLoad.isEmpty() && entity instanceof Player player) {
                player.getInventory().removeItem(ammoToLoad);
            }
        }

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        boolean wasCharged = isCharged(stack);
        ItemStack copyOfCharged = ItemStack.EMPTY;
        float velocity = 0.0F;
        float inaccuracy = 1.0F;

        if (wasCharged) {
            copyOfCharged = stack.copy();
            boolean hasFirework = RepeaterHelper.stackHasFirework(copyOfCharged);
            velocity = hasFirework ? 1.6F : 3.15F;
            inaccuracy = 1.0F;
        }

        InteractionResultHolder<ItemStack> res = super.use(level, player, hand);

        if (!level.isClientSide && wasCharged && !copyOfCharged.isEmpty()) {
            DelayedShotScheduler.schedule(player, copyOfCharged, velocity, inaccuracy, 0.0F, 5);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.CROSSBOW_SHOOT, player.getSoundSource(),
                    1.0F, 1.0F);

            DelayedShotScheduler.schedule(player, copyOfCharged, velocity, inaccuracy, 0.0F, 10);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.CROSSBOW_SHOOT, player.getSoundSource(),
                    1.0F, 1.0F);
        }

        return res;
    }
}