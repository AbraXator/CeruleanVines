package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            MoreSnifferFlowers.loc("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id++, CorruptedSludgePacket.class, CorruptedSludgePacket::encode, CorruptedSludgePacket::new, CorruptedSludgePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DyespriaDisplayModeChangePacket.class, DyespriaDisplayModeChangePacket::encode, DyespriaDisplayModeChangePacket::new, DyespriaDisplayModeChangePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DyespriaModePacket.class, DyespriaModePacket::encode, DyespriaModePacket::new, DyespriaModePacket::handle);
    }
}
