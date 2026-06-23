package net.r_nik.extrashiny.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExtraShiny.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RefiningTableEntity>> REFINING_TABLE =
            BLOCK_ENTITIES.register("refining_table",
                    () -> BlockEntityType.Builder.of(
                            RefiningTableEntity::new,
                            ModBlocks.REFINING_TABLE.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}