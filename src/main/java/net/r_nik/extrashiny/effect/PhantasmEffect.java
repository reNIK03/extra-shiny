package net.r_nik.extrashiny.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class PhantasmEffect extends MobEffect {

    private static final UUID PHANTASM_SPEED_UUID = UUID.fromString("b3e21a4f-5692-4f30-81f1-a123f15c102a");
    private static final UUID PHANTASM_ATK_SPEED_UUID = UUID.fromString("c4f32b50-67a3-5041-9202-b234026d213b");

    private static final double MULTIPLIER_PER_ENTITY = 0.05;

    public PhantasmEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x005b5b);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) return;

        ServerLevel level = (ServerLevel) entity.level();

        double radius = 5.0 + (amplifier * 2.0);

        AABB box = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != entity && e.distanceTo(entity) <= radius
        );

        int entityCount = nearbyEntities.size();

        updateAttributes(entity, entityCount);

        level.sendParticles(ParticleTypes.ENCHANT,
                entity.getX(), entity.getY() + 1.0, entity.getZ(),
                3, 0.5, 0.5, 0.5, 0.1);

        for (LivingEntity target : nearbyEntities) {
            level.sendParticles(ParticleTypes.SMOKE,
                    target.getX(), target.getY() + target.getBbHeight() + 0.2, target.getZ(),
                    2, 0.1, 0.1, 0.1, 0.0);
        }

        spawnRadiusParticles(level, entity, radius);
    }

    private void updateAttributes(LivingEntity entity, int entityCount) {
        AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) {
            speedAttr.removeModifier(PHANTASM_SPEED_UUID);
            if (entityCount > 0) {
                speedAttr.addTransientModifier(new AttributeModifier(
                        PHANTASM_SPEED_UUID, "Phantasm Speed",
                        entityCount * MULTIPLIER_PER_ENTITY, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }

        if (entity instanceof Player) {
            AttributeInstance attackSpeedAttr = entity.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeedAttr != null) {
                attackSpeedAttr.removeModifier(PHANTASM_ATK_SPEED_UUID);
                if (entityCount > 0) {
                    attackSpeedAttr.addTransientModifier(new AttributeModifier(
                            PHANTASM_ATK_SPEED_UUID, "Phantasm Attack Speed",
                            entityCount * MULTIPLIER_PER_ENTITY, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }

    private void spawnRadiusParticles(ServerLevel level, LivingEntity entity, double radius) {
        DustParticleOptions redstoneParticle = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);

        for (int i = 0; i < 360; i += 15) {
            double x = entity.getX() + radius * Math.cos(Math.toRadians(i));
            double z = entity.getZ() + radius * Math.sin(Math.toRadians(i));

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, entity.getY(), z);
            while (pos.getY() > level.getMinBuildHeight() && level.isEmptyBlock(pos)) {
                pos.move(Direction.DOWN);
            }

            level.sendParticles(redstoneParticle, x, pos.getY() + 1.05, z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
        AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttr != null) speedAttr.removeModifier(PHANTASM_SPEED_UUID);

        AttributeInstance attackSpeedAttr = entity.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeedAttr != null) attackSpeedAttr.removeModifier(PHANTASM_ATK_SPEED_UUID);
    }
}