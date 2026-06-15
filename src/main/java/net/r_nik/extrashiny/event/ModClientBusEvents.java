package net.r_nik.extrashiny.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DustColorTransitionParticle;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.r_nik.extrashiny.client.MemoryAlloyDecorator;
import net.r_nik.extrashiny.client.ModBlockEntityWithoutLevelRenderer;
import net.r_nik.extrashiny.client.ModItemProperties;
import net.r_nik.extrashiny.client.model.BulwarkModel;
import net.r_nik.extrashiny.client.model.EnforcerModel;
import net.r_nik.extrashiny.client.model.VanadiumPartisanModel;
import net.r_nik.extrashiny.client.particle.AuroralBoomParticle;
import net.r_nik.extrashiny.client.particle.AuroralDustColorTransitionParticle;
import net.r_nik.extrashiny.client.particle.AuroralTrailParticle;
import net.r_nik.extrashiny.client.renderer.EnforcerRenderer;
import net.r_nik.extrashiny.client.renderer.VanadiumPartisanRenderer;
import net.r_nik.extrashiny.client.renderer.VanadiumGolemRenderer;
import net.r_nik.extrashiny.client.renderer.AuroralArrowRenderer;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.r_nik.extrashiny.particle.ModParticleTypes;
import net.r_nik.extrashiny.screen.ModMenuTypes;
import net.r_nik.extrashiny.screen.RefiningTableScreen;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.ModModelLayers;
import net.r_nik.extrashiny.client.model.VanadiumGolemModel;

import java.util.Optional;


@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientBusEvents {

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModItemProperties.register();

            ItemProperties.register(ModItems.VANADIUM_PARTISAN.get(),
                    new ResourceLocation("throwing"),
                    (itemStack, level, entity, useDur) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F
            );

            MenuScreens.register(
                    ModMenuTypes.REFINING_TABLE_MENU.get(),
                    RefiningTableScreen::new
            );

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), new ResourceLocation("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float)((net.r_nik.extrashiny.item.VanadiumRepeaterItem)stack.getItem()).getRepeaterChargeTime(stack);
                }
            });

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), new ResourceLocation("pulling"), (stack, level, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !net.minecraft.world.item.CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), new ResourceLocation("charged"), (stack, level, entity, seed) -> {
                return entity != null && net.minecraft.world.item.CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), new ResourceLocation("firework"), (stack, level, entity, seed) -> {
                return entity != null && net.minecraft.world.item.CrossbowItem.isCharged(stack) && net.r_nik.extrashiny.item.RepeaterHelper.stackHasFirework(stack) ? 1.0F : 0.0F;
            });
        });
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(
                VanadiumPartisanModel.LAYER_LOCATION,
                VanadiumPartisanModel::createBodyLayer
        );

        event.registerLayerDefinition(
                ModModelLayers.VANADIUM_GOLEM_MAIN,
                VanadiumGolemModel::createBodyLayer
        );

        event.registerLayerDefinition(
                ModModelLayers.ENFORCER_MAIN,
                EnforcerModel::createBodyLayer
        );

        event.registerLayerDefinition(
                BulwarkModel.LAYER_LOCATION,
                BulwarkModel::createBodyLayer
        );
    }


    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.AURORAL_DUST.get(), AuroralDustColorTransitionParticle.Provider::new);

        event.registerSpriteSet(ModParticleTypes.AURORAL_TRAIL.get(), AuroralTrailParticle.Provider::new);

        event.registerSpriteSet(ModParticleTypes.AURORAL_BOOM.get(), AuroralBoomParticle.Provider::new);
    }




    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(
                ModEntities.VANADIUM_PARTISAN_ENTITY.get(),
                VanadiumPartisanRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.VANADIUM_GOLEM.get(),
                VanadiumGolemRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.ENFORCER.get(),
                EnforcerRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.AURORAL_ARROW.get(),
                AuroralArrowRenderer::new
        );
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item.canBeDepleted()) {
                event.register(item, new MemoryAlloyDecorator());
            }
        }
    }



    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ModBlockEntityWithoutLevelRenderer.INSTANCE);
    }
}
