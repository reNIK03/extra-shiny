package net.r_nik.extrashiny.compat;

import net.minecraftforge.fml.ModList;

public class ModCompat {
    public static final boolean DYE_DEPOT_LOADED =
            ModList.get().isLoaded("dye_depot");
}
