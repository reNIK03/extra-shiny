package net.r_nik.extrashiny.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExtraShiny.MOD_ID);

    public static final RegistryObject<EntityType<VanadiumPartisanEntity>> VANADIUM_PARTISAN_ENTITY =
            ENTITY_TYPES.register("vanadium_partisan_entity",
                    () -> EntityType.Builder.<VanadiumPartisanEntity>of(
                                    VanadiumPartisanEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("vanadium_partisan_entity"));

    public static final RegistryObject<EntityType<VanadiumGolemEntity>> VANADIUM_GOLEM =
            ENTITY_TYPES.register("vanadium_golem",
                    () -> EntityType.Builder.of(
                                    VanadiumGolemEntity::new, MobCategory.MISC)
                            .sized(1.4F, 3.7F)
                            .clientTrackingRange(10)
                            .updateInterval(3)
                            .build("vanadium_golem"));

    public static final RegistryObject<EntityType<AuroralArrowEntity>> AURORAL_ARROW =
            ENTITY_TYPES.register("auroral_arrow", () ->
                    EntityType.Builder.<AuroralArrowEntity>of(AuroralArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("auroral_arrow")
            );

    public static final RegistryObject<EntityType<EnforcerEntity>> ENFORCER =
            ENTITY_TYPES.register("enforcer",
                    () -> EntityType.Builder.of(EnforcerEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 1.2F)
                            .clientTrackingRange(12)
                            .updateInterval(3)
                            .build("enforcer"));

}
