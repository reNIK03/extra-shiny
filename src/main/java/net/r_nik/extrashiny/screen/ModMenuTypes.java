package net.r_nik.extrashiny.screen;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.r_nik.extrashiny.ExtraShiny;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, ExtraShiny.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<RefiningTableMenu>> REFINING_TABLE_MENU =
            MENUS.register("refining_table_menu",
                    () -> IMenuTypeExtension.create(RefiningTableMenu::new));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}