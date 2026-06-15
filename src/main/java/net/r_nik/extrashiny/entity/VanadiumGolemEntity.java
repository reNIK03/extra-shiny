package net.r_nik.extrashiny.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.r_nik.extrashiny.compat.ModCompat;
import net.r_nik.extrashiny.entity.ai.*;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.sound.ModSounds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VanadiumGolemEntity extends AbstractGolem {

    // ==========================================
    // CONSTANTS & TUNING
    // ==========================================
    private static final String NBT_DECOR = "Decor";

    public static final int SMALL_HIT_LENGTH = 29;
    public static final int SMALL_HIT_DAMAGE_TICK = 3;

    public static final int MEDIUM_HIT_LENGTH = 43;
    public static final int MEDIUM_HIT_DAMAGE_TICK = 18;

    public static final int HEAVY_HIT_LENGTH = 77;
    public static final int HEAVY_HIT_DAMAGE_TICK = 24;

    private static final int SMALL_SPIN_TICK = 5;
    private static final int MEDIUM_SPIN_TICK = 8;
    private static final int MEDIUM_HIT_TICK = 18;
    private static final int MEDIUM_END_TICK = 18;
    private static final int HEAVY_START_TICK = 4;
    private static final int HEAVY_HIT_TICK = 24;
    private static final int HEAVY_END_TICK = 50;

    private static final Set<ResourceLocation> BLACKLISTED_FLOWERS = Set.of(
            new ResourceLocation("minecraft", "torchflower"),
            new ResourceLocation("minecraft", "wither_rose"),
            new ResourceLocation("collectorsreap", "damselflower"),
            new ResourceLocation("collectorsreap", "moontear"),
            new ResourceLocation("collectorsreap", "skull_lily")
    );

    private static final Map<Item, DecorType> CARPET_TO_DECOR = Map.ofEntries(
            Map.entry(Items.WHITE_CARPET, DecorType.WHITE),
            Map.entry(Items.ORANGE_CARPET, DecorType.ORANGE),
            Map.entry(Items.MAGENTA_CARPET, DecorType.MAGENTA),
            Map.entry(Items.LIGHT_BLUE_CARPET, DecorType.LIGHT_BLUE),
            Map.entry(Items.YELLOW_CARPET, DecorType.YELLOW),
            Map.entry(Items.LIME_CARPET, DecorType.LIME),
            Map.entry(Items.PINK_CARPET, DecorType.PINK),
            Map.entry(Items.GRAY_CARPET, DecorType.GRAY),
            Map.entry(Items.LIGHT_GRAY_CARPET, DecorType.LIGHT_GRAY),
            Map.entry(Items.CYAN_CARPET, DecorType.CYAN),
            Map.entry(Items.PURPLE_CARPET, DecorType.PURPLE),
            Map.entry(Items.BLUE_CARPET, DecorType.BLUE),
            Map.entry(Items.BROWN_CARPET, DecorType.BROWN),
            Map.entry(Items.GREEN_CARPET, DecorType.GREEN),
            Map.entry(Items.RED_CARPET, DecorType.RED),
            Map.entry(Items.BLACK_CARPET, DecorType.BLACK)
    );

    // ==========================================
    // ENUMS
    // ==========================================
    public enum DecorType {
        NONE, WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY,
        LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK,
        AMBER, AQUA, BEIGE, CORAL, FOREST, GINGER, INDIGO, MAROON,
        MINT, NAVY, OLIVE, ROSE, SLATE, TAN, TEAL, VERDANT
    }

    public enum AttackType {
        SMALL(1.0F),
        MEDIUM(1.3F),
        HEAVY(1.6F),
        NONE(1.0F);

        public final float damageMultiplier;

        AttackType(float damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
        }
    }

    // ==========================================
    // SYNCHED DATA IDS
    // ==========================================
    private static final EntityDataAccessor<Boolean> PLAYER_CREATED = SynchedEntityData.defineId(VanadiumGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DECOR = SynchedEntityData.defineId(VanadiumGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(VanadiumGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(VanadiumGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HOSTILE_TO_PLAYER = SynchedEntityData.defineId(VanadiumGolemEntity.class, EntityDataSerializers.BOOLEAN);

    // ==========================================
    // FIELDS & VARIABLES
    // ==========================================
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    private int attackTicks = 0;

    private boolean hasSubduedTarget = false;
    private AttackType queuedAttack = AttackType.NONE;

    public float lockedYaw;
    public float lockedBodyYaw;
    public boolean rotationLocked;

    // ==========================================
    // CONSTRUCTOR & INITIALIZATION
    // ==========================================
    public VanadiumGolemEntity(EntityType<? extends VanadiumGolemEntity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new VanadiumGolemHeavyAttackGoal(this));
        this.goalSelector.addGoal(2, new VanadiumGolemMediumAttackGoal(this));
        this.goalSelector.addGoal(3, new VanadiumGolemSmallAttackGoal(this));
        this.goalSelector.addGoal(4, new VanadiumGolemApproachTargetGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new VanadiumGolemLookAtAnimalGoal(this));
        this.goalSelector.addGoal(8, new VanadiumGolemLookAtFlowerGoal(this));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, mob -> mob instanceof Enemy));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ATTACK_TYPE, AttackType.NONE.ordinal());
        this.entityData.define(DECOR, DecorType.NONE.ordinal());
        this.entityData.define(PLAYER_CREATED, false);
        this.entityData.define(HOSTILE_TO_PLAYER, false);
    }

    // ==========================================
    // CORE TICK & MOVEMENT
    // ==========================================
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.isAttacking()) {
            attackTicks++;
            AttackType type = this.getAttackType();

            switch (type) {
                case SMALL -> {
                    if (attackTicks == SMALL_SPIN_TICK) {
                        this.playSound(ModSounds.VGOLEM_LIGHT_SPIN.get(), 1.0F, 1.0F);
                    }
                }
                case MEDIUM -> {
                    if (attackTicks == MEDIUM_SPIN_TICK) {
                        this.playSound(ModSounds.VGOLEM_SPIN.get(), 1.0F, 1.0F);
                    }
                    if (attackTicks == MEDIUM_HIT_TICK) {
                        this.playSound(ModSounds.VGOLEM_M_ATK.get(), 1.4F, 1.0F);
                        SoundEvent breakSound = this.level().getBlockState(this.blockPosition().below()).getSoundType().getBreakSound();
                        for (int i = 0; i < 3; i++) {
                            this.level().playSound(null, this.blockPosition(), breakSound, SoundSource.HOSTILE, 1.0F, 0.9F + (this.random.nextFloat() * 0.1F));
                        }
                    }
                    if (attackTicks == MEDIUM_END_TICK) {
                        this.playSound(ModSounds.VGOLEM_ATK_END.get(), 1.0F, 1.0F);
                    }
                }
                case HEAVY -> {
                    if (attackTicks == HEAVY_START_TICK) {
                        this.playSound(ModSounds.VGOLEM_H_ATK_START.get(), 1.2F, 1.0F);
                    }
                    if (attackTicks == HEAVY_HIT_TICK) {
                        SoundEvent breakSound = this.level().getBlockState(this.blockPosition().below()).getSoundType().getBreakSound();
                        for (int i = 0; i < 5; i++) {
                            this.level().playSound(null, this.blockPosition(), breakSound, SoundSource.HOSTILE, 1.3F, 0.7F + (this.random.nextFloat() * 0.15F));
                        }
                        this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 0.8F, 0.6F);
                    }
                    if (attackTicks == HEAVY_END_TICK) {
                        this.playSound(ModSounds.VGOLEM_ATK_END.get(), 1.0F, 1.0F);
                    }
                }
            }

            if (attackTicks >= getAttackLength(this.getAttackType())) {
                this.entityData.set(ATTACKING, false);
                this.entityData.set(ATTACK_TYPE, AttackType.NONE.ordinal());
                attackTicks = 0;
                clearQueuedAttack();
            }
        }

        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAngryAtPlayer()) {
            LivingEntity target = this.getTarget();
            boolean invalid = target == null || !target.isAlive() || !(target instanceof Player player) || player.isCreative() || player.isSpectator();
            if (invalid) {
                this.setTarget(null);
                this.entityData.set(HOSTILE_TO_PLAYER, false);
            }
        }

        if (rotationLocked) {
            this.setYRot(this.lockedYaw);
            this.yRotO = this.lockedYaw;
            this.yBodyRot = this.lockedBodyYaw;
            this.yBodyRotO = this.lockedBodyYaw;
            this.yHeadRot = this.lockedYaw;
            this.yHeadRotO = this.lockedYaw;
        }

        if (!this.level().isClientSide && this.getTarget() == null) {
            this.entityData.set(HOSTILE_TO_PLAYER, false);
        }

        if (this.level().isClientSide) return;

        LivingEntity current = this.getTarget();

        if (this.isAngryAtPlayer()) {
            if (!this.isAttacking() && this.getQueuedAttack() == AttackType.NONE) {
                LivingEntity target = this.getTarget();
                if (target != null && target.isAlive()) {
                    this.queueAttack(rollSingleTargetAttack(target));
                }
            }
            return;
        }

        LivingEntity best = this.level().getNearestEntity(
                Monster.class,
                TargetingConditions.forCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(LivingEntity::isAlive),
                this, this.getX(), this.getY(), this.getZ(),
                this.getBoundingBox().inflate(40.0D)
        );

        if (best != null && best != current) {
            this.setTarget(best);
            this.resetSubduedTarget();
        }

        if (current != null && !current.isAlive()) {
            this.resetSubduedTarget();
        }

        if (!this.isAttacking() && this.getQueuedAttack() == AttackType.NONE) {
            LivingEntity target = this.getTarget();

            if (target instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) {
                    this.setTarget(null);
                    this.entityData.set(HOSTILE_TO_PLAYER, false);
                    this.resetSubduedTarget();
                    return;
                }
            }

            if (target != null && target.isAlive()) {
                int nearby = countNearbyMonsters(4.0D);
                if (nearby >= 3) {
                    this.queueAttack(AttackType.HEAVY);
                    return;
                }
                if (shouldUseRangedHeavyAttack(target)) {
                    this.queueAttack(AttackType.HEAVY);
                    return;
                }
                this.queueAttack(rollSingleTargetAttack(target));
            }
        }
    }

    @Override
    protected void updateControlFlags() {
        super.updateControlFlags();
    }

    private void setupAnimationStates() {
        if (!isMoving() && !this.isAttacking()) {
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                idleAnimationState.start(this.tickCount);
            } else {
                idleAnimationTimeout--;
            }
        } else {
            idleAnimationState.stop();
        }

        if (isMoving() && !this.isAttacking()) {
            walkAnimationState.startIfStopped(this.tickCount);
        } else {
            walkAnimationState.stop();
        }

        if (this.isAttacking()) {
            if (!attackAnimationState.isStarted()) {
                attackAnimationState.start(this.tickCount);
            }
        } else {
            attackAnimationState.stop();
        }
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    // ==========================================
    // COMBAT & TARGETING
    // ==========================================
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) {
            amount *= 0.25F;
        }

        boolean result = super.hurt(source, amount);

        if (!this.level().isClientSide && result) {
            if (!this.isPlayerCreated() && source.getEntity() instanceof Player player && !player.isCreative() && !player.isSpectator()) {
                boolean wasPassive = !this.isHostileToPlayer();
                this.setTarget(player);
                if (wasPassive) {
                    this.playHostileActivationEffect();
                    this.alertNearbyNeutralGolems(player);
                }
            }
        }
        return result;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity target)) return false;

        float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        AttackType attackType = this.getAttackType();
        float multiplier = attackType != null ? attackType.damageMultiplier : 1.0F;
        float finalDamage = baseDamage * multiplier;

        boolean hit = target.hurt(this.damageSources().mobAttack(this), finalDamage);

        if (hit) {
            this.doEnchantDamageEffects(this, target);
        }
        return hit;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isPlayerCreated()) {
            return target instanceof Monster && target.isAlive();
        }
        if (target instanceof Player) {
            return this.getTarget() == target;
        }
        return target instanceof Monster && target.isAlive();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator() || this.isPlayerCreated()) {
                super.setTarget(null);
                this.entityData.set(HOSTILE_TO_PLAYER, false);
                return;
            }
        }
        super.setTarget(target);
        if (!this.level().isClientSide) {
            this.entityData.set(HOSTILE_TO_PLAYER, target instanceof Player);
        }
    }

    public AABB getAttackBox(double forwardOffset, double width, double height, double depth) {
        Vec3 forward = Vec3.directionFromRotation(0, this.yBodyRot).normalize();
        Vec3 center = this.position().add(0, this.getBbHeight() * 0.5, 0).add(forward.scale(forwardOffset));

        double halfW = width / 2.0;
        double halfD = depth / 2.0;

        return new AABB(
                center.x - halfW, center.y - height / 2.0, center.z - halfD,
                center.x + halfW, center.y + height / 2.0, center.z + halfD
        );
    }

    public AttackType rollSingleTargetAttack(LivingEntity target) {
        if (target == null) return AttackType.SMALL;
        float roll = this.random.nextFloat();

        if (isHighPriorityTarget(target)) {
            return roll < 0.75F ? AttackType.MEDIUM : AttackType.HEAVY;
        }

        if (roll < 0.50F) return AttackType.SMALL;
        if (roll < 0.80F) return AttackType.MEDIUM;
        return AttackType.HEAVY;
    }

    public void faceTargetInstant(LivingEntity target) {
        if (target == null) return;
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

        this.setYRot(yaw);
        this.yRotO = yaw;
        this.yBodyRot = yaw;
        this.yBodyRotO = yaw;
        this.yHeadRot = yaw;
        this.yHeadRotO = yaw;
    }

    public void alertNearbyNeutralGolems(Player player) {
        if (this.level().isClientSide) return;
        for (VanadiumGolemEntity golem : level().getEntitiesOfClass(VanadiumGolemEntity.class, this.getBoundingBox().inflate(24.0D), g -> g != this && !g.isPlayerCreated() && !g.isHostileToPlayer())) {
            golem.setTarget(player);
            golem.playHostileActivationEffect();
        }
    }

    public void alertNearbyGolems(LivingEntity target) {
        if (this.isPlayerCreated()) return;
        for (VanadiumGolemEntity golem : level().getEntitiesOfClass(VanadiumGolemEntity.class, getBoundingBox().inflate(32.0D), g -> g != this && !g.isPlayerCreated() && g.getTarget() == null)) {
            golem.setTarget(target);
        }
    }

    public boolean isValidAttackTarget(LivingEntity entity) {
        if (entity == this || !entity.isAlive() || entity instanceof VanadiumGolemEntity) return false;
        if (this.isPlayerCreated() && entity instanceof Player) return false;
        if (!this.isPlayerCreated() && entity instanceof Player) return this.getTarget() == entity;
        return entity instanceof Enemy;
    }

    public boolean canAttackPlayerTarget() {
        return !this.isPlayerCreated() && this.isHostileToPlayer() && this.getTarget() instanceof Player;
    }

    public boolean shouldUseRangedHeavyAttack(LivingEntity target) {
        if (target == null || !target.isAlive() || !this.hasSubduedTarget()) return false;
        double dist = this.distanceTo(target);
        return dist >= 3.0D && dist <= 5.0D;
    }

    public boolean isHighPriorityTarget(LivingEntity target) {
        return target instanceof net.minecraft.world.entity.monster.Creeper
                || target instanceof net.minecraft.world.entity.monster.EnderMan
                || target instanceof net.minecraft.world.entity.monster.Zoglin
                || target instanceof net.minecraft.world.entity.monster.WitherSkeleton
                || target instanceof net.minecraft.world.entity.monster.Ravager
                || target instanceof net.minecraft.world.entity.monster.Evoker;
    }

    public int countNearbyMonsters(double radius) {
        return level().getEntitiesOfClass(Monster.class, getBoundingBox().inflate(radius), LivingEntity::isAlive).size();
    }

    public void markTargetSubdued() {
        if (this.getTarget() instanceof Player) return;
        this.hasSubduedTarget = true;
    }

    public boolean hasSubduedTarget() {
        return this.hasSubduedTarget;
    }

    public void resetSubduedTarget() {
        this.hasSubduedTarget = false;
    }

    private int getAttackLength(AttackType type) {
        return switch (type) {
            case SMALL -> 29;
            case MEDIUM -> 43;
            case HEAVY -> 77;
            default -> 0;
        };
    }

    public void queueAttack(AttackType type) {
        this.queuedAttack = type;
    }

    public void clearQueuedAttack() {
        this.queuedAttack = AttackType.NONE;
    }

    public AttackType getQueuedAttack() {
        return queuedAttack;
    }

    public boolean isAngryAtPlayer() {
        return !this.isPlayerCreated() && this.isHostileToPlayer() && this.getTarget() instanceof Player;
    }

    // ==========================================
    // INTERACTION & DECOR
    // ==========================================
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.VANADIUM_INGOT.get())) {
            if (!this.level().isClientSide) {
                float current = this.getHealth();
                float max = this.getMaxHealth();

                if (current < max) {
                    this.heal(50.0F);
                    this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.hasDecor()) {
            DecorType decor = getDecorFromCarpet(stack);
            if (decor != null) {
                if (!this.level().isClientSide) {
                    this.setDecor(decor);
                    this.playSound(SoundEvents.WOOL_PLACE, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if (this.hasDecor() && stack.is(Items.SHEARS)) {
            if (!this.level().isClientSide) {
                this.spawnAtLocation(getCarpetFromDecor());
                this.setDecor(DecorType.NONE);
                this.playSound(SoundEvents.WOOL_BREAK, 1.0F, 1.0F);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public DecorType getDecor() {
        return DecorType.values()[this.entityData.get(DECOR)];
    }

    public void setDecor(DecorType decor) {
        this.entityData.set(DECOR, decor.ordinal());
    }

    public boolean hasDecor() {
        return getDecor() != DecorType.NONE;
    }

    @Nullable
    private DecorType getDecorFromCarpet(ItemStack stack) {
        if (stack.is(Items.WHITE_CARPET)) return DecorType.WHITE;
        if (stack.is(Items.ORANGE_CARPET)) return DecorType.ORANGE;
        if (stack.is(Items.MAGENTA_CARPET)) return DecorType.MAGENTA;
        if (stack.is(Items.LIGHT_BLUE_CARPET)) return DecorType.LIGHT_BLUE;
        if (stack.is(Items.YELLOW_CARPET)) return DecorType.YELLOW;
        if (stack.is(Items.LIME_CARPET)) return DecorType.LIME;
        if (stack.is(Items.PINK_CARPET)) return DecorType.PINK;
        if (stack.is(Items.GRAY_CARPET)) return DecorType.GRAY;
        if (stack.is(Items.LIGHT_GRAY_CARPET)) return DecorType.LIGHT_GRAY;
        if (stack.is(Items.CYAN_CARPET)) return DecorType.CYAN;
        if (stack.is(Items.PURPLE_CARPET)) return DecorType.PURPLE;
        if (stack.is(Items.BLUE_CARPET)) return DecorType.BLUE;
        if (stack.is(Items.BROWN_CARPET)) return DecorType.BROWN;
        if (stack.is(Items.GREEN_CARPET)) return DecorType.GREEN;
        if (stack.is(Items.RED_CARPET)) return DecorType.RED;
        if (stack.is(Items.BLACK_CARPET)) return DecorType.BLACK;

        if (ModCompat.DYE_DEPOT_LOADED) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (id == null || !id.getNamespace().equals("dye_depot")) return null;

            return switch (id.getPath()) {
                case "amber_carpet" -> DecorType.AMBER;
                case "aqua_carpet" -> DecorType.AQUA;
                case "beige_carpet" -> DecorType.BEIGE;
                case "coral_carpet" -> DecorType.CORAL;
                case "forest_carpet" -> DecorType.FOREST;
                case "ginger_carpet" -> DecorType.GINGER;
                case "indigo_carpet" -> DecorType.INDIGO;
                case "maroon_carpet" -> DecorType.MAROON;
                case "mint_carpet" -> DecorType.MINT;
                case "navy_carpet" -> DecorType.NAVY;
                case "olive_carpet" -> DecorType.OLIVE;
                case "rose_carpet" -> DecorType.ROSE;
                case "slate_carpet" -> DecorType.SLATE;
                case "tan_carpet" -> DecorType.TAN;
                case "teal_carpet" -> DecorType.TEAL;
                case "verdant_carpet" -> DecorType.VERDANT;
                default -> null;
            };
        }
        return null;
    }

    private Item getCarpetFromDecor() {
        for (Map.Entry<Item, DecorType> entry : CARPET_TO_DECOR.entrySet()) {
            if (entry.getValue() == this.getDecor()) {
                return entry.getKey();
            }
        }
        if (ModCompat.DYE_DEPOT_LOADED) {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation("dye_depot", this.getDecor().name().toLowerCase() + "_carpet"));
        }
        return Items.AIR;
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.HEAD;
    }

    // ==========================================
    // ENVIRONMENT & PHYSICS
    // ==========================================
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isInWater()) {
            super.travel(travelVector);
            Vec3 motion = this.getDeltaMovement();
            if (motion.y > 0) {
                this.setDeltaMovement(motion.x, motion.y * 0.1D, motion.z);
            }
            return;
        }
        super.travel(travelVector);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !(effect.getEffect() == MobEffects.POISON || effect.getEffect() == MobEffects.WITHER);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return !this.isAttacking();
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.isAttacking()) return;
        super.push(x, y, z);
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        if (this.isAttacking()) return;
        super.knockback(strength, xRatio, zRatio);
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(1.4F, 3.7F);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        BlockPos pos = this.blockPosition();
        return level.noCollision(this, this.getBoundingBox()) && level.getBlockState(pos.below()).entityCanStandOn(level, pos.below(), this);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, this.getBbHeight() * 0.75D, this.getBbWidth() * 0.4D);
    }

    // ==========================================
    // SYNCHED DATA ACCESSORS & STATES
    // ==========================================
    public boolean isPlayerCreated() {
        return this.entityData.get(PLAYER_CREATED);
    }

    public void setPlayerCreated(boolean value) {
        this.entityData.set(PLAYER_CREATED, value);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public AttackType getAttackType() {
        return AttackType.values()[this.entityData.get(ATTACK_TYPE)];
    }

    public void setAttack(AttackType type) {
        this.entityData.set(ATTACKING, type != AttackType.NONE);
        this.entityData.set(ATTACK_TYPE, type.ordinal());

        if (type != AttackType.NONE) {
            this.attackTicks = 0;
        }

        LivingEntity target = this.getTarget();
        if (target != null) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            this.lockedYaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        } else {
            this.lockedYaw = this.getYRot();
        }

        this.lockedBodyYaw = this.lockedYaw;
        this.rotationLocked = true;
        this.lockedYaw = this.getYRot();
        this.lockedBodyYaw = this.yBodyRot;
    }

    public void clearAttack() {
        this.entityData.set(ATTACKING, false);
        this.entityData.set(ATTACK_TYPE, AttackType.NONE.ordinal());
        this.attackTicks = 0;
        this.rotationLocked = false;
    }

    public boolean isAttackLocked() {
        return isAttacking() && attackTicks < getAttackLength(getAttackType());
    }

    public boolean isHostileToPlayer() {
        return this.entityData.get(HOSTILE_TO_PLAYER);
    }

    // ==========================================
    // PARTICLES & SOUNDS
    // ==========================================
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.VGOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.VGOLEM_DEATH.get();
    }

    private void playHostileActivationEffect() {
        if (!(this.level() instanceof ServerLevel server)) return;

        server.playSound(null, this.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1.0F, 1.0F);

        Vec3 head = this.getEyePosition();
        double radius = 1.2D;
        int particles = 28;

        for (int i = 0; i < particles; i++) {
            double angle = (Math.PI * 2D) * i / particles;
            double x = head.x + Math.cos(angle) * radius;
            double z = head.z + Math.sin(angle) * radius;

            server.sendParticles(ParticleTypes.POOF, x, head.y, z, 1, 0.0D, 0.02D, 0.0D, 0.0D);
        }
    }

    public void spawnGroundShockwaveParticles(double radius, int count, boolean heavy) {
        if (!(this.level() instanceof ServerLevel server)) return;

        BlockPos groundPos = this.blockPosition().below();
        BlockState ground = this.level().getBlockState(groundPos);

        if (ground.isAir()) return;

        RandomSource rand = this.random;
        double centerX = this.getX();
        double centerY = this.getY();
        double centerZ = this.getZ();

        if (!heavy) {
            Vec3 forward = Vec3.directionFromRotation(0, this.yBodyRot).normalize();
            Vec3 right = new Vec3(-forward.z, 0, forward.x);
            double impactX = centerX + forward.x * 2.0D + right.x * 0.5D;
            double impactZ = centerZ + forward.z * 2.0D + right.z * 0.5D;

            int particleCount = 20;
            double shockRadius = 1.2D;

            for (int i = 0; i < particleCount; i++) {
                double angle = rand.nextDouble() * Math.PI * 2;
                double dist = rand.nextDouble() * shockRadius;
                double x = impactX + Math.cos(angle) * dist;
                double z = impactZ + Math.sin(angle) * dist;

                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), x, centerY + 0.08D, z, 1, 0.0D, 0.04D, 0.0D, 0.02D);
            }
        }

        if (heavy) {
            Vec3 forward = Vec3.directionFromRotation(0, this.yBodyRot).normalize();
            double impactDistance = 3.0D;
            double impactX = centerX + forward.x * impactDistance;
            double impactZ = centerZ + forward.z * impactDistance;

            for (int i = 0; i < 30; i++) {
                double vx = (rand.nextDouble() - 0.5D) * 0.5D;
                double vz = (rand.nextDouble() - 0.5D) * 0.5D;
                double vy = 0.6D + rand.nextDouble() * 1.6D;

                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), impactX, centerY + 0.05D, impactZ, 3, vx, vy, vz, 0.0D);
            }

            int rings = 5;
            for (int r = 0; r < rings; r++) {
                double ringRadius = (0.4D + r * 1.1D) * 0.4D;
                server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, impactX, centerY + 0.15D, impactZ, 40, ringRadius, 0.15D, ringRadius, 0.03D);
            }
        }
    }

    // ==========================================
    // SPAWNING & SAVING
    // ==========================================
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("PlayerCreated", this.isPlayerCreated());
        tag.putInt(NBT_DECOR, this.getDecor().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(NBT_DECOR, Tag.TAG_INT)) {
            int ordinal = tag.getInt(NBT_DECOR);
            if (ordinal >= 0 && ordinal < DecorType.values().length) {
                this.setDecor(DecorType.values()[ordinal]);
            }
        }
        if (tag.contains("PlayerCreated")) {
            this.setPlayerCreated(tag.getBoolean("PlayerCreated"));
        }
    }

    @Nullable
    private ItemStack getDecorCarpetDrop() {
        DecorType decor = this.getDecor();
        if (decor == DecorType.NONE) return ItemStack.EMPTY;
        Item carpet = getCarpetFromDecor();
        if (carpet == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(carpet);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource source) {
        super.dropAllDeathLoot(source);

        if (this.level().isClientSide) return;

        int nuggets = 4 + this.random.nextInt(5);
        this.spawnAtLocation(new ItemStack(ModItems.VANADIUM_NUGGET.get(), nuggets));

        if (this.hasDecor()) {
            ItemStack carpet = getDecorCarpetDrop();
            if (!carpet.isEmpty()) {
                this.spawnAtLocation(carpet);
            }
        }

        if (this.random.nextFloat() < 0.5F) {
            List<Block> flowers = BuiltInRegistries.BLOCK.stream()
                    .filter(block -> block instanceof FlowerBlock)
                    .filter(block -> {
                        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                        return id != null && !BLACKLISTED_FLOWERS.contains(id);
                    })
                    .collect(Collectors.toList());

            if (!flowers.isEmpty()) {
                Block chosen = flowers.get(this.random.nextInt(flowers.size()));
                this.spawnAtLocation(new ItemStack(chosen));
            }
        }
    }
}