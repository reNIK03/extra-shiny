package net.r_nik.extrashiny.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.r_nik.extrashiny.screen.RefiningTableMenu;

import java.util.function.Supplier;

public class RefiningButtonPacket {

    private final boolean overcap;

    public RefiningButtonPacket(boolean overcap) {
        this.overcap = overcap;
    }

    public RefiningButtonPacket(FriendlyByteBuf buf) {
        this.overcap = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(overcap);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (player.containerMenu instanceof RefiningTableMenu menu) {
                menu.refine(overcap);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
