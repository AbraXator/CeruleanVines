package net.abraxator.moresnifferflowers.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IModEntityDoubleTallBlock {
    Block getLowerBlock();

    Block getCorruptedLowerBlock();

    Block getUpperBlock();

    Block getCorruptedUpperBlock();

    default boolean isLower(BlockState blockState) {
        return !isUpper(blockState);
    }

    default boolean isUpper(BlockState blockState) {
        return blockState.is(getUpperBlock());
    }
    
    default boolean isStateThis(BlockState blockState) {
        if(getCorruptedLowerBlock() != null && getCorruptedUpperBlock() != null)
            return blockState.is(getLowerBlock()) || blockState.is(getUpperBlock()) || blockState.is(getCorruptedLowerBlock()) || blockState.is(getCorruptedLowerBlock());
        return blockState.is(getLowerBlock()) || blockState.is(getUpperBlock());
    }
    
    default boolean areTwoHalfSame(BlockState block1, BlockState block2) {
        return (isUpper(block1) && isUpper(block2)) || (isLower(block1) && isLower(block2));
    }
} 
