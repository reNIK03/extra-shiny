package net.r_nik.extrashiny.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ExtraShiny.MOD_ID);

    public static final RegistryObject<SoundEvent> NORMAL_REFINE =
            registerSound("normalrefine");

    public static final RegistryObject<SoundEvent> GOLDEN_REFINE =
            registerSound("goldenrefine");

    public static final RegistryObject<SoundEvent> VGOLEM_HURT =
            registerSound("vgolem_hurt");

    public static final RegistryObject<SoundEvent> VGOLEM_DEATH =
            registerSound("vgolem_death");

    public static final RegistryObject<SoundEvent> VGOLEM_M_ATK =
            registerSound("vgolem_medium_hit");

    public static final RegistryObject<SoundEvent> VGOLEM_H_ATK_START =
            registerSound("vgolem_heavy_start");

    public static final RegistryObject<SoundEvent> VGOLEM_ATK_END =
            registerSound("vgolem_attack_end");

    public static final RegistryObject<SoundEvent> VGOLEM_LIGHT_SPIN =
            registerSound("vgolem_light_spin");

    public static final RegistryObject<SoundEvent> VGOLEM_SPIN =
            registerSound("vgolem_medium_spin");

    public static final RegistryObject<SoundEvent> LABRADORITE_BLOCK =
            registerSound("labradorite_block");

    public static final ForgeSoundType LABRADORITE_SOUND_TYPE = new ForgeSoundType(
            1.0f, 1.0f,
            ModSounds.LABRADORITE_BLOCK,  // break
            ModSounds.LABRADORITE_BLOCK,  // step
            ModSounds.LABRADORITE_BLOCK,  // place
            ModSounds.LABRADORITE_BLOCK,  // hit
            ModSounds.LABRADORITE_BLOCK   // fall
    );

    public static final RegistryObject<SoundEvent> DAMASK_BLOCK =
            registerSound("damask_block");

    public static final ForgeSoundType DAMASK_SOUND_TYPE = new ForgeSoundType(
            1.0f, 1.0f,
            ModSounds.DAMASK_BLOCK,  // break
            ModSounds.DAMASK_BLOCK,  // step
            ModSounds.DAMASK_BLOCK,  // place
            ModSounds.DAMASK_BLOCK,  // hit
            ModSounds.DAMASK_BLOCK   // fall
    );

    public static final RegistryObject<SoundEvent> OSMIUM_BLOCK =
            registerSound("osmium_block");

    public static final ForgeSoundType OSMIUM_SOUND_TYPE = new ForgeSoundType(
            0.2f, 1.0f,
            ModSounds.OSMIUM_BLOCK,  // break
            ModSounds.OSMIUM_BLOCK,  // step
            ModSounds.OSMIUM_BLOCK,  // place
            ModSounds.OSMIUM_BLOCK,  // hit
            ModSounds.OSMIUM_BLOCK   // fall
    );

    public static final RegistryObject<SoundEvent> CIMMERIAN_BLOCK =
            registerSound("cimmerian_block");

    public static final ForgeSoundType CIMMERIAN_SOUND_TYPE = new ForgeSoundType(
            1.0f, 1.0f,
            ModSounds.CIMMERIAN_BLOCK,  // break
            ModSounds.CIMMERIAN_BLOCK,  // step
            ModSounds.CIMMERIAN_BLOCK,  // place
            ModSounds.CIMMERIAN_BLOCK,  // hit
            ModSounds.CIMMERIAN_BLOCK   // fall
    );


    public static final RegistryObject<SoundEvent> VANADIUM_BLOCK =
            registerSound("vanadium_block");

    public static final ForgeSoundType VANADIUM_SOUND_TYPE = new ForgeSoundType(
            1.0f, 1.0f,
            ModSounds.VANADIUM_BLOCK,  // break
            ModSounds.VANADIUM_BLOCK,  // step
            ModSounds.VANADIUM_BLOCK,  // place
            ModSounds.VANADIUM_BLOCK,  // hit
            ModSounds.VANADIUM_BLOCK   // fall
    );

    public static final RegistryObject<SoundEvent> ENFORCER_DEATH =
            registerSound("enforcer_death");

    public static final RegistryObject<SoundEvent> ENFORCER_ENRAGED =
            registerSound("enforcer_enraged");

    public static final RegistryObject<SoundEvent> ENFORCER_HURT =
            registerSound("enforcer_hurt");

    public static final RegistryObject<SoundEvent> ENFORCER_HOWL =
            registerSound("enforcer_howl");

    public static final RegistryObject<SoundEvent> ENFORCER_IDLE =
            registerSound("enforcer_idle");

    public static final RegistryObject<SoundEvent> ENFORCER_BITE =
            registerSound("enforcer_bite");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(
                        new ResourceLocation(ExtraShiny.MOD_ID, name)));
    }
    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
