package net.abraxator.moresnifferflowers.networking;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

public record CorruptedSludgePacket(Vector3f start, Vector3f target, Vector3f direction) implements IMSFPacket {
    public static final CustomPacketPayload.Type<CorruptedSludgePacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("send_sludge_particle"));
    public static final StreamCodec<ByteBuf, CorruptedSludgePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, CorruptedSludgePacket::start,
            ByteBufCodecs.VECTOR3F, CorruptedSludgePacket::target,
            ByteBufCodecs.VECTOR3F, CorruptedSludgePacket::direction,
            CorruptedSludgePacket::new
    );
    
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isClientSide()) {
                float distance = start.distance(target);
                
                for (int i = 0; i < 15; i++) {
                    double progress = (double) i / 15;
                    Vector3f pos = new Vector3f(start).add(new Vector3f(direction).mul((float) (distance * progress)));
                    level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
