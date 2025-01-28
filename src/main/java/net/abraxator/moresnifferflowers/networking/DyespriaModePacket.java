package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record DyespriaModePacket(int amount) {
    public DyespriaModePacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(amount);
    }

    public static void handle(DyespriaModePacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            var stack = player.getMainHandItem();
            if(stack.getItem() instanceof DyespriaItem dyespriaItem) {
                dyespriaItem.changeMode(player, stack, packet.amount);
            }
        });
    }
}
