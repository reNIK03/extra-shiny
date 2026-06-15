package net.r_nik.extrashiny.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.particle.AuroralDustColorTransitionOptions;
import net.r_nik.extrashiny.particle.ModParticleTypes;
import org.joml.Vector3f;

import java.util.List;

public class AuroralArrowEntity extends AbstractArrow {

    private double extrashiny$prevTrailX;
    private double extrashiny$prevTrailY;
    private double extrashiny$prevTrailZ;
    private boolean extrashiny$trailInit = false;

    public AuroralArrowEntity(EntityType<? extends AuroralArrowEntity> type, Level level) {
        super(type, level);
    }

    public AuroralArrowEntity(Level level, double x, double y, double z) {
        super(ModEntities.AURORAL_ARROW.get(), x, y, z, level);
    }

    public AuroralArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ModEntities.AURORAL_ARROW.get(), level);
    }

    public AuroralArrowEntity(Level level, LivingEntity shooter) {
        super(ModEntities.AURORAL_ARROW.get(), shooter, level);
        this.setBaseDamage(this.getBaseDamage() * 1.5D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) return;

        if (!this.isCritArrow()) {
            extrashiny$trailInit = false;
            return;
        }

        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (!extrashiny$trailInit) {
            extrashiny$trailInit = true;
            extrashiny$prevTrailX = x;
            extrashiny$prevTrailY = y;
            extrashiny$prevTrailZ = z;
            return;
        }

        double dxSeg = x - extrashiny$prevTrailX;
        double dySeg = y - extrashiny$prevTrailY;
        double dzSeg = z - extrashiny$prevTrailZ;

        double dist = Math.sqrt(dxSeg * dxSeg + dySeg * dySeg + dzSeg * dzSeg);

        double spacing = 0.07;

        int count = Math.max(1, (int) Math.ceil(dist / spacing));

        for (int i = 0; i <= count; i++) {
            double t = (double) i / (double) count;

            double px = extrashiny$prevTrailX + dxSeg * t;
            double py = extrashiny$prevTrailY + dySeg * t;
            double pz = extrashiny$prevTrailZ + dzSeg * t;

            double vx = (this.random.nextDouble() - 0.5) * 0.004;
            double vy = (this.random.nextDouble() - 0.5) * 0.004;
            double vz = (this.random.nextDouble() - 0.5) * 0.004;

            this.level().addParticle(
                    ModParticleTypes.AURORAL_TRAIL.get(),
                    px, py, pz,
                    vx, vy, vz
            );
        }

        extrashiny$prevTrailX = x;
        extrashiny$prevTrailY = y;
        extrashiny$prevTrailZ = z;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity targetEntity = hitResult.getEntity();

        if (!(targetEntity instanceof LivingEntity primaryTarget)) {
            super.onHitEntity(hitResult);
            return;
        }

        float hpBefore = primaryTarget.getHealth();

        super.onHitEntity(hitResult);

        float dealt = Math.max(0.0F, hpBefore - primaryTarget.getHealth());
        float extraDamage = dealt * 0.5F;

        if (!this.isCritArrow()) return;
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        Vec3 motion = this.getDeltaMovement();
        double len = motion.length();
        Vec3 dir = (len > 1.0E-6) ? motion.scale(1.0 / len) : this.getLookAngle();

        double forward = 0.6D;
        Vec3 p = this.position().add(dir.scale(forward));

        serverLevel.sendParticles(
                ModParticleTypes.AURORAL_BOOM.get(),
                p.x, p.y, p.z,
                1,
                0.0D, 0.0D, 0.0D,
                0.0D
        );

        Vector3f FROM = new Vector3f(199f / 255f, 235f / 255f, 136f / 255f);
        Vector3f TO   = new Vector3f(199f / 255f, 112f / 255f, 199f / 255f);

        int dustCount = 40;
        float dustScale = 1.0f;
        double radius = 2.0;

        for (int i = 0; i < dustCount; i++) {
            double u = this.random.nextDouble();
            double r = radius * Math.cbrt(u);

            double theta = this.random.nextDouble() * (Math.PI * 2.0);
            double phi = Math.acos(2.0 * this.random.nextDouble() - 1.0);

            double ox = r * Math.sin(phi) * Math.cos(theta);
            double oy = r * Math.cos(phi);
            double oz = r * Math.sin(phi) * Math.sin(theta);

            double vx = (this.random.nextDouble() - 0.5) * 0.03;
            double vy = (this.random.nextDouble() - 0.5) * 0.03;
            double vz = (this.random.nextDouble() - 0.5) * 0.03;

            serverLevel.sendParticles(
                    new AuroralDustColorTransitionOptions(FROM, TO, dustScale),
                    p.x + ox, p.y + oy, p.z + oz,
                    1,
                    vx, vy, vz,
                    0.0D
            );
        }

        AABB box = AABB.ofSize(p, 4.0D, 4.0D, 4.0D);

        DamageSource src = (this.getOwner() == null)
                ? this.damageSources().arrow(this, this)
                : this.damageSources().arrow(this, this.getOwner());

        List<LivingEntity> victims = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e.isAlive()
                        && e != this.getOwner()
                        && e != primaryTarget
                        && !e.isInvulnerableTo(src)
        );

        for (LivingEntity e : victims) {
            e.hurt(src, extraDamage);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.setSoundEvent(this.getDefaultHitGroundSoundEvent());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return super.getDefaultHitGroundSoundEvent();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.AURORAL_ARROW.get());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}