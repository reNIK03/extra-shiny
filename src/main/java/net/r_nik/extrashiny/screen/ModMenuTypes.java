package net.r_nik.extrashiny.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.r_nik.extrashiny.ExtraShiny;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExtraShiny.MOD_ID);

    public static final RegistryObject<MenuType<RefiningTableMenu>> REFINING_TABLE_MENU =
            MENUS.register("refining_table_menu",
                    () -> IForgeMenuType.create(RefiningTableMenu::new));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}

