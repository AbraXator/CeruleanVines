package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface Corruptable {
    default Optional<Block> getCorruptedBlock(Block block, RandomSource random) { 
        return net.abraxator.moresnifferflowers.data.datamaps.Corruptable.getCorruptedBlock(block, random);
    }
    
    default void onCorrupt(ServerLevel level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var corruptedState = corruptedBlock.withPropertiesOf(oldState);
        level.setBlockAndUpdate(pos, corruptedState);
    }
}
