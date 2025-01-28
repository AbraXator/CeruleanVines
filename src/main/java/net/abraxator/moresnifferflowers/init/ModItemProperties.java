package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;

public class ModItemProperties {
    public static final int FRAME_TIME = 20;
    public static final int FRAME_AMOUNT = 3;
    public static final int COPRESSOR_ANIMATION_FRAMES = FRAME_TIME * FRAME_AMOUNT;
    
    public static void register() {
        ItemProperties.register(ModItems.DYESPRIA.get(), MoreSnifferFlowers.loc("color"), (pStack, pLevel, pEntity, pSeed) -> {
            if(!Dye.getDyeFromDyespria(pStack).isEmpty()) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.DRAGONFLY.get(), MoreSnifferFlowers.loc("og"), (pStack, pLevel, pEntity, pSeed) -> {
            Component component = pStack.getDisplayName();
            if(component != null && component.getString().equals("og")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }
}
