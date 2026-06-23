package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.enchant.ModEnchantments;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends TagsProvider<Enchantment> {

    // 1. Define the custom Exclusive Set tags
    // Minecraft automatically treats any tag in the "exclusive_set" folder as a conflict group!
    public static final TagKey<Enchantment> FROST_EDGE_EXCLUSIVE = TagKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "exclusive_set/frost_edge")
    );
    public static final TagKey<Enchantment> GLACIATION_EXCLUSIVE = TagKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "exclusive_set/glaciation")
    );
    public static final TagKey<Enchantment> SERRATION_EXCLUSIVE = TagKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "exclusive_set/serration")
    );

    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.ENCHANTMENT, lookupProvider, ExtraShiny.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        // ==========================================
        // INCOMPATIBILITIES (Conflicts)
        // ==========================================

        // Frost Edge conflicts with Fire Aspect
        this.tag(FROST_EDGE_EXCLUSIVE)
                .add(ModEnchantments.FROST_EDGE)
                .add(Enchantments.FIRE_ASPECT);

        // Glaciation conflicts with Riptide and Channeling
        this.tag(GLACIATION_EXCLUSIVE)
                .add(ModEnchantments.GLACIATION)
                .add(Enchantments.RIPTIDE)
                .add(Enchantments.CHANNELING);

        // Serration conflicts with Impaling
        this.tag(SERRATION_EXCLUSIVE)
                .add(ModEnchantments.SERRATION)
                .add(Enchantments.IMPALING);

        // ==========================================
        // AVAILABILITY (Crucial for Survival Mode!)
        // ==========================================

        // Allow enchantments to appear in the Enchanting Table
        this.tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(ModEnchantments.FROST_EDGE)
                .add(ModEnchantments.GLACIATION)
                .add(ModEnchantments.SERRATION);

        // Allow enchantments to be found in chest loot
        this.tag(EnchantmentTags.ON_RANDOM_LOOT)
                .add(ModEnchantments.FROST_EDGE)
                .add(ModEnchantments.GLACIATION)
                .add(ModEnchantments.SERRATION);

        // Allow Villagers to sell them as Enchanted Books
        this.tag(EnchantmentTags.TRADEABLE)
                .add(ModEnchantments.FROST_EDGE)
                .add(ModEnchantments.GLACIATION)
                .add(ModEnchantments.SERRATION);

        // (Optional) Make Frost Edge a Treasure Enchantment instead of table-loot
        // If you want Frost Edge to act like Mending/Frost Walker, REMOVE it from IN_ENCHANTING_TABLE
        // and add it to EnchantmentTags.TREASURE instead!
    }
}