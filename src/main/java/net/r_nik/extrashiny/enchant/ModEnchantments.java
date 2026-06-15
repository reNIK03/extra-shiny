package net.r_nik.extrashiny.enchant;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExtraShiny.MOD_ID);


    public static final RegistryObject<Enchantment> GLACIATION = ENCHANTMENTS.register(
            "glaciation",
            GlaciationEnchantment::new
    );

    public static final RegistryObject<Enchantment> SERRATION = ENCHANTMENTS.register(
            "serration",
            SerrationEnchantment::new
    );

    public static final RegistryObject<Enchantment> FROST_EDGE = ENCHANTMENTS.register(
            "frost_edge",
            () -> new FrostEdgeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND)
    );
}