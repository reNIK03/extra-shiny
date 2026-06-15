package net.r_nik.extrashiny.trim;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.registries.ForgeRegistries;
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

    public static void bootstrap(BootstapContext<TrimMaterial> context) {
        register(context, VANADIUM, ModItems.VANADIUM_INGOT.get(), Style.EMPTY.withColor(0x9C9AAF), Map.of());
        register(context, VANADIUM_DARKER, ModItems.VANADIUM_INGOT.get(), Style.EMPTY.withColor(0x9C9AAF), Map.of());
        register(context, LABRADORITE, ModItems.LABRADORITE.get(), Style.EMPTY.withColor(0x5DA387), Map.of());
        register(context, OSMIUM, ModItems.OSMIUM_INGOT.get(), Style.EMPTY.withColor(0x869bd8), Map.of());
        register(context, OSMIUM_DARKER, ModItems.OSMIUM_INGOT.get(), Style.EMPTY.withColor(0x869bd8), Map.of());
        register(context, CIMMERIAN, ModItems.ANCIENT_LATTICE.get(), Style.EMPTY.withColor(0x4e3850), Map.of());
        register(context, CIMMERIAN_DARKER, ModItems.ANCIENT_LATTICE.get(), Style.EMPTY.withColor(0x4e3850), Map.of());
        register(context, DAMASK, ModItems.DAMASK_INGOT.get(), Style.EMPTY.withColor(0x886674), Map.of());
        register(context, DAMASK_DARKER, ModItems.DAMASK_INGOT.get(), Style.EMPTY.withColor(0x886674), Map.of());
    }

    public static void registerArmorMaterialOverrides() {
        registerArmorMaterialOverrides(VANADIUM, ModArmorMaterials.VANADIUM, VANADIUM_DARKER);
        registerArmorMaterialOverrides(OSMIUM, ModArmorMaterials.OSMIUM, OSMIUM_DARKER);
        registerArmorMaterialOverrides(CIMMERIAN, ModArmorMaterials.CIMMERIAN, CIMMERIAN_DARKER);
        registerArmorMaterialOverrides(DAMASK, ModArmorMaterials.DAMASK, DAMASK_DARKER);
    }

    public static void registerArmorMaterialOverrides(ResourceKey<TrimMaterial> trim, ArmorMaterial material, ResourceKey<TrimMaterial> darkerTrim) {
        BlueprintTrims.registerArmorMaterialOverrides(trim, Map.of(material, darkerTrim.location().getNamespace() + "_" + darkerTrim.location().getPath()));
    }

    private static ResourceKey<TrimMaterial> registerKey(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(ExtraShiny.MOD_ID, name)
        );
    }

    private static void register(
            BootstapContext<TrimMaterial> context,
            ResourceKey<TrimMaterial> key,
            Item item,
            Style style,
            Map<ArmorMaterials, String> overrides
    ) {
        ResourceLocation rl = key.location();
        context.register(key, new TrimMaterial(
                rl.getNamespace() + "_" + rl.getPath(),
                ForgeRegistries.ITEMS.getHolder(item).get(),
                -1.0F,
                overrides,
                Component.translatable("trim_material." + rl.toLanguageKey())
                        .withStyle(style)
        ));
    }
}
