package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.entity.EnforcerEntity;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

// 1.21.1: Removed 'bus' parameter as it is now inferred automatically
@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.VANADIUM_GOLEM.get(),
                VanadiumGolemEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ENFORCER.get(),
                EnforcerEntity.createAttributes().build()
        );
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.ENFORCER.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EnforcerEntity::canEnforcerSpawn,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}