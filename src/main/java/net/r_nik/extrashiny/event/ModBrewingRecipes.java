package net.r_nik.extrashiny.event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.potion.ModPotions;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class ModBrewingRecipes {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Note: We use PotionContents.createItemStack instead of PotionUtils

            // Deceiver Recipes
            register(Potions.AWKWARD, ModItems.ANCIENT_LATTICE, ModPotions.DECEIVER);
            register(ModPotions.DECEIVER, Items.GLOWSTONE_DUST, ModPotions.STRONG_DECEIVER);
            register(ModPotions.DECEIVER, Items.REDSTONE, ModPotions.LONG_DECEIVER);

            // Wisdom Recipes
            register(Potions.AWKWARD, ModItems.LABRADORITE, ModPotions.WISDOM);
            register(ModPotions.WISDOM, Items.GLOWSTONE_DUST, ModPotions.STRONG_WISDOM);
            register(ModPotions.WISDOM, Items.REDSTONE, ModPotions.LONG_WISDOM);
        });
    }

    private static void register(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> input,
                                 net.minecraft.world.level.ItemLike ingredient,
                                 net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> output) {

        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionContents.createItemStack(Items.POTION, input)),
                Ingredient.of(ingredient),
                PotionContents.createItemStack(Items.POTION, output)
        );
    }
}