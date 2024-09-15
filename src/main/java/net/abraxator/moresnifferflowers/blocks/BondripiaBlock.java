package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BondripiaBlock extends SporeBlossomBlock implements ModEntityBlock {
    public BondripiaBlock(Properties p_49795_) {
        super(p_49795_);
        this.defaultBlockState().setValue(ModStateProperties.CENTER, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ModStateProperties.CENTER);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        Direction.Plane.HORIZONTAL.forEach(direction -> {
            BlockPos blockPos = pPos.relative(direction);
            
            pLevel.setBlock(blockPos, this.defaultBlockState().setValue(ModStateProperties.CENTER, pPos.equals(blockPos)), 3);
            
            if(pLevel.getBlockEntity(blockPos) instanceof BondripiaBlockEntity entity) {
                entity.center = pPos;
            }
        });
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof BondripiaBlockEntity entity) {
            for (BlockPos blockPos : BlockPos.betweenClosed(entity.center.north().east(), entity.center.south().west())) {
                BlockPos currentPos = blockPos;

                for (int i = 0; i < 10; i++) {
                    if (isBondripable(pLevel, currentPos)) {
                        BlockState blockState = pLevel.getBlockState(currentPos);


                        if (blockState.getBlock() instanceof BonemealableBlock bonemealable) {
                            if (bonemealable.isValidBonemealTarget(pLevel, currentPos, blockState)) {
                                bonemealable.performBonemeal(pLevel, pRandom, currentPos, blockState);
                                break;
                            }
                        }
                        if (blockState.is(ModTags.ModBlockTags.BONMEELABLE)) {
                            Bonmeelable bonmeelable = ((Bonmeelable) Bonmeelable.MAP.get(blockState.getBlock()));
                            if (bonmeelable.canBonmeel(blockPos, blockState, pLevel)) {
                                bonmeelable.performBonmeel(currentPos, blockState, pLevel, null);
                                break;
                            }
                        }

                    }
                    
                    currentPos = currentPos.below();
                }
            }
        }
    }
    
    private boolean isBondripable(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).getBlock() instanceof BonemealableBlock || level.getBlockState(blockPos).is(ModTags.ModBlockTags.BONMEELABLE);
    }

    @Override
    public void animateTick(BlockState p_222503_, Level p_222504_, BlockPos p_222505_, RandomSource p_222506_) {}

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        var list = Direction.Plane.HORIZONTAL.stream().filter(direction -> super.canSurvive(level.getBlockState(blockPos.relative(direction)), level, blockPos.relative(direction))).toList();
        return super.canSurvive(blockState, level, blockPos) && list.size() == 4;
    }

    @Override
    protected BlockState updateShape(BlockState p_154713_, Direction p_154714_, BlockState p_154715_, LevelAccessor p_154716_, BlockPos pCurrentPos, BlockPos p_154718_) {
        BlockState blockState = super.updateShape(p_154713_, p_154714_, p_154715_, p_154716_, pCurrentPos, p_154718_);
        if(blockState.is(Blocks.AIR)) {
            Direction.Plane.HORIZONTAL.forEach(direction -> {
                BlockPos blockPos = pCurrentPos.relative(direction);

                p_154716_.destroyBlock(blockPos, true);
            });
        }
        
        return super.updateShape(p_154713_, p_154714_, p_154715_, p_154716_, pCurrentPos, p_154718_);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BondripiaBlockEntity(pPos, pState);
    }
}
