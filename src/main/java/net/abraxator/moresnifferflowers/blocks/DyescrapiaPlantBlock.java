package net.abraxator.moresnifferflowers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class DyescrapiaPlantBlock extends Block {
    public DyescrapiaPlantBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState belowBlockState = pLevel.getBlockState(blockpos);
        net.neoforged.neoforge.common.util.TriState soilDecision = belowBlockState.canSustainPlant(pLevel, blockpos, Direction.UP, pState);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return this.mayPlaceOn(belowBlockState);
    }
    
    public boolean mayPlaceOn(BlockState pState) {
        return pState.is(BlockTags.DIRT) && !(pState.getBlock() instanceof FarmBlock);
    }
}
