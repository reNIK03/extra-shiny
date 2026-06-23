package net.r_nik.extrashiny.entity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.AABB;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.BiConsumer;

public class EnforcerEntity extends Monster implements VibrationSystem {
    // ==========================================
    // CONSTANTS & TUNING
    // ==========================================
    private static final AttributeModifier AGGRO_SPEED_BONUS = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "enforcer_aggro_speed"),
            1.5D,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    private static final int VIBRATION_COOLDOWN_TICKS = 40;
    private static final float MOVE_TURN_SPEED_DEG_PER_TICK = 8.0F;

    private static final int HOWL_ANIM_TICKS = 80;
    private static final int HOWL_AFTER_DETECT_TICKS = 20 * 10;
    private static final int HOWL_AFTER_ATTACK_TICKS = 20 * 5;
    private static final int HOWL_SHRIEK_DELAY_TICKS = 30;
    private static final int HOWL_SHRIEK_BURST_COUNT = 10;
    private static final int HOWL_SHRIEK_INTERVAL_TICKS = 3;

    private static final int INVESTIGATE_DURATION_TICKS = 20 * 5;
    private static final double INVESTIGATE_STOP_DIST_SQR = 2.0D * 2.0D;
    private static final double INVESTIGATE_SPEED = 1.4D;

    private static final int LISTEN_RADIUS = 15;
    private static final int ANGER_MAX = 150;
    private static final int ANGER_ALERT = 80;
    private static final int ANGER_VIBRATION_NORMAL = 25;
    private static final int ANGER_VIBRATION_PROJECTILE = 15;
    private static final int ANGER_TOUCH = 35;
    private static final int ANGER_SNIFF = 10;

    private static final int HOWL_DURATION = 30;
    private static final int HOWL_COOLDOWN = 20 * 20;

    private static final int SNIFF_DURATION = 60;
    private static final int SNIFF_COOLDOWN_MIN = 100;
    private static final int SNIFF_COOLDOWN_MAX = 200;

    private static final double WARDEN_AVOID_RADIUS = 32.0;
    private static final double RALLY_RADIUS = 48.0;

    // ==========================================
    // SYNCHED DATA IDS
    // ==========================================
    private static final EntityDataAccessor<Boolean> SNIFFING = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HOWLING = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BITING = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> AGGRESSIVE = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HOWL_LOCK_YAW = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> CLIENT_ANGER = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SIGNAL_TICKS = SynchedEntityData.defineId(EnforcerEntity.class, EntityDataSerializers.INT);

    // ==========================================
    // FIELDS & VARIABLES
    // ==========================================
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sniffAnimationState = new AnimationState();
    public final AnimationState howlAnimationState = new AnimationState();
    public final AnimationState biteAnimationState = new AnimationState();

    private final Object2IntOpenHashMap<UUID> angerMap = new Object2IntOpenHashMap<>();
    private final VibrationSystem.Data vibrationData = new VibrationSystem.Data();
    private final VibrationSystem.User vibrationUser = new EnforcerVibrationUser();
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));

    private int vibrationCooldownTicks = 0;
    private boolean pendingRally = false;
    private int howlShriekRemaining = 0;
    private int howlShriekIntervalTimer = 0;
    private int howlShriekBurstTicks = 0;
    private int targetDetectedTick = -999999;
    private UUID detectedTargetId = null;
    private int lastAttackTick = -999999;
    private float howlLockedYaw = 0.0F;

    private int sniffCooldownTicks = 80;
    private int sniffTicks = 0;
    private int howlCooldownTicks = 0;
    private int howlTicks = 0;
    private int biteAnimTicks = 0;
    private int enragedSoundCooldown = 0;

    @Nullable
    private BlockPos investigatePos = null;
    private int investigateTicks = 0;
    private boolean sniffOnInvestigateArrival = false;

    // ==========================================
    // CONSTRUCTOR & INITIALIZATION
    // ==========================================
    public EnforcerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.14D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Warden.class, (float)WARDEN_AVOID_RADIUS, 2.8D, 3.4D));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(SNIFFING, false);
        builder.define(HOWLING, false);
        builder.define(BITING, false);
        builder.define(CLIENT_ANGER, 0);
        builder.define(SIGNAL_TICKS, 0);
        builder.define(AGGRESSIVE, false);
        builder.define(HOWL_LOCK_YAW, 0.0F);
    }

    // ==========================================
    // CORE TICK & MOVEMENT
    // ==========================================
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.idleAnimationState.startIfStopped(this.tickCount);

            if (this.isSniffing()) this.sniffAnimationState.startIfStopped(this.tickCount);
            else this.sniffAnimationState.stop();

            if (this.isHowling()) this.howlAnimationState.startIfStopped(this.tickCount);
            else this.howlAnimationState.stop();

            if (this.isBiting()) this.biteAnimationState.startIfStopped(this.tickCount);
            else this.biteAnimationState.stop();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (howlTicks > 0) {
            howlTicks--;

            if (this.isHowling() && howlTicks == 55) {
                this.playSound(ModSounds.ENFORCER_HOWL.get(), 2.0F, 1.0F);
            }

            if (this.isHowling() && howlTicks == (HOWL_ANIM_TICKS - HOWL_SHRIEK_DELAY_TICKS)) {
                howlShriekRemaining = HOWL_SHRIEK_BURST_COUNT;
                howlShriekIntervalTimer = 0;

                if (this.pendingRally) {
                    LivingEntity t = this.getTarget();
                    if (t != null && t.isAlive()) {
                        rallyNearbyEnforcers(t);
                    }
                    this.pendingRally = false;
                }
            }

            if (this.isHowling() && howlShriekRemaining > 0) {
                if (howlShriekIntervalTimer <= 0) {
                    spawnHowlShriekParticles();
                    howlShriekRemaining--;
                    howlShriekIntervalTimer = HOWL_SHRIEK_INTERVAL_TICKS;
                } else {
                    howlShriekIntervalTimer--;
                }
            }

            if (howlTicks == 0) {
                setHowling(false);
                howlShriekRemaining = 0;
                howlShriekIntervalTimer = 0;
                this.pendingRally = false;

                LivingEntity t = this.getTarget();
                if (!this.level().isClientSide && t != null && t.isAlive()) {
                    this.getNavigation().moveTo(t, 1.25D);
                }
            }
        }

        if (this.isHowling()) {
            this.getNavigation().stop();
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));

            float locked = getHowlLockYaw();
            this.setYRot(locked);
            this.yBodyRot = locked;
            this.yHeadRot = locked;
            return;
        }

        if (!this.level().isClientSide) {
            VibrationSystem.Ticker.tick(this.level(), this.vibrationData, this.vibrationUser);
        }

        int s = getSignalTicks();
        if (s > 0) setSignalTicks(s - 1);

        if (sniffCooldownTicks > 0) sniffCooldownTicks--;
        if (howlCooldownTicks > 0) howlCooldownTicks--;
        if (enragedSoundCooldown > 0) enragedSoundCooldown--;

        if (sniffTicks > 0) {
            sniffTicks--;
            if (sniffTicks == 0) {
                setSniffing(false);
                applySniffAnger();
            }
        }

        if (!this.level().isClientSide) {
            tickInvestigateVibration();
        }

        if (biteAnimTicks > 0) {
            biteAnimTicks--;
            if (biteAnimTicks == 0) setBiting(false);
        }

        rotateBodyTowardsMovement();

        if (vibrationCooldownTicks > 0) vibrationCooldownTicks--;

        if (sniffOnInvestigateArrival) {
            if (this.getTarget() == null && !this.isHowling() && !this.isBiting() && !this.isSniffing()) {
                sniffOnInvestigateArrival = false;
                startSniffNow();
            }
        }

        if (!this.level().isClientSide) {
            decayAnger();
            enforceWardenFear();
            updateTargetFromAnger();
            updateAggroSpeed();
            maybeStartSniff();
            maybeStartHowl();

            LivingEntity t = this.getTarget();
            this.setAggressiveSynced(t != null && t.isAlive());

            syncAngerToClient();
        }
    }

    private void tickInvestigateVibration() {
        if (investigateTicks > 0) investigateTicks--;

        if (this.getTarget() != null) {
            investigateTicks = 0;
            investigatePos = null;
            return;
        }

        if (investigatePos == null || investigateTicks <= 0) {
            investigatePos = null;
            return;
        }

        if (this.isSniffing() || this.isHowling() || this.isBiting()) {
            this.getNavigation().stop();
            return;
        }

        double dx = (investigatePos.getX() + 0.5D) - this.getX();
        double dy = (investigatePos.getY()) - this.getY();
        double dz = (investigatePos.getZ() + 0.5D) - this.getZ();
        double distSqr = dx * dx + dy * dy + dz * dz;

        if (distSqr <= INVESTIGATE_STOP_DIST_SQR) {
            investigateTicks = 0;
            investigatePos = null;
            this.getNavigation().stop();
            sniffOnInvestigateArrival = true;
            return;
        }

        if (this.getNavigation().isDone() || this.tickCount % 10 == 0) {
            this.getNavigation().moveTo(
                    investigatePos.getX() + 0.5D,
                    investigatePos.getY(),
                    investigatePos.getZ() + 0.5D,
                    INVESTIGATE_SPEED
            );
        }
    }

    private void rotateBodyTowardsMovement() {
        var v = this.getDeltaMovement();
        double dx = v.x;
        double dz = v.z;

        double speedSqr = dx * dx + dz * dz;
        if (speedSqr < 1.0E-4) return;

        float desiredYaw = (float)(Mth.atan2(dz, dx) * (180F / (float)Math.PI)) - 90.0F;
        float currentYaw = this.getYRot();
        float newYaw = Mth.approachDegrees(currentYaw, desiredYaw, MOVE_TURN_SPEED_DEG_PER_TICK);

        this.setYRot(newYaw);
        this.yBodyRot = newYaw;
        this.yHeadRot = newYaw;
    }

    private void updateAggroSpeed() {
        AttributeInstance inst = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (inst == null) return;

        boolean shouldBoost = this.getTarget() != null && this.getTarget().isAlive();

        if (shouldBoost) {
            // Apply by ResourceLocation key instead of UUID
            if (!inst.hasModifier(AGGRO_SPEED_BONUS.id())) {
                inst.addTransientModifier(AGGRO_SPEED_BONUS);
            }
        } else {
            inst.removeModifier(AGGRO_SPEED_BONUS.id());
        }
    }

    // ==========================================
    // COMBAT & TARGETING
    // ==========================================
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        LivingEntity prev = this.getTarget();
        super.setTarget(target);

        if (this.level().isClientSide) return;

        if (prev == null && target != null && this.enragedSoundCooldown <= 0) {
            float pitch = 0.9F + this.random.nextFloat() * 0.2F;
            this.playSound(ModSounds.ENFORCER_ENRAGED.get(), 1.5F, pitch);
            this.enragedSoundCooldown = 200;
        }

        if (target == null) {
            detectedTargetId = null;
            return;
        }

        UUID id = target.getUUID();
        if (prev == null || detectedTargetId == null || !id.equals(detectedTargetId)) {
            detectedTargetId = id;
            targetDetectedTick = this.tickCount;
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.isHowling()) return false;

        boolean ok = super.doHurtTarget(target);

        if (ok && !this.level().isClientSide) {
            lastAttackTick = this.tickCount;
            float pitch = 0.9F + this.random.nextFloat() * 0.2F;
            this.playSound(ModSounds.ENFORCER_BITE.get(), 1.0F, pitch);
        }

        setBiting(true);
        biteAnimTicks = 12;

        return ok;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ok = super.hurt(source, amount);
        if (!ok) return false;

        if (!this.level().isClientSide) {
            Entity attacker = source.getEntity();

            if (attacker instanceof EnforcerEntity) return true;

            if (attacker instanceof LivingEntity le && isValidSuspect(le)) {
                boolean wasPassive = (this.getTarget() == null) || isInvestigatingVibration();

                angerMap.put(le.getUUID(), ANGER_MAX);
                this.setTarget(le);

                if (wasPassive) {
                    startHowlAbsolute(true);
                }
            }
        }

        return true;
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);

        if (this.level().isClientSide) return;
        if (entity instanceof EnforcerEntity) return;

        if (entity instanceof LivingEntity le && isValidSuspect(le)) {
            addAnger(le, ANGER_TOUCH);
        }
    }

    private void enforceWardenFear() {
        if (!(this.level() instanceof ServerLevel)) return;

        boolean wardenNearby = !this.level().getEntitiesOfClass(
                Warden.class,
                this.getBoundingBox().inflate(WARDEN_AVOID_RADIUS)
        ).isEmpty();

        if (wardenNearby) {
            this.setTarget(null);
        }
    }

    // ==========================================
    // ANGER MANAGEMENT
    // ==========================================
    private void decayAnger() {
        if (this.tickCount % 20 != 0) return;

        angerMap.object2IntEntrySet().removeIf(entry -> {
            int v = entry.getIntValue() - 1;
            if (v <= 0) return true;
            entry.setValue(v);
            return false;
        });
    }

    private void updateTargetFromAnger() {
        if (this.getTarget() == null) {
            LivingEntity best = findBestAngerTarget();
            if (best != null && getAnger(best) >= ANGER_ALERT) {
                this.setTarget(best);
            }
        } else {
            LivingEntity t = this.getTarget();
            if (t == null || !t.isAlive() || getAnger(t) <= 0) {
                this.setTarget(null);
            }
        }

        LivingEntity current = this.getTarget();
        Player angryPlayer = findAnyAngryPlayer();
        if (angryPlayer != null && angryPlayer.isAlive()) {
            if (current == null || !(current instanceof Player)) {
                if (getAnger(angryPlayer) > 0) this.setTarget(angryPlayer);
            }
        }
    }

    @Nullable
    private LivingEntity findBestAngerTarget() {
        return this.level().getEntitiesOfClass(
                        LivingEntity.class,
                        this.getBoundingBox().inflate(49.0D, 51.0D, 49.0D),
                        this::isValidSuspect
                ).stream()
                .max(Comparator.comparingInt(this::getAnger))
                .orElse(null);
    }

    @Nullable
    private Player findAnyAngryPlayer() {
        return this.level().getEntitiesOfClass(
                        Player.class,
                        this.getBoundingBox().inflate(49.0D, 51.0D, 49.0D),
                        p -> isValidSuspect(p) && getAnger(p) > 0
                ).stream()
                .max(Comparator.comparingInt(this::getAnger))
                .orElse(null);
    }

    private void addAnger(LivingEntity who, int amount) {
        UUID id = who.getUUID();
        int v = angerMap.getInt(id);
        v = Mth.clamp(v + amount, 0, ANGER_MAX);
        angerMap.put(id, v);
    }

    private int getAnger(LivingEntity who) {
        return angerMap.getInt(who.getUUID());
    }

    private int getAnger(UUID id) {
        return angerMap.getInt(id);
    }

    private boolean isValidSuspect(LivingEntity e) {
        if (e == null) return false;
        if (!e.isAlive()) return false;
        if (e instanceof EnforcerEntity) return false;
        if (e instanceof Warden) return false;
        if (e == this) return false;
        if (e instanceof Player p) {
            if (p.isCreative() || p.isSpectator()) return false;
        }
        return true;
    }

    private void syncAngerToClient() {
        int max = 0;
        for (UUID id : angerMap.keySet()) {
            max = Math.max(max, getAnger(id));
        }
        this.entityData.set(CLIENT_ANGER, max);
    }

    // ==========================================
    // HOWL BEHAVIOR
    // ==========================================
    private void maybeStartHowl() {
        if (howlCooldownTicks > 0) return;
        if (this.isHowling()) return;

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return;

        if (detectedTargetId == null || !detectedTargetId.equals(target.getUUID())) return;
        if ((this.tickCount - targetDetectedTick) < HOWL_AFTER_DETECT_TICKS) return;
        if ((this.tickCount - lastAttackTick) < HOWL_AFTER_ATTACK_TICKS) return;

        startHowlAbsolute(true);
    }

    private void rallyNearbyEnforcers(LivingEntity target) {
        for (EnforcerEntity ally : this.level().getEntitiesOfClass(
                EnforcerEntity.class,
                this.getBoundingBox().inflate(RALLY_RADIUS),
                e -> e != this && e.isAlive()
        )) {
            ally.addAnger(target, ANGER_ALERT);
            ally.silenceEnragedSound(200);
            ally.setTarget(target);
        }
    }

    private void startHowlAbsolute(boolean rallyAllies) {
        if (this.isHowling()) return;

        setSniffing(false);
        sniffTicks = 0;
        sniffOnInvestigateArrival = false;

        setBiting(false);
        biteAnimTicks = 0;

        investigatePos = null;
        investigateTicks = 0;

        setHowling(true);
        howlTicks = HOWL_ANIM_TICKS;
        howlCooldownTicks = HOWL_COOLDOWN;
        howlShriekBurstTicks = 0;

        setHowlLockYaw(this.getYRot());
        this.getNavigation().stop();

        this.pendingRally = rallyAllies;
    }

    private void startHowlAbsolute() {
        setSniffing(false);
        sniffTicks = 0;

        setBiting(false);
        biteAnimTicks = 0;

        setHowling(true);
        howlTicks = HOWL_DURATION;
        howlCooldownTicks = HOWL_COOLDOWN;

        investigateTicks = 0;
        investigatePos = null;
        sniffOnInvestigateArrival = false;

        this.getNavigation().stop();
        this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        this.setZza(0.0F);
        this.setXxa(0.0F);
        this.setYya(0.0F);
    }

    private void spawnHowlShriekParticles() {
        if (!(this.level() instanceof ServerLevel server)) return;

        float yawRad = this.getYRot() * Mth.DEG_TO_RAD;
        double fx = -Mth.sin(yawRad);
        double fz =  Mth.cos(yawRad);

        double px = this.getX() + fx * 0.5D;
        double pz = this.getZ() + fz * 0.5D;
        double py = this.getY() + (this.getBbHeight() * 0.5D) + 1.5D;

        server.sendParticles(new net.minecraft.core.particles.ShriekParticleOption(0),
                px, py, pz,
                1,
                0.0D, 0.0D, 0.0D,
                0.0D
        );
    }

    // ==========================================
    // SNIFF & INVESTIGATION BEHAVIOR
    // ==========================================
    private void maybeStartSniff() {
        if (this.getTarget() != null) return;
        if (this.isSniffing() || this.isHowling() || this.isBiting()) return;
        if (isInvestigatingVibration()) return;
        if (sniffCooldownTicks > 0) return;

        setSniffing(true);
        sniffTicks = SNIFF_DURATION;
        sniffCooldownTicks = this.random.nextIntBetweenInclusive(SNIFF_COOLDOWN_MIN, SNIFF_COOLDOWN_MAX);
    }

    private void startSniffNow() {
        if (this.getTarget() != null) return;
        if (this.isSniffing() || this.isHowling() || this.isBiting()) return;

        setSniffing(true);
        sniffTicks = SNIFF_DURATION;
        sniffCooldownTicks = this.random.nextIntBetweenInclusive(SNIFF_COOLDOWN_MIN, SNIFF_COOLDOWN_MAX);
    }

    private void applySniffAnger() {
        LivingEntity nearest = this.level().getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(this.blockPosition()).inflate(6.0D, 20.0D, 6.0D),
                        this::isValidSuspect
                ).stream()
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);

        if (nearest != null) {
            addAnger(nearest, ANGER_SNIFF);
        }
    }

    private boolean isInvestigatingVibration() {
        return investigatePos != null && investigateTicks > 0;
    }

    // ==========================================
    // SYNCHED DATA ACCESSORS & STATES
    // ==========================================
    public boolean isSniffing() {
        return this.entityData.get(SNIFFING);
    }

    private void setSniffing(boolean v) {
        this.entityData.set(SNIFFING, v);
    }

    public boolean isHowling() {
        return this.entityData.get(HOWLING);
    }

    private void setHowling(boolean v) {
        this.entityData.set(HOWLING, v);
    }

    public boolean isBiting() {
        return this.entityData.get(BITING);
    }

    private void setBiting(boolean v) {
        this.entityData.set(BITING, v);
    }

    private void setHowlLockYaw(float v) {
        this.entityData.set(HOWL_LOCK_YAW, v);
    }

    private float getHowlLockYaw() {
        return this.entityData.get(HOWL_LOCK_YAW);
    }

    public boolean isAggressiveSynced() {
        return this.entityData.get(AGGRESSIVE);
    }

    private void setAggressiveSynced(boolean v) {
        this.entityData.set(AGGRESSIVE, v);
    }

    public int getSignalTicks() {
        return this.entityData.get(SIGNAL_TICKS);
    }

    private void setSignalTicks(int v) {
        this.entityData.set(SIGNAL_TICKS, v);
    }

    public int getClientAnger() {
        return this.entityData.get(CLIENT_ANGER);
    }

    public void silenceEnragedSound(int ticks) {
        this.enragedSoundCooldown = ticks;
    }

    // ==========================================
    // VIBRATION SYSTEM
    // ==========================================
    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
        if (this.level() instanceof ServerLevel serverLevel) {
            consumer.accept(this.dynamicGameEventListener, serverLevel);
        }
    }

    private class EnforcerVibrationUser implements VibrationSystem.User {

        @Override
        public int getListenerRadius() {
            return LISTEN_RADIUS;
        }

        @Override
        public PositionSource getPositionSource() {
            return new EntityPositionSource(EnforcerEntity.this, EnforcerEntity.this.getEyeHeight());
        }
        @Override
        public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
            if (!EnforcerEntity.this.isAlive()) return false;
            if (EnforcerEntity.this.vibrationCooldownTicks > 0) return false;

            boolean wardenNearby = !level.getEntitiesOfClass(
                    Warden.class,
                    EnforcerEntity.this.getBoundingBox().inflate(WARDEN_AVOID_RADIUS)
            ).isEmpty();
            if (wardenNearby) return false;

            Entity source = context.sourceEntity();
            if (source instanceof EnforcerEntity) return false;
            if (source instanceof LivingEntity le) {
                return EnforcerEntity.this.isValidSuspect(le);
            }

            return true;
        }

        @Override
        public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, @Nullable Entity sourceEntity, @Nullable Entity projectileEntity, float distance) {
            if (sourceEntity instanceof EnforcerEntity) return;

            EnforcerEntity.this.vibrationCooldownTicks = VIBRATION_COOLDOWN_TICKS;
            EnforcerEntity.this.setSignalTicks(18);

            if (EnforcerEntity.this.getTarget() == null) {
                BlockPos origin = (sourceEntity != null) ? sourceEntity.blockPosition() : pos;
                EnforcerEntity.this.investigatePos = origin;
                EnforcerEntity.this.investigateTicks = INVESTIGATE_DURATION_TICKS;
            }

            level.playSound(null, EnforcerEntity.this.blockPosition(), SoundEvents.SCULK_CLICKING, SoundSource.HOSTILE, 1.0F, 0.9F + level.random.nextFloat() * 0.2F);

            if (sourceEntity instanceof LivingEntity le && EnforcerEntity.this.isValidSuspect(le)) {
                int add = (projectileEntity != null) ? ANGER_VIBRATION_PROJECTILE : ANGER_VIBRATION_NORMAL;
                EnforcerEntity.this.addAnger(le, add);

                if (EnforcerEntity.this.getAnger(le) >= ANGER_ALERT) {
                    EnforcerEntity.this.setTarget(le);
                    EnforcerEntity.this.investigateTicks = 0;
                    EnforcerEntity.this.investigatePos = null;
                }
            }
        }
    }

    // ==========================================
    // SOUNDS & LOOT
    // ==========================================
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.ENFORCER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENFORCER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null || this.isHowling()) {
            return null;
        }
        return ModSounds.ENFORCER_IDLE.get();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);


        int looting = 0;
        if (source.getEntity() instanceof LivingEntity killer) {
            var registry = level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);

            var lootingHolder = registry.getHolderOrThrow(Enchantments.LOOTING);

            looting = EnchantmentHelper.getItemEnchantmentLevel(lootingHolder, killer.getMainHandItem());
        }

        float baseChance = 0.50F;
        float lootingBonus = 0.25F;
        float maxChance = 0.99F;

        float chance = baseChance + (looting * lootingBonus);
        if (chance > maxChance) chance = maxChance;

        if (this.random.nextFloat() < chance) {
            int amount = this.random.nextInt(looting + 2);
            if (amount > 0) {
                this.spawnAtLocation(new ItemStack(Items.ECHO_SHARD, amount));
            }
        }
    }

    // ==========================================
    // SPAWNING & SAVING
    // ==========================================
    public static boolean canEnforcerSpawn(EntityType<EnforcerEntity> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (level.getDifficulty() == Difficulty.PEACEFUL) return false;
        if (!Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) return false;
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) return false;
        if (!level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) return false;
        if (!level.getFluidState(pos).isEmpty()) return false;

        int moonPhase = level.getLevel().getMoonPhase();

        float cityChance = 0f;
        float darkChance = 0f;

        switch (moonPhase) {
            case 0 -> { cityChance = 0.00f; darkChance = 0.00f; }
            case 1, 7 -> { cityChance = 0.30f; darkChance = 0.05f; }
            case 2, 6 -> { cityChance = 0.50f; darkChance = 0.15f; }
            case 3, 5 -> { cityChance = 0.80f; darkChance = 0.40f; }
            case 4 -> { cityChance = 1.00f; darkChance = 1.00f; }
        }

        boolean isStructureSpawn = (reason == MobSpawnType.STRUCTURE);
        float currentChance = isStructureSpawn ? cityChance : darkChance;

        if (random.nextFloat() > currentChance) {
            return false;
        }

        BlockState floor = level.getBlockState(below);

        if (isStructureSpawn) {
            return floor.is(Blocks.SMOOTH_BASALT) || floor.is(Blocks.CHISELED_DEEPSLATE);
        } else {
            return floor.is(Blocks.SCULK) || floor.is(Blocks.DEEPSLATE);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ClientAnger", this.getClientAnger());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ClientAnger")) {
            this.entityData.set(CLIENT_ANGER, tag.getInt("ClientAnger"));
        }
    }
}