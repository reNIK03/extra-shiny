package net.r_nik.extrashiny.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

import java.util.EnumSet;
import java.util.List;

public class VanadiumGolemLookAtAnimalGoal extends Goal {

    private final VanadiumGolemEntity golem;
    private LivingEntity targetAnimal;
    private int lookTime;

    public VanadiumGolemLookAtAnimalGoal(VanadiumGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (golem.getTarget() != null) return false;
        if (golem.isAttacking()) return false;
        if (golem.getRandom().nextInt(120) != 0) return false; // rare

        List<Animal> animals = golem.level().getEntitiesOfClass(
                Animal.class,
                golem.getBoundingBox().inflate(8.0D),
                a -> a.isAlive()
        );

        if (animals.isEmpty()) return false;

        this.targetAnimal = animals.get(golem.getRandom().nextInt(animals.size()));
        return true;
    }

    @Override
    public void start() {
        this.lookTime = 30 + golem.getRandom().nextInt(80);
        golem.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return lookTime > 0
                && targetAnimal != null
                && targetAnimal.isAlive()
                && golem.getTarget() == null;
    }

    @Override
    public void tick() {
        golem.getLookControl().setLookAt(
                targetAnimal,
                30.0F,
                30.0F
        );
        lookTime--;
    }
}
