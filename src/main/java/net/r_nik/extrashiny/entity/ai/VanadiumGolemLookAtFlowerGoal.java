package net.r_nik.extrashiny.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

import java.util.EnumSet;

public class VanadiumGolemLookAtFlowerGoal extends Goal {

    private final VanadiumGolemEntity golem;
    private BlockPos flowerPos;
    private int lookTime;

    public VanadiumGolemLookAtFlowerGoal(VanadiumGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (golem.getTarget() != null) return false;
        if (golem.isAttacking()) return false;
        if (golem.getRandom().nextInt(160) != 0) return false;

        BlockPos base = golem.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(base.offset(-3, -1, -3), base.offset(3, 1, 3))) {
            BlockState state = golem.level().getBlockState(pos);
            if (state.is(BlockTags.FLOWERS)) {
                this.flowerPos = pos;
                return true;
            }
        }

        return false;
    }

    @Override
    public void start() {
        this.lookTime = 30 + golem.getRandom().nextInt(80);
        golem.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return lookTime > 0
                && golem.getTarget() == null;
    }

    @Override
    public void tick() {
        golem.getLookControl().setLookAt(
                flowerPos.getX() + 0.5,
                flowerPos.getY() + 0.5,
                flowerPos.getZ() + 0.5,
                30.0F,
                30.0F
        );
        lookTime--;
    }
}
