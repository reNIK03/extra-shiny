package net.r_nik.extrashiny.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;

public class ModModelLayers {

    public static final ModelLayerLocation VANADIUM_GOLEM_MAIN =
            new ModelLayerLocation(
                    new ResourceLocation(ExtraShiny.MOD_ID, "vanadium_golem"),
                    "main"
            );

    public static final ModelLayerLocation ENFORCER_MAIN =
            new ModelLayerLocation(
                    new ResourceLocation(ExtraShiny.MOD_ID, "enforcer"),
                    "main"
            );
}
