package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.CropressorAnimation;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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
            Component component = pStack.get(DataComponents.CUSTOM_NAME);
            if(component != null && component.getString().equals("og")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.CROPRESSOR.get(), MoreSnifferFlowers.loc("animate"), (pStack, pLevel, pEntity, pSeed) -> {
            var animation = getAnimation(pStack);
            System.out.println(animation.progress());
            if (animation.playing()) {
                pStack.set(ModDataComponents.CROPRESSOR_ANIMATE, new CropressorAnimation(true, animation.progress() + (double) 1 / COPRESSOR_ANIMATION_FRAMES));
                
                if(animation.progress() >= 0.99D) {
                    pStack.set(ModDataComponents.CROPRESSOR_ANIMATE, new CropressorAnimation(false, animation.progress()));
                }
                
                return 1.0F;
            }
            
            return 0.0F;
        });
    }
    
    private static CropressorAnimation getAnimation(ItemStack itemStack) {
        return itemStack.getOrDefault(ModDataComponents.CROPRESSOR_ANIMATE, new CropressorAnimation(false, 0));
    }
}
