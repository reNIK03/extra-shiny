package net.r_nik.extrashiny.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.r_nik.extrashiny.ExtraShiny;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ExtraShiny.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NORMAL_REFINE =
            registerSound("normalrefine");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOLDEN_REFINE =
            registerSound("goldenrefine");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_HURT =
            registerSound("vgolem_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_DEATH =
            registerSound("vgolem_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_M_ATK =
            registerSound("vgolem_medium_hit");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_H_ATK_START =
            registerSound("vgolem_heavy_start");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_ATK_END =
            registerSound("vgolem_attack_end");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_LIGHT_SPIN =
            registerSound("vgolem_light_spin");

    public static final DeferredHolder<SoundEvent, SoundEvent> VGOLEM_SPIN =
            registerSound("vgolem_medium_spin");

    public static final DeferredHolder<SoundEvent, SoundEvent> LABRADORITE_BLOCK =
            registerSound("labradorite_block");

    public static final SoundType LABRADORITE_SOUND_TYPE = new SoundType(
            1.0f, 1.0f,
            ModSounds.LABRADORITE_BLOCK.get(),  // break
            ModSounds.LABRADORITE_BLOCK.get(),  // step
            ModSounds.LABRADORITE_BLOCK.get(),  // place
            ModSounds.LABRADORITE_BLOCK.get(),  // hit
            ModSounds.LABRADORITE_BLOCK.get()   // fall
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> DAMASK_BLOCK =
            registerSound("damask_block");

    public static final SoundType DAMASK_SOUND_TYPE = new SoundType(
            1.0f, 1.0f,
            ModSounds.DAMASK_BLOCK.get(),
            ModSounds.DAMASK_BLOCK.get(),
            ModSounds.DAMASK_BLOCK.get(),
            ModSounds.DAMASK_BLOCK.get(),
            ModSounds.DAMASK_BLOCK.get()
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> OSMIUM_BLOCK =
            registerSound("osmium_block");

    public static final SoundType OSMIUM_SOUND_TYPE = new SoundType(
            0.2f, 1.0f,
            ModSounds.OSMIUM_BLOCK.get(),
            ModSounds.OSMIUM_BLOCK.get(),
            ModSounds.OSMIUM_BLOCK.get(),
            ModSounds.OSMIUM_BLOCK.get(),
            ModSounds.OSMIUM_BLOCK.get()
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> CIMMERIAN_BLOCK =
            registerSound("cimmerian_block");

    public static final SoundType CIMMERIAN_SOUND_TYPE = new SoundType(
            1.0f, 1.0f,
            ModSounds.CIMMERIAN_BLOCK.get(),
            ModSounds.CIMMERIAN_BLOCK.get(),
            ModSounds.CIMMERIAN_BLOCK.get(),
            ModSounds.CIMMERIAN_BLOCK.get(),
            ModSounds.CIMMERIAN_BLOCK.get()
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> VANADIUM_BLOCK =
            registerSound("vanadium_block");

    public static final SoundType VANADIUM_SOUND_TYPE = new SoundType(
            1.0f, 1.0f,
            ModSounds.VANADIUM_BLOCK.get(),
            ModSounds.VANADIUM_BLOCK.get(),
            ModSounds.VANADIUM_BLOCK.get(),
            ModSounds.VANADIUM_BLOCK.get(),
            ModSounds.VANADIUM_BLOCK.get()
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_DEATH =
            registerSound("enforcer_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_ENRAGED =
            registerSound("enforcer_enraged");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_HURT =
            registerSound("enforcer_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_HOWL =
            registerSound("enforcer_howl");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_IDLE =
            registerSound("enforcer_idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENFORCER_BITE =
            registerSound("enforcer_bite");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, name)));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}