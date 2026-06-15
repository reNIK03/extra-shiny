package net.r_nik.extrashiny.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

@Mod.EventBusSubscriber(
        modid = ExtraShiny.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class VanadiumGolemCreationHandler {

    private static final String VANADIUM_HEAL_COUNT = "VanadiumHealCount";
    private static final int REQUIRED_INGOTS = 10;

    @SubscribeEvent
    public static void onInteractIronGolem(PlayerInteractEvent.EntityInteract event) {

        if (!(event.getTarget() instanceof IronGolem ironGolem)) return;

        Player player = event.getEntity();
        Level level = player.level();

        if (level.isClientSide) return;

        ItemStack stack = player.getItemInHand(event.getHand());

        if (!stack.is(ModItems.VANADIUM_INGOT.get())) return;

        float current = ironGolem.getHealth();
        float max = ironGolem.getMaxHealth();

        if (current >= max * 0.5F) return;

        CompoundTag tag = ironGolem.getPersistentData();
        int healed = tag.getInt(VANADIUM_HEAL_COUNT);

        ironGolem.setHealth(max);

        healed++;
        tag.putInt(VANADIUM_HEAL_COUNT, healed);

        level.playSound(
                null,
                ironGolem.blockPosition(),
                SoundEvents.IRON_GOLEM_REPAIR,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );

        if (level instanceof ServerLevel server) {

            boolean glowingPhase = healed > 5;

            server.sendParticles(
                    glowingPhase
                            ? ParticleTypes.GLOW
                            : ParticleTypes.WAX_OFF,
                    ironGolem.getX(),
                    ironGolem.getY() + ironGolem.getBbHeight() * 0.6,
                    ironGolem.getZ(),
                    glowingPhase ? 40 : 25,
                    0.6D,
                    0.6D,
                    0.6D,
                    0.02D
            );
        }


        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        if (healed >= REQUIRED_INGOTS) {
            transformIronGolem(ironGolem);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static void transformIronGolem(IronGolem ironGolem) {

        Level level = ironGolem.level();

        if (level instanceof ServerLevel server) {

            server.sendParticles(
                    ParticleTypes.POOF,
                    ironGolem.getX(),
                    ironGolem.getY() + ironGolem.getBbHeight() * 0.5,
                    ironGolem.getZ(),
                    60,
                    0.8D,
                    1.0D,
                    0.8D,
                    0.05D
            );
        }

        VanadiumGolemEntity vanadium = new VanadiumGolemEntity(
                ModEntities.VANADIUM_GOLEM.get(),
                level
        );

        vanadium.moveTo(
                ironGolem.getX(),
                ironGolem.getY(),
                ironGolem.getZ(),
                ironGolem.getYRot(),
                ironGolem.getXRot()
        );

        vanadium.setHealth(vanadium.getMaxHealth());
        vanadium.setPlayerCreated(true);

        ironGolem.discard();
        level.addFreshEntity(vanadium);
    }

}
