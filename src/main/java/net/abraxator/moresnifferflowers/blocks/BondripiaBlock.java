package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BondripiaBlock extends SporeBlossomBlock implements ModEntityBlock, ModCropBlock {
    public BondripiaBlock(Properties p_49795_) {
        super(p_49795_);
        this.defaultBlockState()
                .setValue(ModStateProperties.CENTER, false)
                .setValue(getAgeProperty(), 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ModStateProperties.CENTER, getAgeProperty());
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
    protected boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(state.getValue(ModStateProperties.CENTER) && random.nextDouble() <= 0.3D && isMaxAge(state)) {
            List<BlockPos> list = new java.util.ArrayList<>(BlockPos.betweenClosedStream(pos.north().east(), pos.south().west()).map(BlockPos::immutable).toList());
            Collections.shuffle(list);
            list = list.subList(0, random.nextInt(6));
            list.forEach(blockPos -> {
                var vec3 = blockPos.getCenter();
                level.addParticle(ModParticles.BONDRIPIA.get(), vec3.x, vec3.y, vec3.z, 0, 0, 0);
            });
        }
    }
    
 
    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!isMaxAge(pState)) {
            grow(pLevel, pPos);
        } else if (pRandom.nextDouble() <= 0.33D && pLevel.getBlockEntity(pPos) instanceof BondripiaBlockEntity entity) {
            for (BlockPos blockPos : BlockPos.betweenClosed(entity.center.north().east(), entity.center.south().west())) {
                BlockPos currentPos = blockPos;

                for (int i = 0; i < 10; i++) {
                    if (isBondripable(pLevel, currentPos)) {
                        BlockState blockState = pLevel.getBlockState(currentPos);


                        if (blockState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(pLevel, currentPos, blockState)) {
                            bonemealable.performBonemeal(pLevel, pRandom, currentPos, blockState);
                            break;
                        }
                        
                        if (blockState.is(ModTags.ModBlockTags.BONMEELABLE)) {
                            Bonmeelable bonmeelable = ((Bonmeelable) Bonmeelable.MAP.get(blockState.getBlock()));
                            if (bonmeelable.canBonmeel(currentPos, blockState, pLevel)) {
                                bonmeelable.performBonmeel(currentPos, blockState, pLevel, null);
                                break;
                            }
                        }

                    } else if (pLevel.getBlockState(currentPos).getBlock() instanceof AbstractCauldronBlock block) {
                        
                    }

                    currentPos = currentPos.below();
                }
            }
        }
    }
    
    private void fillCauldron(Level level, BlockPos blockPos, BlockState blockState) {
        BlockState blockstate = ModBlocks.BONMEEL_FILLED_CAULDRON.defaultBlockState();
        int fluidLevel = blockState.getOptionalValue(LayeredCauldronBlock.LEVEL).orElse(0);
        if(fluidLevel < 3) {
            level.setBlockAndUpdate(blockPos, blockstate.setValue(LayeredCauldronBlock.LEVEL, fluidLevel + 1));
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockstate));
            level.levelEvent(1047, blockPos, 0);
        }
    }
    
    private void grow(Level level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof BondripiaBlockEntity entity) {
            makeGrowOnBonemeal(level, entity.center, level.getBlockState(entity.center));
            Direction.Plane.HORIZONTAL.forEach(direction -> {
                makeGrowOnBonemeal(level, entity.center.relative(direction), level.getBlockState(entity.center.relative(direction)));
            });
        }
    }
    
    private boolean isBondripable(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).getBlock() instanceof BonemealableBlock || level.getBlockState(blockPos).is(ModTags.ModBlockTags.BONMEELABLE);
    }

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

    @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_2;
    }
    
    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return !isMaxAge(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        grow(pLevel, pPos);
    }
}
