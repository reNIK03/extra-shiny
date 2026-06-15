package net.r_nik.extrashiny.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.r_nik.extrashiny.ExtraShiny;


public class ModMessages {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ExtraShiny.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(
                id++,
                RefiningButtonPacket.class,
                RefiningButtonPacket::toBytes,
                RefiningButtonPacket::new,
                RefiningButtonPacket::handle
        );
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }
}
