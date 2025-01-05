package net.abraxator.moresnifferflowers.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.ICustomPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMSFPacket extends ICustomPacket {
    void handle(NetworkEvent.Context context);
    
    void encode(FriendlyByteBuf buf);
    
    static <P extends IMSFPacket> void handle(P message, Supplier<NetworkEvent.Context> ctx) {
        if (message != null) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> message.handle(context));
            context.setPacketHandled(true);
        }
    }
}
