package net.r_nik.extrashiny.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
        super(ModEntities.VANADIUM_PARTISAN_ENTITY.get(), owner, level);
        this.setItem(stack);
        this.entityData.set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
        this.entityData.set(FOIL, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOYALTY, (byte) 0);
        this.entityData.define(FOIL, false);
        this.entityData.define(DATA_ITEM, ItemStack.EMPTY);

    }

    @Override
    protected ItemStack getPickupItem() {
        return this.getItem().copy();
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

                this.setPosRaw(
                        this.getX(),
                        this.getY() + toOwner.y * 0.015 * loyalty,
                        this.getZ()
                );

                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                this.setDeltaMovement(
                        this.getDeltaMovement().scale(0.95)
                                .add(toOwner.normalize().scale(0.05 * loyalty))
                );

                if (this.clientReturnTick == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientReturnTick;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity owner = this.getOwner();
        if (owner != null && owner.isAlive()) {
            return !(owner instanceof ServerPlayer sp) || !sp.isSpectator();
        }
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        this.playSound(SoundEvents.TRIDENT_HIT_GROUND, 1.0F, 1.0F);

        if (!this.level().isClientSide && this.isChanneling() && this.level().isThundering()) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = this.level().getBlockState(pos);

            if (state.is(Blocks.LIGHTNING_ROD)) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (bolt != null) {
                    bolt.moveTo(Vec3.atBottomCenterOf(pos));
                    if (this.getOwner() instanceof ServerPlayer sp) {
                        bolt.setCause(sp);
                    }
                    this.level().addFreshEntity(bolt);
                }
                this.playSound(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity target = hit.getEntity();
        float damage = 11.0F;

        if (target instanceof LivingEntity living) {
            damage += EnchantmentHelper.getDamageBonus(this.getItem(), living.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().trident(this, owner == null ? this : owner);
        this.dealtDamage = true;

        if (target.hurt(source, damage)) {
            if (target instanceof LivingEntity l && owner instanceof LivingEntity o) {
                EnchantmentHelper.doPostHurtEffects(l, o);
                EnchantmentHelper.doPostDamageEffects(o, l);
            }
        }

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);

        if (this.isChanneling() && this.level().isThundering() && this.level().canSeeSky(target.blockPosition())) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (bolt != null) {
                bolt.moveTo(target.getX(), target.getY(), target.getZ());
                bolt.setCause(owner instanceof ServerPlayer sp ? sp : null);
                this.level().addFreshEntity(bolt);
            }
            this.playSound(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player)
                || (this.isNoPhysics() && this.ownedBy(player)
                && player.getInventory().add(this.getPickupItem()));
    }

    @Override
    public void tickDespawn() {
        int loyalty = this.entityData.get(LOYALTY);
        if (this.pickup != Pickup.ALLOWED || loyalty <= 0) {
            super.tickDespawn();
        }
    }

    public boolean isFoil() {
        return this.entityData.get(FOIL);
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.getItem());
    }
}
