package net.r_nik.extrashiny.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.datagen.client.ExtraShinySpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

// CRITICAL: Specify that this listens to the MOD bus!
@EventBusSubscriber(modid = ExtraShiny.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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

        ModEnchantmentTagsProvider enchantmentTags = new ModEnchantmentTagsProvider(output, lookup, helper);

        generator.addProvider(event.includeServer(), registries);

        ModBlockTagGenerator blockTags =
                new ModBlockTagGenerator(output, lookup, helper);

        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(),
                new ModItemTagGenerator(output, lookup, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(),
                new ModTrimMaterialTagProvider(output, lookup, helper));

        generator.addProvider(event.includeServer(), enchantmentTags);

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookup));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output, lookup));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(output, lookup));

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));

        generator.addProvider(event.includeClient(), new ExtraShinySpriteSourceProvider(output, lookup, helper));
    }
}