package net.abraxator.moresnifferflowers.blocks;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Map;

public interface ColorableVivicusBlock extends Colorable {
    default EnumProperty<DyeColor> getColorProperty() {
        return ModStateProperties.COLOR;
    }

    default void addDye(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if(!blockState.hasProperty(getColorProperty())) {
            return;
        }
        
        var stack = player.getMainHandItem();
        Dye dye = Dye.getDyeFromStack(stack);
        RandomSource randomSource = level.random;

        if(blockState.getValue(getColorProperty()).equals(dye.color()) || dye.isEmpty()) {
            return;
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(getColorProperty(), dye.color()));
        ItemStack itemStack = Dye.stackFromDye(new Dye(dye.color(), dye.amount() - randomSource.nextIntBetweenInclusive(0, 1)));
        Dye.setDyeToStack(stack, itemStack, itemStack.getCount());
        
        if(!level.isClientSide()) {
            particles(randomSource, ((ServerLevel) level), dye, blockPos);
        }
    }

    @Override
    default Map<DyeColor, Integer> colorValues() {
        return Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
            dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFFFFFFF);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFFd2cad8);
            dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFFb0a4be);
            dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF837e9b);
            dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFFcba886);
            dyeColorHexFormatMap.put(DyeColor.RED, 0xFFf3c1c1);
            dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFf8d5ba);
            dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFf8f6bc);
            dyeColorHexFormatMap.put(DyeColor.LIME, 0xFFdcf9b7);
            dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFFafecb7);
            dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF9df8e3);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFFa1eaf1);
            dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFFa3cae6);
            dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFFccb6f6);
            dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFe2bcf7);
            dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFfbd8fa);
        });
    }
}
