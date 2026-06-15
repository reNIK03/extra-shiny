package net.r_nik.extrashiny.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.EnforcerEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;


@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.ENFORCER.get(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EnforcerEntity::canEnforcerSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }



}
