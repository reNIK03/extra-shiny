package net.r_nik.extrashiny.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, ExtraShiny.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> WISDOM =
            MOB_EFFECTS.register("wisdom", WisdomEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> PHANTASM =
            MOB_EFFECTS.register("phantasm", PhantasmEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}