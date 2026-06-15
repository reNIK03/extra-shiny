package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.datagen.client.ExtraShinySpriteSourceProvider;
import net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        ModWorldGenProvider registries =
                new ModWorldGenProvider(output, event.getLookupProvider());

        CompletableFuture<HolderLookup.Provider> lookup =
                registries.getRegistryProvider();

        generator.addProvider(event.includeServer(), registries);
        ModBlockTagGenerator blockTags =
                new ModBlockTagGenerator(output, lookup, helper);

        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(),
                new ModItemTagGenerator(output, lookup, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(),
                new ModTrimMaterialTagProvider(output, lookup, helper));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(output));

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new ExtraShinySpriteSourceProvider(output, helper));
    }
}


