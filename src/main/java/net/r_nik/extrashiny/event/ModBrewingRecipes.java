package net.r_nik.extrashiny.event;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraft.world.item.crafting.Ingredient;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.potion.ModPotions;

@Mod.EventBusSubscriber(modid = ExtraShiny.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBrewingRecipes {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(ModItems.ANCIENT_LATTICE.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DECEIVER.get())
            ));

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DECEIVER.get())),
                    Ingredient.of(Items.GLOWSTONE_DUST),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.STRONG_DECEIVER.get())
            ));

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.DECEIVER.get())),
                    Ingredient.of(Items.REDSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_DECEIVER.get())
            ));

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(ModItems.LABRADORITE.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.WISDOM.get())
            ));

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.WISDOM.get())),
                    Ingredient.of(Items.GLOWSTONE_DUST),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.STRONG_WISDOM.get())
            ));

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.WISDOM.get())),
                    Ingredient.of(Items.REDSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.LONG_WISDOM.get())
            ));
        });
    }
}
