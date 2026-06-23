package net.r_nik.extrashiny.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.r_nik.extrashiny.ExtraShiny;

@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class ModMessages {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                RefiningButtonPacket.TYPE,
                RefiningButtonPacket.STREAM_CODEC,
                RefiningButtonPacket::handle
        );
    }

    public static void sendToServer(RefiningButtonPacket packet) {
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(packet);
    }
}