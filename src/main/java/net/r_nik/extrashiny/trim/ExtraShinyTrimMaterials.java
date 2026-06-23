package net.r_nik.extrashiny.trim;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModArmorMaterials;
import net.r_nik.extrashiny.item.ModItems;

import java.util.Map;

public class ExtraShinyTrimMaterials {
    public static final ResourceKey<TrimMaterial> VANADIUM = registerKey("vanadium");
    public static final ResourceKey<TrimMaterial> VANADIUM_DARKER = registerKey("vanadium_darker");
    public static final ResourceKey<TrimMaterial> LABRADORITE = registerKey("labradorite");
    public static final ResourceKey<TrimMaterial> OSMIUM = registerKey("osmium");
    public static final ResourceKey<TrimMaterial> OSMIUM_DARKER = registerKey("osmium_darker");
    public static final ResourceKey<TrimMaterial> CIMMERIAN = registerKey("cimmerian");
    public static final ResourceKey<TrimMaterial> CIMMERIAN_DARKER = registerKey("cimmerian_darker");
    public static final ResourceKey<TrimMaterial> DAMASK = registerKey("damask");
    public static final ResourceKey<TrimMaterial> DAMASK_DARKER = registerKey("damask_darker");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, VANADIUM, ModItems.VANADIUM_INGOT.get(), Style.EMPTY.withColor(0x9C9AAF), overrides(ModArmorMaterials.VANADIUM, VANADIUM_DARKER));
        register(context, OSMIUM, ModItems.OSMIUM_INGOT.get(), Style.EMPTY.withColor(0x869bd8), overrides(ModArmorMaterials.OSMIUM, OSMIUM_DARKER));
        register(context, CIMMERIAN, ModItems.ANCIENT_LATTICE.get(), Style.EMPTY.withColor(0x4e3850), overrides(ModArmorMaterials.CIMMERIAN, CIMMERIAN_DARKER));
        register(context, DAMASK, ModItems.DAMASK_INGOT.get(), Style.EMPTY.withColor(0x886674), overrides(ModArmorMaterials.DAMASK, DAMASK_DARKER));

        register(context, LABRADORITE, ModItems.LABRADORITE.get(), Style.EMPTY.withColor(0x5DA387), Map.of());

        register(context, VANADIUM_DARKER, ModItems.VANADIUM_INGOT.get(), Style.EMPTY.withColor(0x9C9AAF), Map.of());
        register(context, OSMIUM_DARKER, ModItems.OSMIUM_INGOT.get(), Style.EMPTY.withColor(0x869bd8), Map.of());
        register(context, CIMMERIAN_DARKER, ModItems.ANCIENT_LATTICE.get(), Style.EMPTY.withColor(0x4e3850), Map.of());
        register(context, DAMASK_DARKER, ModItems.DAMASK_INGOT.get(), Style.EMPTY.withColor(0x886674), Map.of());
    }

    private static ResourceKey<TrimMaterial> registerKey(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, name));
    }

    private static void register(
            BootstrapContext<TrimMaterial> context,
            ResourceKey<TrimMaterial> key,
            Item item,
            Style style,
            Map<Holder<ArmorMaterial>, String> overrides
    ) {
        ResourceLocation rl = key.location();
        context.register(key, new TrimMaterial(
                rl.getNamespace() + "_" + rl.getPath(),
                item.builtInRegistryHolder(),
                -1.0F,
                overrides,
                Component.translatable(Util.makeDescriptionId("trim_material", rl)).withStyle(style)
        ));
    }

    public static Map<Holder<ArmorMaterial>, String> overrides(Holder<ArmorMaterial> regularMaterial, ResourceKey<TrimMaterial> darker) {
        return Map.of(regularMaterial, darker.location().toString().replace(':', '_'));
    }
}