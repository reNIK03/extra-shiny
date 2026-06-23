package net.r_nik.extrashiny.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ExtraShiny.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<VanadiumPartisanEntity>> VANADIUM_PARTISAN_ENTITY =
            ENTITY_TYPES.register("vanadium_partisan_entity",
                    () -> EntityType.Builder.<VanadiumPartisanEntity>of(
                                    VanadiumPartisanEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("vanadium_partisan_entity"));

    public static final DeferredHolder<EntityType<?>, EntityType<VanadiumGolemEntity>> VANADIUM_GOLEM =
            ENTITY_TYPES.register("vanadium_golem",
                    () -> EntityType.Builder.of(
                                    VanadiumGolemEntity::new, MobCategory.MISC)
                            .sized(1.4F, 3.7F)
                            .clientTrackingRange(10)
                            .updateInterval(3)
                            .build("vanadium_golem"));

    public static final DeferredHolder<EntityType<?>, EntityType<AuroralArrowEntity>> AURORAL_ARROW =
            ENTITY_TYPES.register("auroral_arrow", () ->
                    EntityType.Builder.<AuroralArrowEntity>of(AuroralArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("auroral_arrow")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<EnforcerEntity>> ENFORCER =
            ENTITY_TYPES.register("enforcer",
                    () -> EntityType.Builder.of(EnforcerEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 1.2F)
                            .clientTrackingRange(12)
                            .updateInterval(3)
                            .build("enforcer"));

}