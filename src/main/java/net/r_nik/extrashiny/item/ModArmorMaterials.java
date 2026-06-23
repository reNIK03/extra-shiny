package net.r_nik.extrashiny.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.r_nik.extrashiny.ExtraShiny;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ExtraShiny.MOD_ID);

    public static final Holder<ArmorMaterial> VANADIUM = registerArmorMaterial(
            "vanadium",
            defenseMap(3, 6, 8, 3),
            16,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(ModItems.VANADIUM_INGOT.get()),
            1f,
            0f
    );

    public static final Holder<ArmorMaterial> OSMIUM = registerArmorMaterial(
            "osmium",
            defenseMap(1, 3, 6, 2),
            13,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(ModItems.OSMIUM_INGOT.get()),
            0.0f,
            0.05f
    );

    public static final Holder<ArmorMaterial> DAMASK = registerArmorMaterial(
            "damask",
            defenseMap(3, 6, 8, 3),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(ModItems.DAMASK_INGOT.get()),
            2f,
            0.0f
    );

    public static final Holder<ArmorMaterial> CIMMERIAN = registerArmorMaterial(
            "cimmerian",
            defenseMap(2, 5, 6, 2),
            5,
            SoundEvents.ARMOR_EQUIP_IRON,
            () -> Ingredient.of(ModItems.ANCIENT_LATTICE.get()),
            1f,
            0.0f
    );

    // Helper: defense values in original order (boots, leggings, chestplate, helmet)
    // matching your old int[]{boots, leggings, chestplate, helmet} arrays.
    private static EnumMap<ArmorItem.Type, Integer> defenseMap(int boots, int leggings, int chestplate, int helmet) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.BODY, chestplate); // for horse armor reuse; matches chestplate-tier protection
        return map;
    }

    private static Holder<ArmorMaterial> registerArmorMaterial(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            float toughness,
            float knockbackResistance
    ) {
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, name))
        );
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                defense,
                enchantmentValue,
                equipSound,
                repairIngredient,
                layers,
                toughness,
                knockbackResistance
        ));
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}