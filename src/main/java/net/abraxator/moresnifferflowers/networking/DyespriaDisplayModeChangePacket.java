package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record DyespriaDisplayModeChangePacket(int dyespriaModeId) {
    public DyespriaDisplayModeChangePacket(FriendlyByteBuf buf) {
        this(buf.readByte());
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(dyespriaModeId());
    }
    
    public class Handler {
        public static void handle(DyespriaDisplayModeChangePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                context.get().getSender().displayClientMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(packet.dyespriaModeId)), true);
            });
            context.get().setPacketHandled(true);
        }
    }
}
