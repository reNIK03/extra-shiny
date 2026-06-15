package net.r_nik.extrashiny;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.effect.ModEffects;
import net.r_nik.extrashiny.enchant.ModEnchantments;
import net.r_nik.extrashiny.entity.EnforcerEntity;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.event.ModDispenserBehaviors;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.loot.ModLootModifiers;
import net.r_nik.extrashiny.particle.ModParticleTypes;
import net.r_nik.extrashiny.potion.ModPotions;
import net.r_nik.extrashiny.block.entity.ModBlockEntities;
import net.r_nik.extrashiny.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.r_nik.extrashiny.screen.RefiningTableScreen;
import net.r_nik.extrashiny.sound.ModSounds;
import org.slf4j.Logger;
import net.r_nik.extrashiny.network.ModMessages;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtraShiny.MOD_ID)
public class ExtraShiny {
    public static final String MOD_ID = "extrashiny";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID, path);
    }


    public ExtraShiny() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModEntities.ENTITY_TYPES.register(modEventBus);

        ModAttributes.ATTRIBUTES.register(modEventBus);

        ModEnchantments.ENCHANTMENTS.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModSounds.register(modEventBus);


        modEventBus.addListener(this::commonSetup);

        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials.registerArmorMaterialOverrides();
            ModDispenserBehaviors.register();
        });
    }





    private void addCreative(BuildCreativeModeTabContentsEvent event) {
         if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
             event.accept(ModItems.VANADIUM_INGOT);
             event.accept(ModItems.RAW_VANADIUM);
             event.accept(ModItems.VANADIUM_NUGGET);
             event.accept(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE);
             event.accept(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE);

             event.accept(ModItems.LABRADORITE);

             event.accept(ModItems.OSMIUM_INGOT);
             event.accept(ModItems.RAW_OSMIUM);
             event.accept(ModItems.OSMIUM_NUGGET);

             event.accept(ModItems.ANCIENT_LATTICE);

             event.accept(ModItems.DAMASK_INGOT);
             event.accept(ModItems.DAMASK_NUGGET);
             event.accept(ModItems.MEMORY_ALLOY);
         }

        if(event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
           event.accept(ModBlocks.LEAP_RAIL);
        }

        if(event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.VANADIUM_SWORD);
            event.accept(ModItems.VANADIUM_PARTISAN);
            event.accept(ModItems.VANADIUM_REPEATER);
            event.accept(ModItems.VANADIUM_HELMET);
            event.accept(ModItems.VANADIUM_CHESTPLATE);
            event.accept(ModItems.VANADIUM_LEGGINGS);
            event.accept(ModItems.VANADIUM_BOOTS);
            event.accept(ModItems.VANADIUM_HORSE_ARMOR);
            event.accept(ModItems.AURORAL_ARROW);

            event.accept(ModItems.OSMIUM_SWORD);
            event.accept(ModItems.BULWARK);
            event.accept(ModItems.OSMIUM_HELMET);
            event.accept(ModItems.OSMIUM_CHESTPLATE);
            event.accept(ModItems.OSMIUM_LEGGINGS);
            event.accept(ModItems.OSMIUM_BOOTS);
            event.accept(ModItems.OSMIUM_HORSE_ARMOR);


            event.accept(ModItems.CIMMERIAN_HELMET);
            event.accept(ModItems.CIMMERIAN_CHESTPLATE);
            event.accept(ModItems.CIMMERIAN_LEGGINGS);
            event.accept(ModItems.CIMMERIAN_BOOTS);

            event.accept(ModItems.DAMASK_SWORD);
            event.accept(ModItems.DAMASK_HELMET);
            event.accept(ModItems.DAMASK_CHESTPLATE);
            event.accept(ModItems.DAMASK_LEGGINGS);
            event.accept(ModItems.DAMASK_BOOTS);
            event.accept(ModItems.DAMASK_HORSE_ARMOR);
        }


        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.VANADIUM_PICKAXE);
            event.accept(ModItems.VANADIUM_AXE);
            event.accept(ModItems.VANADIUM_SHOVEL);
            event.accept(ModItems.VANADIUM_HOE);
            event.accept(ModItems.RADAR);
            event.accept(ModItems.OSMIUM_PICKAXE);
            event.accept(ModItems.OSMIUM_AXE);
            event.accept(ModItems.OSMIUM_SHOVEL);
            event.accept(ModItems.OSMIUM_HOE);
            event.accept(ModItems.MOONDIAL);
            event.accept(ModItems.RECALIBRATED_RADAR);
            event.accept(ModItems.DAMASK_PICKAXE);
            event.accept(ModItems.DAMASK_AXE);
            event.accept(ModItems.DAMASK_SHOVEL);
            event.accept(ModItems.DAMASK_HOE);
            event.accept(ModItems.ORE_TRACKER);

        }

        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.REFINING_TABLE);
            event.accept(ModBlocks.OSMIUM_SPOTLIGHT);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.VANADIUM_BLOCK);
            event.accept(ModBlocks.VANADIUM_BARS);
            event.accept(ModBlocks.RAW_VANADIUM_BLOCK);
            event.accept(ModBlocks.VANADIUM_ORE);
            event.accept(ModBlocks.DEEPSLATE_VANADIUM_ORE);
            event.accept(ModBlocks.LABRADORITE_BLOCK);
            event.accept(ModBlocks.DEEPSLATE_LABRADORITE_ORE);
            event.accept(ModBlocks.LABRADORITE_ORE);
            event.accept(ModBlocks.LABRADORITE_BRICKS);
            event.accept(ModBlocks.LABRADORITE_BRICK_STAIRS);
            event.accept(ModBlocks.LABRADORITE_BRICK_SLAB);
            event.accept(ModBlocks.LABRADORITE_BRICK_WALL);
            event.accept(ModBlocks.LABRADORITE_PILLAR);
            event.accept(ModBlocks.LABRADORITE_LAMP);
            event.accept(ModBlocks.OSMIUM_BLOCK);
            event.accept(ModBlocks.OSMIUM_BARS);
            event.accept(ModBlocks.RAW_OSMIUM_BLOCK);
            event.accept(ModBlocks.OSMIUM_ORE);
            event.accept(ModBlocks.DEEPSLATE_OSMIUM_ORE);
            event.accept(ModBlocks.SPOTTED_BLACKSTONE);

            event.accept(ModBlocks.CIMMERIAN_BLOCK);
            event.accept(ModBlocks.DAMASK_BLOCK);

            event.accept(ModBlocks.VANADIUM_BRICKS);
            event.accept(ModBlocks.VANADIUM_BRICK_STAIRS);
            event.accept(ModBlocks.VANADIUM_BRICK_SLAB);
            event.accept(ModBlocks.VANADIUM_BRICK_WALL);
            event.accept(ModBlocks.CHISELED_VANADIUM_BRICKS);

            event.accept(ModBlocks.OSMIUM_BRICKS);
            event.accept(ModBlocks.OSMIUM_BRICK_STAIRS);
            event.accept(ModBlocks.OSMIUM_BRICK_SLAB);
            event.accept(ModBlocks.OSMIUM_BRICK_WALL);
            event.accept(ModBlocks.CHISELED_OSMIUM_BRICKS);
        }

        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.VANADIUM_GOLEM_SPAWN_EGG);
            event.accept(ModItems.ENFORCER_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
