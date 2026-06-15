package net.r_nik.extrashiny.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity.AttackType;

import java.util.EnumSet;

public class VanadiumGolemSmallAttackGoal extends Goal {

    private static final int ATTACK_LENGTH = 29;
    private static final int DAMAGE_TICK = 3;

    private final VanadiumGolemEntity golem;
    private int tick;
    private boolean hasHit;
    private LivingEntity attackTarget;

    public VanadiumGolemSmallAttackGoal(VanadiumGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = golem.getTarget();

        if (target == null || !target.isAlive()) return false;
        if (golem.isAttacking()) return false;
        if (golem.getQueuedAttack() != AttackType.SMALL) return false;
        if (target instanceof Player && !golem.canAttackPlayerTarget()) {
            return false;
        }

        return golem.distanceToSqr(target) <= 9.0D;
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
        golem.setAttack(AttackType.SMALL);
        golem.alertNearbyGolems(attackTarget);
    }

    @Override
    public void tick() {
        if (tick < DAMAGE_TICK && attackTarget != null) {
            golem.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
        }

        tick++;

        if (!hasHit && tick >= DAMAGE_TICK && attackTarget != null && attackTarget.isAlive()) {
            hasHit = true;
            golem.swing(InteractionHand.MAIN_HAND);

            Vec3 forward = Vec3.directionFromRotation(0, golem.yBodyRot).normalize();

            attackTarget.knockback(0.6F, forward.x, forward.z);
            golem.doHurtTarget(attackTarget);
            golem.markTargetSubdued();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return golem.isAttacking() && tick < ATTACK_LENGTH;
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
