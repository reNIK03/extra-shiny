package net.r_nik.extrashiny.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;

// The Dist.CLIENT parameter ensures this never accidentally loads on a dedicated server!
@EventBusSubscriber(modid = ExtraShiny.MOD_ID, value = Dist.CLIENT)
public class ClientItemExtensions {

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ModBlockEntityWithoutLevelRenderer.INSTANCE;
            }
        }, ModItems.BULWARK.get());

        // If you ever add more items that need custom BEWLRs, you can register them here too!
    }
}