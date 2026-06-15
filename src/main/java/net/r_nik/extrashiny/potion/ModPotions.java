package net.r_nik.extrashiny.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.effect.ModEffects;

public class ModPotions {

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ExtraShiny.MOD_ID);

    public static final RegistryObject<Potion> DECEIVER =
            POTIONS.register("deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM.get(),
                            3600, // 3 min
                            0
                    )));

    public static final RegistryObject<Potion> STRONG_DECEIVER =
            POTIONS.register("strong_deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM.get(),
                            1800, // 1.5 min
                            1
                    )));

    public static final RegistryObject<Potion> LONG_DECEIVER =
            POTIONS.register("long_deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM.get(),
                            9600, // 8 min
                            0
                    )));


    public static final RegistryObject<Potion> WISDOM =
            POTIONS.register("wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM.get(),
                            2400, // 2 min
                            0
                    )));

    public static final RegistryObject<Potion> STRONG_WISDOM =
            POTIONS.register("strong_wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM.get(),
                            1200, // 1 min
                            1
                    )));

    public static final RegistryObject<Potion> LONG_WISDOM =
            POTIONS.register("long_wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM.get(),
                            4800, // 4 min
                            0
                    )));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
