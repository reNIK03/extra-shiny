package net.r_nik.extrashiny.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ExtraShiny.MOD_ID);

    public static final RegistryObject<Attribute> DAMAGE_NEGATION =
            ATTRIBUTES.register("damage_negation",
                    () -> new RangedAttribute("attribute." + ExtraShiny.MOD_ID + ".damage_negation",
                            0.0D,
                            0.0D,
                            1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> SHOCK_ABSORPTION =
            ATTRIBUTES.register("shock_absorption",
                    () -> new RangedAttribute(
                            "attribute." + ExtraShiny.MOD_ID + ".shock_absorption",
                            0.0D,
                            0.0D,
                            0.9D
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> DAMAGE_REBOUND =
            ATTRIBUTES.register("damage_rebound",
                    () -> new RangedAttribute(
                            "attribute." + ExtraShiny.MOD_ID + ".damage_rebound",
                            0.0D,
                            0.0D,
                            1.0D
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> COUNTER_THORNS =
            ATTRIBUTES.register("counter_thorns",
                    () -> new RangedAttribute(
                            "attribute." + ExtraShiny.MOD_ID + ".counter_thorns",
                            0.0D,
                            0.0D,
                            1.0D
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> CRIT_DAMAGE_BONUS =
            ATTRIBUTES.register("crit_damage_bonus",
                    () -> new RangedAttribute(
                            "attribute." + ExtraShiny.MOD_ID + ".crit_damage_bonus",
                            0.0D,
                            0.0D,
                            10.0D
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> ARMOR_PIERCING =
            ATTRIBUTES.register("armor_piercing",
                    () -> new RangedAttribute(
                            "attribute." + ExtraShiny.MOD_ID + ".armor_piercing",
                            0.0D,
                            0.0D,
                            1.0D
                    ).setSyncable(true));
}