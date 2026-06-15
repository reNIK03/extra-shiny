package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_,  @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ExtraShiny.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(ItemTags.SWORDS)
                .add(
                        ModItems.VANADIUM_SWORD.get(),
                        ModItems.OSMIUM_SWORD.get(),
                        ModItems.DAMASK_SWORD.get()
                );

        this.tag(ItemTags.PICKAXES)
                .add(
                        ModItems.VANADIUM_PICKAXE.get(),
                        ModItems.OSMIUM_PICKAXE.get(),
                        ModItems.DAMASK_PICKAXE.get()
                );

        this.tag(ItemTags.AXES)
                .add(
                        ModItems.VANADIUM_AXE.get(),
                        ModItems.OSMIUM_AXE.get(),
                        ModItems.DAMASK_AXE.get()
                );

        this.tag(ItemTags.SHOVELS)
                .add(
                        ModItems.VANADIUM_SHOVEL.get(),
                        ModItems.OSMIUM_SHOVEL.get(),
                        ModItems.DAMASK_SHOVEL.get()
                );

        this.tag(ItemTags.HOES)
                .add(
                        ModItems.VANADIUM_HOE.get(),
                        ModItems.OSMIUM_HOE.get(),
                        ModItems.DAMASK_HOE.get()
                );


        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(
                        ModItems.VANADIUM_HELMET.get(),
                        ModItems.VANADIUM_CHESTPLATE.get(),
                        ModItems.VANADIUM_LEGGINGS.get(),
                        ModItems.VANADIUM_BOOTS.get(),
                        ModItems.OSMIUM_HELMET.get(),
                        ModItems.OSMIUM_CHESTPLATE.get(),
                        ModItems.OSMIUM_LEGGINGS.get(),
                        ModItems.OSMIUM_BOOTS.get(),
                        ModItems.CIMMERIAN_HELMET.get(),
                        ModItems.CIMMERIAN_CHESTPLATE.get(),
                        ModItems.CIMMERIAN_LEGGINGS.get(),
                        ModItems.CIMMERIAN_BOOTS.get(),
                        ModItems.DAMASK_HELMET.get(),
                        ModItems.DAMASK_CHESTPLATE.get(),
                        ModItems.DAMASK_LEGGINGS.get(),
                        ModItems.DAMASK_BOOTS.get()
                );

        this.tag(ItemTags.BEACON_PAYMENT_ITEMS)
                .add(
                        ModItems.VANADIUM_INGOT.get(),
                        ModItems.OSMIUM_INGOT.get(),
                        ModItems.DAMASK_INGOT.get()
                );

        this.tag(ItemTags.TRIM_MATERIALS)
                .add(
                        ModItems.VANADIUM_INGOT.get(),
                        ModItems.OSMIUM_INGOT.get(),
                        ModItems.LABRADORITE.get(),
                        ModItems.ANCIENT_LATTICE.get(),
                        ModItems.DAMASK_INGOT.get()
                );

        this.tag(ItemTags.TRIM_TEMPLATES)
                .add(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        this.tag(ItemTags.ARROWS)
                .add(
                        ModItems.AURORAL_ARROW.get()
                );
    }



}
