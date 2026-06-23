package net.r_nik.extrashiny.potion;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.effect.ModEffects;

public class ModPotions {

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, ExtraShiny.MOD_ID);

    public static final DeferredHolder<Potion, Potion> DECEIVER =
            POTIONS.register("deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM,
                            3600,
                            0
                    )));

    public static final DeferredHolder<Potion, Potion> STRONG_DECEIVER =
            POTIONS.register("strong_deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM,
                            1800,
                            1
                    )));

    public static final DeferredHolder<Potion, Potion> LONG_DECEIVER =
            POTIONS.register("long_deceiver",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.PHANTASM,
                            9600,
                            0
                    )));

    public static final DeferredHolder<Potion, Potion> WISDOM =
            POTIONS.register("wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM,
                            2400,
                            0
                    )));

    public static final DeferredHolder<Potion, Potion> STRONG_WISDOM =
            POTIONS.register("strong_wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM,
                            1200,
                            1
                    )));

    public static final DeferredHolder<Potion, Potion> LONG_WISDOM =
            POTIONS.register("long_wisdom",
                    () -> new Potion(new MobEffectInstance(
                            ModEffects.WISDOM,
                            4800,
                            0
                    )));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}