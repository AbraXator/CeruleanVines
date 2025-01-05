package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.networking.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;

public class ClientEvents {
    @SubscribeEvent
    public static void onInputMouseScrolling(MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.DYESPRIA.get())) {
            event.setCanceled(true);
            ModPacketHandler.CHANNEL.sendToServer(new DyespriaModePacket((int) event.getScrollDelta()));
        }
    }
}
