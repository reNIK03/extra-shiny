package net.r_nik.extrashiny.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.*;
import net.r_nik.extrashiny.client.model.*;
import net.r_nik.extrashiny.client.particle.*;
import net.r_nik.extrashiny.client.renderer.*;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.particle.ModParticleTypes;
import net.r_nik.extrashiny.screen.ModMenuTypes;
import net.r_nik.extrashiny.screen.RefiningTableScreen;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID, value = Dist.CLIENT)
public class ModClientBusEvents {

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModItemProperties.register();

            // ResourceLocation.withDefaultNamespace is the modern way to call "minecraft:property"
            ItemProperties.register(ModItems.VANADIUM_PARTISAN.get(),
                    ResourceLocation.withDefaultNamespace("throwing"),
                    (itemStack, level, entity, useDur) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F
            );


            ItemProperties.register(
                    ModItems.VANADIUM_REPEATER.get(),
                    ResourceLocation.withDefaultNamespace("pull"),
                    (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {
                        if (entity == null) return 0.0F;

                        // Calculate duration and remaining ticks
                        float totalDuration = (float) stack.getUseDuration(entity);
                        float remainingTicks = (float) entity.getUseItemRemainingTicks();

                        // Cast the item to your class
                        net.r_nik.extrashiny.item.VanadiumRepeaterItem repeaterItem =
                                (net.r_nik.extrashiny.item.VanadiumRepeaterItem) stack.getItem();

                        // FIX: Pass 'level' as the second argument to match your method signature
                        float chargeTime = (float) repeaterItem.getRepeaterChargeTime(stack, level);

                        return entity.getUseItem() != stack ? 0.0F : (totalDuration - remainingTicks) / chargeTime;
                    }
            );

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !net.minecraft.world.item.CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), ResourceLocation.withDefaultNamespace("charged"), (stack, level, entity, seed) -> {
                return entity != null && net.minecraft.world.item.CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.VANADIUM_REPEATER.get(), ResourceLocation.withDefaultNamespace("firework"), (stack, level, entity, seed) -> {
                return entity != null && net.minecraft.world.item.CrossbowItem.isCharged(stack) && net.r_nik.extrashiny.item.RepeaterHelper.stackHasFirework(stack) ? 1.0F : 0.0F;
            });
        });
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(
                ModMenuTypes.REFINING_TABLE_MENU.get(),
                RefiningTableScreen::new
        );
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(VanadiumPartisanModel.LAYER_LOCATION, VanadiumPartisanModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.VANADIUM_GOLEM_MAIN, VanadiumGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ENFORCER_MAIN, EnforcerModel::createBodyLayer);
        event.registerLayerDefinition(BulwarkModel.LAYER_LOCATION, BulwarkModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.AURORAL_DUST.get(), AuroralDustColorTransitionParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.AURORAL_TRAIL.get(), AuroralTrailParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.AURORAL_BOOM.get(), AuroralBoomParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VANADIUM_PARTISAN_ENTITY.get(), VanadiumPartisanRenderer::new);
        event.registerEntityRenderer(ModEntities.VANADIUM_GOLEM.get(), VanadiumGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.ENFORCER.get(), EnforcerRenderer::new);
        event.registerEntityRenderer(ModEntities.AURORAL_ARROW.get(), AuroralArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            // Replace canBeDepleted() with isDamageable()
            if (item.isDamageable(new ItemStack(item))) {
                event.register(item, new MemoryAlloyDecorator());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ModBlockEntityWithoutLevelRenderer.INSTANCE);
    }
}