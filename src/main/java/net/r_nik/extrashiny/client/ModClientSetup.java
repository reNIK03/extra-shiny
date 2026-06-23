package net.r_nik.extrashiny.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.item.ModItems;

@EventBusSubscriber(
        modid = ExtraShiny.MOD_ID,
        value = Dist.CLIENT
)
public class ModClientSetup {

    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(ModItems.MEMORY_ALLOY.get(), new MemoryAlloyDecorator());
    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.VANADIUM_BARS.get(),
                    RenderType.cutout()
            );
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.OSMIUM_BARS.get(),
                    RenderType.cutout()
            );
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.LEAP_RAIL.get(),
                    RenderType.cutout()
            );
        });
    }
}