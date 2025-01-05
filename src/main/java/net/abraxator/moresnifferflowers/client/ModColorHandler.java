package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.util.Mth;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

public class ModColorHandler {
    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if(tintIndex == 0 && state.getValue(ModStateProperties.FULLNESS) > 0) {
                return state.getValue(ModStateProperties.CROP).tint;
            }        
            
            return -1;
        }, ModBlocks.CROPRESSOR_CENTER.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
            Colorable colorable = ((Colorable) pState.getBlock());
            Dye dye = colorable.getDyeFromBlock(pState);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                if (pTintIndex == 0) {
                    int startRed = (color >> 16) & 0xFF;
                    int startGreen = (color >> 8) & 0xFF;
                    int startBlue = color & 0xFF;
                    float[] colorHSB = Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                    return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
                }
                if (pTintIndex == 1) {
                    return color;
                }
            }
            return -1;
        }, ModBlocks.CAULORFLOWER.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
                    var colorable = ((ColorableVivicusBlock) pState.getBlock());
                    if(pTintIndex == 0) {
                        var dyedValue = Dye.colorForDye(colorable, pState.getValue(colorable.getColorProperty()));
                        var color = colorable.getDyeFromBlock(pState).color();

                        if(pState.is(ModBlocks.VIVICUS_LEAVES.get()) || pState.is(ModBlocks.VIVICUS_LEAVES_SPROUT.get()) || pState.is(ModBlocks.VIVICUS_SAPLING.get())) {
                            int startRed = (dyedValue >> 16) & 0xFF;
                            int startGreen = (dyedValue >> 8) & 0xFF;
                            int startBlue = dyedValue & 0xFF;
                            float[] colorHSB =  Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                            assert pPos != null;
                            float hue = colorHSB[0] + ((1+ Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 15);

                            if (colorHSB[1] < 0.3 && colorHSB[2] < 0.8){
                                colorHSB[2] = colorHSB[2] - ((1+Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 15);
                            }

                            if (colorHSB[1] < 0.3){
                                colorHSB[1] = colorHSB[1] + ((1+Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 12);
                            }


                            return Color.HSBtoRGB(hue, colorHSB[1], colorHSB[2]);
                        }

                        return dyedValue;
                    }

                    return -1;
                }, ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(),
                ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.VIVICUS_PLANKS.get(), ModBlocks.VIVICUS_STAIRS.get(),
                ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(),
                ModBlocks.VIVICUS_DOOR.get(), ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get(),
                ModBlocks.VIVICUS_BUTTON.get(), ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.VIVICUS_SAPLING.get(),
                ModBlocks.VIVICUS_LEAVES_SPROUT.get(), ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_HANGING_SIGN.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> {
            Dye dye = Dye.getDyeFromDyespria(pStack);
            if(pTintIndex != 0 || dye.isEmpty()) {
                return -1;
            } else {
                return Dye.colorForDye(((DyespriaItem) pStack.getItem()), dye.color());
            }
        }, ModItems.DYESPRIA.get());
        event.register((pStack, pTintIndex) -> {
            return pTintIndex > 0 ? -1 : PotionUtils.getColor(pStack);
        }, ModItems.EXTRACTED_BOTTLE.get(), ModItems.REBREWED_POTION.get(), ModItems.REBREWED_SPLASH_POTION.get(), ModItems.REBREWED_LINGERING_POTION.get());
    }

    public static float[] hexToRGB(int hex) {
        return new float[] {(hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF};
    }
}
