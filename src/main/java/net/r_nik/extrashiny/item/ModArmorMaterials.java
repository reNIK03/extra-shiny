package net.r_nik.extrashiny.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    VANADIUM("vanadium",35,new int[]{3,8,6,3},16, SoundEvents.ARMOR_EQUIP_NETHERITE,1,0,() -> Ingredient.of(ModItems.VANADIUM_INGOT.get())
    ),

    OSMIUM(
            "osmium",
            11,
            new int[]{2, 6, 3, 1},
            13,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.05F,
            () -> Ingredient.of(ModItems.OSMIUM_INGOT.get())
    ),


    DAMASK(
            "damask",
            37,
            new int[]{3, 8, 6, 3},
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            2F,
            0.0F,
            () -> Ingredient.of(ModItems.DAMASK_INGOT.get())
    ),

    CIMMERIAN(
            "cimmerian",
            23,
            new int[]{2, 6, 5, 2},
            5,
            SoundEvents.ARMOR_EQUIP_IRON,
            1F,
            0.0F,
            () -> Ingredient.of(ModItems.ANCIENT_LATTICE.get())
    );




    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = {11, 16, 16, 13};

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()]*this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return ExtraShiny.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
