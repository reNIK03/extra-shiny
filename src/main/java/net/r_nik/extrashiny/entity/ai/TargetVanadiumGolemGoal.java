package net.r_nik.extrashiny.entity.ai;

import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

public class TargetVanadiumGolemGoal
        extends NearestAttackableTargetGoal<VanadiumGolemEntity> {

    public TargetVanadiumGolemGoal(Monster mob) {
        super(
                mob,
                VanadiumGolemEntity.class,
                true,
                target -> {

                    if (mob instanceof Creeper) return false;
                    if (mob instanceof EnderMan) return false;

                    return true;
                }
        );
    }
}
