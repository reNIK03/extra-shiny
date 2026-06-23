package net.r_nik.extrashiny.enchant;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.r_nik.extrashiny.ExtraShiny;

public class ModEnchantments {

    // We now just define the Keys that point to our datapack/datagen entries
    public static final ResourceKey<Enchantment> GLACIATION = createKey("glaciation");
    public static final ResourceKey<Enchantment> SERRATION = createKey("serration");
    public static final ResourceKey<Enchantment> FROST_EDGE = createKey("frost_edge");

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(
                Registries.ENCHANTMENT,
                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, name)
        );
    }
}