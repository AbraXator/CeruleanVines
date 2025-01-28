package net.abraxator.moresnifferflowers.networking;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DyespriaModePacket(int amount) implements IMSFPacket {
    public static final Type<DyespriaModePacket> TYPE = new Type<>(MoreSnifferFlowers.loc("dyespria_mode"));
    public static final StreamCodec<ByteBuf, DyespriaModePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DyespriaModePacket::amount,
            DyespriaModePacket::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            var stack = player.getMainHandItem();
            if(stack.getItem() instanceof DyespriaItem dyespriaItem && player instanceof ServerPlayer serverPlayer) {
                dyespriaItem.changeMode(serverPlayer, stack, amount);
            }
        });
    }
}
