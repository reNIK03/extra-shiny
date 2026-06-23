package net.r_nik.extrashiny.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.r_nik.extrashiny.item.ModItems;

public class VanadiumPartisanEntity extends AbstractArrow {

    private static final EntityDataAccessor<Byte> LOYALTY =
            SynchedEntityData.defineId(VanadiumPartisanEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FOIL =
            SynchedEntityData.defineId(VanadiumPartisanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DATA_ITEM =
            SynchedEntityData.defineId(VanadiumPartisanEntity.class, EntityDataSerializers.ITEM_STACK);

    private boolean dealtDamage;
    public int clientReturnTick;

    public VanadiumPartisanEntity(EntityType<? extends VanadiumPartisanEntity> type, Level level) {
        super(type, level);
    }

    public VanadiumPartisanEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.VANADIUM_PARTISAN_ENTITY.get(), level);
        this.setOwner(owner);
        this.setItem(stack);
        this.entityData.set(LOYALTY, (byte) getLoyaltyLevel(stack));
        this.entityData.set(FOIL, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LOYALTY, (byte) 0);
        builder.define(FOIL, false);
        builder.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.VANADIUM_PARTISAN.get()); // Replace with your actual item
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM, stack.copy());
    }

    public ItemStack getItem() {
        return this.entityData.get(DATA_ITEM);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        int loyalty = this.entityData.get(LOYALTY);
        Entity owner = this.getOwner();

        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 toOwner = owner.getEyePosition().subtract(this.position());

                this.setPosRaw(this.getX(), this.getY() + toOwner.y * 0.015 * loyalty, this.getZ());

                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(toOwner.normalize().scale(0.05 * loyalty)));

                if (this.clientReturnTick == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.clientReturnTick;
            }
        }
        super.tick();
    }

    private int getLoyaltyLevel(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(
                this.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                        .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOYALTY),
                stack
        );
    }

    private boolean isAcceptibleReturnOwner() {
        Entity owner = this.getOwner();
        return owner != null && owner.isAlive() && (!(owner instanceof ServerPlayer sp) || !sp.isSpectator());
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity target = hit.getEntity();
        float damage = 11.0F;

        // 1.21.1: Use RegistryAccess to get damage bonus
        if (target instanceof LivingEntity living) {
            damage += EnchantmentHelper.getDamageBonus(this.level().registryAccess(), this.getItem(), living.getMobType(), this.damageSources().mobAttack(this));
        }

        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().trident(this, owner == null ? this : owner);
        this.dealtDamage = true;

        if (target.hurt(source, damage)) {
            if (target instanceof LivingEntity l && owner instanceof LivingEntity o) {
                // 1.21.1: Consolidated post-attack effects
                EnchantmentHelper.doPostAttackEffects(this.level(), target, source);
            }
        }

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);

        // ... (Lightning/Channeling logic remains the same)
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
    }

    public boolean isChanneling() {
        // 1.21.1: Check for Channeling using Holder
        return EnchantmentHelper.getItemEnchantmentLevel(
                this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.CHANNELING),
                this.getItem()
        ) > 0;
    }
}