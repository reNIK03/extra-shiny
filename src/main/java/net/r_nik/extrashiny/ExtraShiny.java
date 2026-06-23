package net.r_nik.extrashiny;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.block.ModBlocks;
import net.r_nik.extrashiny.block.entity.ModBlockEntities;
import net.r_nik.extrashiny.block.entity.RefiningTableEntity;
import net.r_nik.extrashiny.effect.ModEffects;
import net.r_nik.extrashiny.enchant.ModEnchantments;
import net.r_nik.extrashiny.entity.ModEntities;
import net.r_nik.extrashiny.event.ModDispenserBehaviors;
import net.r_nik.extrashiny.item.ModArmorMaterials;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.loot.ModLootModifiers;
import net.r_nik.extrashiny.particle.ModParticleTypes;
import net.r_nik.extrashiny.potion.ModPotions;
import net.r_nik.extrashiny.screen.ModMenuTypes;
import net.r_nik.extrashiny.sound.ModSounds;
import net.r_nik.extrashiny.network.ModMessages;
import org.slf4j.Logger;

@Mod(ExtraShiny.MOD_ID)
public class ExtraShiny {
    public static final String MOD_ID = "extrashiny";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public ExtraShiny(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ExtraShiny::registerCapabilities);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            net.r_nik.extrashiny.trim.ExtraShinyTrimMaterials.registerArmorMaterialOverrides();
            ModDispenserBehaviors.register();
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.VANADIUM_INGOT.get());
            event.accept(ModItems.RAW_VANADIUM.get());
            event.accept(ModItems.VANADIUM_NUGGET.get());
            event.accept(ModItems.VANADIUM_UPGRADE_SMITHING_TEMPLATE.get());
            event.accept(ModItems.MEMORY_ARMOR_TRIM_SMITHING_TEMPLATE.get());
            event.accept(ModItems.LABRADORITE.get());
            event.accept(ModItems.OSMIUM_INGOT.get());
            event.accept(ModItems.RAW_OSMIUM.get());
            event.accept(ModItems.OSMIUM_NUGGET.get());
            event.accept(ModItems.ANCIENT_LATTICE.get());
            event.accept(ModItems.DAMASK_INGOT.get());
            event.accept(ModItems.DAMASK_NUGGET.get());
            event.accept(ModItems.MEMORY_ALLOY.get());
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.LEAP_RAIL.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.VANADIUM_SWORD.get());
            event.accept(ModItems.VANADIUM_PARTISAN.get());
            event.accept(ModItems.VANADIUM_REPEATER.get());
            event.accept(ModItems.VANADIUM_HELMET.get());
            event.accept(ModItems.VANADIUM_CHESTPLATE.get());
            event.accept(ModItems.VANADIUM_LEGGINGS.get());
            event.accept(ModItems.VANADIUM_BOOTS.get());
            event.accept(ModItems.VANADIUM_HORSE_ARMOR.get());
            event.accept(ModItems.AURORAL_ARROW.get());
            event.accept(ModItems.OSMIUM_SWORD.get());
            event.accept(ModItems.BULWARK.get());
            event.accept(ModItems.OSMIUM_HELMET.get());
            event.accept(ModItems.OSMIUM_CHESTPLATE.get());
            event.accept(ModItems.OSMIUM_LEGGINGS.get());
            event.accept(ModItems.OSMIUM_BOOTS.get());
            event.accept(ModItems.OSMIUM_HORSE_ARMOR.get());
            event.accept(ModItems.CIMMERIAN_HELMET.get());
            event.accept(ModItems.CIMMERIAN_CHESTPLATE.get());
            event.accept(ModItems.CIMMERIAN_LEGGINGS.get());
            event.accept(ModItems.CIMMERIAN_BOOTS.get());
            event.accept(ModItems.DAMASK_SWORD.get());
            event.accept(ModItems.DAMASK_HELMET.get());
            event.accept(ModItems.DAMASK_CHESTPLATE.get());
            event.accept(ModItems.DAMASK_LEGGINGS.get());
            event.accept(ModItems.DAMASK_BOOTS.get());
            event.accept(ModItems.DAMASK_HORSE_ARMOR.get());
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.VANADIUM_PICKAXE.get());
            event.accept(ModItems.VANADIUM_AXE.get());
            event.accept(ModItems.VANADIUM_SHOVEL.get());
            event.accept(ModItems.VANADIUM_HOE.get());
            event.accept(ModItems.RADAR.get());
            event.accept(ModItems.OSMIUM_PICKAXE.get());
            event.accept(ModItems.OSMIUM_AXE.get());
            event.accept(ModItems.OSMIUM_SHOVEL.get());
            event.accept(ModItems.OSMIUM_HOE.get());
            event.accept(ModItems.MOONDIAL.get());
            event.accept(ModItems.RECALIBRATED_RADAR.get());
            event.accept(ModItems.DAMASK_PICKAXE.get());
            event.accept(ModItems.DAMASK_AXE.get());
            event.accept(ModItems.DAMASK_SHOVEL.get());
            event.accept(ModItems.DAMASK_HOE.get());
            event.accept(ModItems.ORE_TRACKER.get());
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.REFINING_TABLE.get());
            event.accept(ModBlocks.OSMIUM_SPOTLIGHT.get());
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.VANADIUM_BLOCK.get());
            event.accept(ModBlocks.VANADIUM_BARS.get());
            event.accept(ModBlocks.RAW_VANADIUM_BLOCK.get());
            event.accept(ModBlocks.VANADIUM_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_VANADIUM_ORE.get());
            event.accept(ModBlocks.LABRADORITE_BLOCK.get());
            event.accept(ModBlocks.DEEPSLATE_LABRADORITE_ORE.get());
            event.accept(ModBlocks.LABRADORITE_ORE.get());
            event.accept(ModBlocks.LABRADORITE_BRICKS.get());
            event.accept(ModBlocks.LABRADORITE_BRICK_STAIRS.get());
            event.accept(ModBlocks.LABRADORITE_BRICK_SLAB.get());
            event.accept(ModBlocks.LABRADORITE_BRICK_WALL.get());
            event.accept(ModBlocks.LABRADORITE_PILLAR.get());
            event.accept(ModBlocks.LABRADORITE_LAMP.get());
            event.accept(ModBlocks.OSMIUM_BLOCK.get());
            event.accept(ModBlocks.OSMIUM_BARS.get());
            event.accept(ModBlocks.RAW_OSMIUM_BLOCK.get());
            event.accept(ModBlocks.OSMIUM_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_OSMIUM_ORE.get());
            event.accept(ModBlocks.SPOTTED_BLACKSTONE.get());
            event.accept(ModBlocks.CIMMERIAN_BLOCK.get());
            event.accept(ModBlocks.DAMASK_BLOCK.get());
            event.accept(ModBlocks.VANADIUM_BRICKS.get());
            event.accept(ModBlocks.VANADIUM_BRICK_STAIRS.get());
            event.accept(ModBlocks.VANADIUM_BRICK_SLAB.get());
            event.accept(ModBlocks.VANADIUM_BRICK_WALL.get());
            event.accept(ModBlocks.CHISELED_VANADIUM_BRICKS.get());
            event.accept(ModBlocks.OSMIUM_BRICKS.get());
            event.accept(ModBlocks.OSMIUM_BRICK_STAIRS.get());
            event.accept(ModBlocks.OSMIUM_BRICK_SLAB.get());
            event.accept(ModBlocks.OSMIUM_BRICK_WALL.get());
            event.accept(ModBlocks.CHISELED_OSMIUM_BRICKS.get());
        }

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.VANADIUM_GOLEM_SPAWN_EGG.get());
            event.accept(ModItems.ENFORCER_SPAWN_EGG.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.REFINING_TABLE.get(),
                (RefiningTableEntity entity, Direction direction) -> entity.getItemHandler()
        );
    }
}