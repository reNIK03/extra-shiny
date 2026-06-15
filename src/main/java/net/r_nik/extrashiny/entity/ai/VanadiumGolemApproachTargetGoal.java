package net.r_nik.extrashiny.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

import java.util.EnumSet;

public class VanadiumGolemApproachTargetGoal extends Goal {

    private final VanadiumGolemEntity golem;
    private LivingEntity target;

    public VanadiumGolemApproachTargetGoal(VanadiumGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = golem.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (target instanceof Player && golem.isPlayerCreated()) return false;
        if (golem.isAttacking()) return false;
        return golem.distanceToSqr(target) > 9.0D;
    }

    @Override
    public void tick() {
        if (target == null) return;

        golem.getNavigation().moveTo(
                target,
                1.0D
        );
    }

    @Override
    public boolean canContinueToUse() {
        return target != null
                && target.isAlive()
                && !golem.isAttacking()
                && golem.distanceToSqr(target) > 9.0D;
    }

    @Override
    public void stop() {
        golem.getNavigation().stop();
        target = null;
    }
}
