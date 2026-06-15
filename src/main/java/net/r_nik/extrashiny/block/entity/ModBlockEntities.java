package net.r_nik.extrashiny.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExtraShiny.MOD_ID);

    public static final RegistryObject<BlockEntityType<RefiningTableEntity>> REFINING_TABLE =
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
