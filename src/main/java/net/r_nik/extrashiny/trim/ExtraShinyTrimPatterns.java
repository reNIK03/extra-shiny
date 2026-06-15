package net.r_nik.extrashiny.trim;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;

public class ExtraShinyTrimPatterns {

    public static final ResourceKey<TrimPattern> CIMMERIAN = createKey("cimmerian");
    public static final ResourceKey<TrimPattern> MEMORY = createKey("memory");
    public static final ResourceKey<TrimPattern> DAMASK = createKey("damask");


    public static void bootstrap(BootstapContext<TrimPattern> context) {
        register(context, CIMMERIAN, ModItems.ANCIENT_LATTICE.get());
        register(context, MEMORY, ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        register(context, DAMASK, ModItems.DAMASK_INGOT.get());
    }

    private static ResourceKey<TrimPattern> createKey(String name) {
        return ResourceKey.create(
                Registries.TRIM_PATTERN,
                new ResourceLocation(ExtraShiny.MOD_ID, name)
        );
    }

    private static void register(
            BootstapContext<TrimPattern> context,
            ResourceKey<TrimPattern> key,
            Item item
    ) {
        context.register(
                key,
                new TrimPattern(
                        key.location(),
                        ForgeRegistries.ITEMS.getHolder(item).orElseThrow(),
                        Component.translatable(
                                Util.makeDescriptionId("trim_pattern", key.location())
                        )
                )
        );
    }
}
