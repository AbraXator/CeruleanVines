package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BondripiaBlock extends SporeBlossomBlock implements ModEntityBlock, ModCropBlock, Corruptable {
    public BondripiaBlock(Properties p_49795_) {
        super(p_49795_);
        this.defaultBlockState()
                .setValue(ModStateProperties.CENTER, false)
                .setValue(getAgeProperty(), 0);
    }
    private static final VoxelShape SHAPE = Block.box(2.0, 13.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape SHAPE_CENTER = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ModStateProperties.CENTER, getAgeProperty());
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        getPositionsForPlant(pLevel, pPos).ifPresent(list -> {
            list.forEach(blockPos -> {
                pLevel.setBlock(blockPos, this.defaultBlockState().setValue(ModStateProperties.CENTER, pPos.equals(blockPos)), 3);
                if(pLevel.getBlockEntity(blockPos) instanceof BondripiaBlockEntity entity) {
                    entity.center = pPos;
                }
            });
        });
    }

    private Optional<List<BlockPos>> getPositionsForPlant(BlockGetter pLevel, BlockPos pPos) {
        List<BlockPos> positions = new ArrayList<>();
        positions.add(pPos);
        boolean b = Direction.Plane.HORIZONTAL.stream()
                .allMatch(direction -> pLevel.getBlockState(pPos.relative(direction)).canBeReplaced());
        if(b) {
            positions.addAll(Direction.Plane.HORIZONTAL.stream().map(direction -> pPos.relative(direction).immutable()).toList());
        }
        
        return positions.size() == 5 ? Optional.of(positions) : Optional.empty();
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState pState) {
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
                level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0xAA51B2).toVector3f(), 1.0F), vec3.x, vec3.y - random.nextIntBetweenInclusive(0, 5), vec3.z, 0, -1.0F, 0);
            });
        }
    }
    
 
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!isMaxAge(pState)) {
            grow(pLevel, pPos);
        } else if (pRandom.nextDouble() <= 0.33D && pLevel.getBlockEntity(pPos) instanceof BondripiaBlockEntity entity) {
            for (BlockPos blockPos : BlockPos.betweenClosed(entity.center.below().north().east(), entity.center.below().south().west())) {
                BlockPos currentPos = blockPos;

                for (int i = 0; i < 10; i++) {
                    if (isBondripable(pLevel, currentPos)) {
                        BlockState blockState = pLevel.getBlockState(currentPos);
                        
                        if (blockState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(pLevel, currentPos, blockState, false)) {
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
                        fillCauldron(pLevel, currentPos, pLevel.getBlockState(currentPos));
                    }

                    currentPos = currentPos.below();
                }
            }
        }
    }
    
    public void fillCauldron(Level level, BlockPos blockPos, BlockState blockState) {
        BlockState blockstate = blockState.is(ModBlocks.ACIDRIPIA.get()) ? ModBlocks.ACID_FILLED_CAULDRON.get().defaultBlockState() : ModBlocks.BONMEEL_FILLED_CAULDRON.get().defaultBlockState();
        int fluidLevel = level.getBlockState(blockPos).getOptionalValue(LayeredCauldronBlock.LEVEL).orElse(0);
        if(fluidLevel < 3) {
            level.setBlockAndUpdate(blockPos, blockstate.setValue(LayeredCauldronBlock.LEVEL, fluidLevel + 1));
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockstate));
            level.levelEvent(1047, blockPos, 0);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityinside) {
            if(entityinside instanceof CorruptedProjectile && CorruptionRecipe.canBeCorrupted(state.getBlock(), level)) {
                if(level.getBlockEntity(pos) instanceof BondripiaBlockEntity entity) {
                    BlockPos centrePos = entity.center;
                    BlockState centreState = level.getBlockState(centrePos);
                    Direction.Plane.HORIZONTAL.forEach(direction -> {
                        BlockPos blockPos = centrePos.relative(direction);
                        AciddripiaBlock.afterCorruption(centrePos, level, blockPos);

                    });
                    AciddripiaBlock.afterCorruption(centrePos, level, centrePos);

                }
        }
    }

    public void grow(Level level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof BondripiaBlockEntity entity) {
            if(level.getBlockState(entity.center).is(this))
                makeGrowOnBonemeal(level, entity.center, level.getBlockState(entity.center));
            else level.destroyBlock(blockPos, false);

            Direction.Plane.HORIZONTAL.forEach(direction -> {
                if(level.getBlockState(entity.center.relative(direction)).is(this)) {
                    makeGrowOnBonemeal(level, entity.center.relative(direction), level.getBlockState(entity.center.relative(direction)));
                }else {
                    System.out.println("Acidripia or Bondripia goofed up, centre = " + entity.center.toString());
                    System.out.println("If this happens often, you might wanna report it to the More Sniffer Flowers devs");
                    level.destroyBlock(entity.center.relative(direction), false);
                    if (direction.equals(Direction.NORTH)) level.destroyBlock(entity.center, false);
                }

            });
        }
    }
    
    private boolean isBondripable(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).getBlock() instanceof BonemealableBlock || level.getBlockState(blockPos).is(ModTags.ModBlockTags.BONMEELABLE);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof BondripiaBlockEntity entity) {
            var list = Direction.Plane.HORIZONTAL.stream().filter(direction -> super.canSurvive(level.getBlockState(entity.center.relative(direction)), level, entity.center.relative(direction))).toList();

            // System.out.println("postcheck " + Block.canSupportCenter(level, blockPos.above(), Direction.DOWN) + !level.isWaterAt(blockPos) + (list.size() == 4) + " CC needed = "+ (corruptionCheck(entity, level) && !(list.size() == 4)));
            return Block.canSupportCenter(level, blockPos.above(), Direction.DOWN) && !level.isWaterAt(blockPos) && (list.size() == 4 || corruptionCheck(entity, level));

        }
        var list = Direction.Plane.HORIZONTAL.stream().filter(direction -> super.canSurvive(level.getBlockState(blockPos.relative(direction)), level, blockPos.relative(direction))).toList();
        // System.out.println("precheck " + Block.canSupportCenter(level, blockPos.above(), Direction.DOWN) + !level.isWaterAt(blockPos) + (list.size() == 4) + getPositionsForPlant(level, blockPos).isPresent());
        return Block.canSupportCenter(level, blockPos.above(), Direction.DOWN) && !level.isWaterAt(blockPos) && list.size() == 4 && getPositionsForPlant(level, blockPos).isPresent();
    }

    private boolean corruptionCheck(BondripiaBlockEntity entity, LevelReader level){
        AtomicInteger i = new AtomicInteger();
        Direction.Plane.HORIZONTAL.forEach(direction2 -> {
            BlockPos blockPos2 = entity.center.relative(direction2);
            if (level.getBlockState(blockPos2).is(ModBlocks.ACIDRIPIA.get()))
                i.getAndIncrement();

        });
        if (level.getBlockState(entity.center).is(ModBlocks.ACIDRIPIA.get())) i.getAndIncrement();
        return !(i.get() == 0);
    }

    @Override
    public BlockState updateShape(BlockState stateOriginal, Direction dir, BlockState stateNew, LevelAccessor level, BlockPos pCurrentPos, BlockPos pNewPos) {
        if(level.getBlockEntity(pCurrentPos) instanceof BondripiaBlockEntity entity) {

            if (!this.canSurvive(stateOriginal, level, pCurrentPos)){
                Direction.Plane.HORIZONTAL.forEach(direction2 -> {
                    BlockPos blockPos2 = entity.center.relative(direction2);

                    level.destroyBlock(blockPos2, true);
                });
                level.destroyBlock(entity.center, true);

            }

        }
        return super.updateShape(stateOriginal, dir, stateNew, level, pCurrentPos, pNewPos);
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
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
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

    @Override
    public VoxelShape getShape(BlockState p_154699_, BlockGetter p_154700_, BlockPos p_154701_, CollisionContext p_154702_) {
        if(p_154699_.getValue(ModStateProperties.CENTER)) return SHAPE_CENTER;
        return SHAPE;
    }
}
