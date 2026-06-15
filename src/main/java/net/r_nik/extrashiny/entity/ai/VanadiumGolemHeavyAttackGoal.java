package net.r_nik.extrashiny.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity.AttackType;

import java.util.EnumSet;
import java.util.List;

public class VanadiumGolemHeavyAttackGoal extends Goal {

    private static final int ATTACK_LENGTH = 77;
    private static final int DAMAGE_TICK = 24;

    private final VanadiumGolemEntity golem;
    private int tick;
    private boolean hasHit;
    private LivingEntity attackTarget;

    public VanadiumGolemHeavyAttackGoal(VanadiumGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = golem.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (golem.isAttackLocked()) return false;
        if (golem.getQueuedAttack() != AttackType.HEAVY) return false;
        if (target instanceof Player && !golem.canAttackPlayerTarget()) {
            return false;
        }
        return golem.distanceToSqr(target) <= 16.0D;
    }



    @Override
    public void start() {
        tick = 0;
        hasHit = false;
        attackTarget = golem.getTarget();

        if (attackTarget != null) {
            golem.setYRot((float)(Math.atan2(
                    attackTarget.getZ() - golem.getZ(),
                    attackTarget.getX() - golem.getX()
            ) * (180F / Math.PI)) - 90F);
            golem.yBodyRot = golem.getYRot();
        }

        golem.getNavigation().stop();
        golem.setAttack(AttackType.HEAVY);
        golem.alertNearbyGolems(attackTarget);

    }

    @Override
    public void tick() {
        if (tick < DAMAGE_TICK && attackTarget != null) {
            golem.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
        }

        tick++;

        if (!hasHit && tick >= DAMAGE_TICK) {
            hasHit = true;
            golem.swing(InteractionHand.MAIN_HAND);

            AABB area = golem.getAttackBox(2.0D, 6.0D, 3.0D, 6.0D);
            Vec3 forward = Vec3.directionFromRotation(0, golem.yBodyRot).normalize();

            List<LivingEntity> entities = golem.level()
                    .getEntitiesOfClass(
                            LivingEntity.class,
                            area,
                            golem::isValidAttackTarget
                    );
            golem.spawnGroundShockwaveParticles(
                    4.0D,
                    40,
                    true
            );



            for (LivingEntity e : entities) {
                e.knockback(2.5F, forward.x, forward.z);
                e.setDeltaMovement(e.getDeltaMovement().add(0, 0.85D, 0));
                golem.doHurtTarget(e);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return golem.getAttackType() == AttackType.HEAVY && tick < ATTACK_LENGTH;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        tick = 0;
        hasHit = false;

        golem.clearQueuedAttack();
        golem.clearAttack();
    }

}
