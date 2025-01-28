package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.OptionalInt;

public class CorruptedLeavesBlock extends LeavesBlock {
    public CorruptedLeavesBlock(Properties p_54422_) {
        super(p_54422_);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.setBlock(pPos, updateDistance(pState, pLevel, pPos), 3);
    }
    
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        int i = getDistanceAt(pFacingState) + 1;
        if (i != 1 || pState.getValue(DISTANCE) != i) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
        }

        return pState;
    }

    private static BlockState updateDistance(BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(pPos, direction);
            i = Math.min(i, getDistanceAt(pLevel.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        return pState.setValue(DISTANCE, Integer.valueOf(i));
    }

    private static int getDistanceAt(BlockState pNeighbor) {
        return getOptionalDistanceAt(pNeighbor).orElse(7);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState pState) {
        if (pState.is(BlockTags.LOGS) || pState.is(ModBlocks.CORRUPTED_SLUDGE.get())) {
            return OptionalInt.of(0);
        } else {
            return pState.hasProperty(DISTANCE) ? OptionalInt.of(pState.getValue(DISTANCE)) : OptionalInt.empty();
        }
    }
}
