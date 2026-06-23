package net.r_nik.extrashiny.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.screen.RefiningTableMenu;

public record RefiningButtonPacket(boolean overcap) implements CustomPacketPayload {

    public static final Type<RefiningButtonPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "refining_button"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RefiningButtonPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, RefiningButtonPacket::overcap,
                    RefiningButtonPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RefiningButtonPacket packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof RefiningTableMenu menu) {
                    menu.refine(packet.overcap());
                }
            }
        });
    }
}