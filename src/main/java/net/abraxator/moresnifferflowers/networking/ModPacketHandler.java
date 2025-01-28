package net.abraxator.moresnifferflowers.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.net.http.HttpResponse;

public class ModPacketHandler {
    public ModPacketHandler(IEventBus modEventBus, int version) {
        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(String.valueOf(version));
            registerClientToServer(new ModPacketRegistrar(registrar, true));
            registerServerToClient(new ModPacketRegistrar(registrar, false));
        });
    }
    
    protected void registerClientToServer(ModPacketRegistrar registrar) {
        registrar.play(DyespriaModePacket.TYPE, DyespriaModePacket.STREAM_CODEC);
    }
    
    protected void registerServerToClient(ModPacketRegistrar registrar) {
        registrar.play(DyespriaDisplayModeChangePacket.TYPE, DyespriaDisplayModeChangePacket.STREAM_CODEC);
        registrar.play(CorruptedSludgePacket.TYPE, CorruptedSludgePacket.STREAM_CODEC);
    }

    public static ModPacketHandler register(IEventBus iEventBus, int version) {
        return new ModPacketHandler(iEventBus, version);
    } 
    
    protected record ModPacketRegistrar(PayloadRegistrar registrar, boolean toServer) {
        public <MSG extends IMSFPacket> void play(CustomPacketPayload.Type<MSG> type, StreamCodec<? super RegistryFriendlyByteBuf, MSG> reader) {
            if (toServer) {
                registrar.playToServer(type, reader, IMSFPacket::handle);
            } else {
                registrar.playToClient(type, reader, IMSFPacket::handle);
            }
        }
    }
}
