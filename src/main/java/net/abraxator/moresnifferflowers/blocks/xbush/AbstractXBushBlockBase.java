package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.blockentities.XbushBlockEntity;
import net.abraxator.moresnifferflowers.blocks.Corruptable;
import net.abraxator.moresnifferflowers.blocks.ModCropBlock;
import net.abraxator.moresnifferflowers.blocks.ModEntityDoubleTallBlock;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractXBushBlockBase extends ModEntityDoubleTallBlock implements ModCropBlock, Corruptable {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    public static final int AGE_TO_GROW_UP = 4;

    public AbstractXBushBlockBase(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_8;
    }

    @Override   
    public boolean isRandomlyTicking(BlockState pState) {
        return !isMaxAge(pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(isUpper(pState) && pLevel.getBlockEntity(pPos) instanceof XbushBlockEntity entity && entity.hasGrown) {
            return super.getShape(pState, pLevel, pPos, pContext);
        } else {
            return SHAPE;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return this.mayPlaceOn(pLevel.getBlockState(pPos.below())) && sufficientLight(pLevel, pPos) && super.canSurvive(pState, pLevel, pPos);
    }
    
    @Override
    public boolean mayPlaceOn(BlockState pState) {
        return ModCropBlock.super.mayPlaceOn(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ModStateProperties.AGE_8);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if(pEntity instanceof Ravager && pLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            pLevel.destroyBlock(pPos, true, pEntity);
        }
        
        onCorruptByEntity(pEntity, pPos, pState, this, pLevel);

        super.entityInside(pState, pLevel, pPos, pEntity);
    }
    
    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return false;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(getAge(pState) == 7 && pRandom.nextInt(100) < 10 && isLower(pState)) {
            pLevel.addAlwaysVisibleParticle(
                    ModParticles.AMBUSH.get(), 
                    true,
                    (double)pPos.getX() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                    (double)pPos.getY() + pRandom.nextDouble() + pRandom.nextDouble(),
                    (double)pPos.getZ() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                    0.0,
                    0.07,
                    0.0
            );
        }
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        float f = ModCropBlock.getGrowthSpeed(pState, pLevel, pPos);
        if(pRandom.nextInt((int) ((25.0F / f) + 1)) == 0) {
            this.grow(pLevel, pState, pPos, 1);
        }
    }

    public void grow(ServerLevel pLevel, BlockState pState, BlockPos pPos, int i) {
        int k = Math.min(getAge(pState) + i, getMaxAge());
        if(this.canGrow(pLevel, pPos, pState, k)) {
            pLevel.setBlock(pPos, pState.setValue(getAgeProperty(), k), 2);
            if(k >= AGE_TO_GROW_UP && isLower(pState)) {
                pLevel.setBlock(pPos.above(), getUpperBlock().defaultBlockState().setValue(getAgeProperty(), k), 3);
            }

            getLowerHalf(pLevel, pPos, pState).ifPresent(posAndState -> {
                if(pLevel.getBlockEntity(posAndState.blockPos().above()) instanceof XbushBlockEntity entity) {
                    entity.growProgress = 0;
                } 
            });
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        Optional<PosAndState> optional = getLowerHalf(pLevel, pPos, pState);
        var stack = pPlayer.getItemInHand(pHand);

        if((!isMaxAge(pState) && stack.is(Items.BONE_MEAL)) || optional.isEmpty()) {
            return InteractionResult.PASS;
        }

        if(pLevel.getBlockEntity(optional.get().blockPos().above()) instanceof XbushBlockEntity entity && entity.hasGrown) {
            var lowerPos = isLower(pState) ? pPos : pPos.below();
            popResource(pLevel, pPos, new ItemStack(getDropBlock()));
            pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);

            for(int i = 0; i <= 1; i++) {
                var halfPos = i == 0 ? lowerPos : lowerPos.above();
                var state = pLevel.getBlockState(halfPos).setValue(getAgeProperty(), 7);
                pLevel.setBlock(halfPos, state, 3);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, halfPos, GameEvent.Context.of(pPlayer, state));
            }

            entity.reset();
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean canGrowInto(BlockState state) {
        return state.isAir() || state.is(getUpperBlock());
    }

    private boolean sufficientLight(LevelReader pLevel, BlockPos pPos) {
        return pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos);
    }

    @Override
    public int getMaxAge() {
        return ModCropBlock.super.getMaxAge() - 1;
    }

    private boolean canGrow(LevelReader pLevel, BlockPos pPos, BlockState pState, int k) {
        return !this.isMaxAge(pState) && sufficientLight(pLevel, pPos) && (k < AGE_TO_GROW_UP || canGrowInto(pLevel.getBlockState(pPos.above()))) && isLower(pState);
    }

    @Override
    public void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var lowerHalf = getLowerHalf(level, pos, oldState);
        lowerHalf.ifPresent(posAndState -> {
            level.setBlockAndUpdate(posAndState.blockPos(), corruptedBlock.withPropertiesOf(oldState));
            if(getAge(lowerHalf.get().state()) > 3) {
                getCorruptedBlock(getUpperBlock(), level).ifPresent(block ->
                        level.setBlockAndUpdate(posAndState.blockPos().above(), block.withPropertiesOf(level.getBlockState(posAndState.blockPos().above()))));
            }
        });
    }

    Optional<PosAndState> getLowerHalf(LevelReader level, BlockPos blockPos, BlockState state) {
        if(isLower(state)) {
            return Optional.of(new PosAndState(blockPos, state));
        } else {
            BlockPos posBelow = blockPos.below();
            BlockState stateBelow = level.getBlockState(posBelow);
            return isLower(stateBelow) ? Optional.of(new PosAndState(posBelow, stateBelow)) : Optional.empty();
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        Optional<PosAndState> posAndState = this.getLowerHalf(pLevel, pPos, pState);
        return posAndState.isPresent() && this.canGrow(pLevel, posAndState.get().blockPos(), posAndState.get().state(), getAge(posAndState.get().state()) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        this.getLowerHalf(pLevel, pPos, pState).ifPresent(posAndState -> {
            if(pState.getValue(ModStateProperties.AGE_8) < 8) {
                this.grow(pLevel, posAndState.state(), posAndState.blockPos(), 1);
            } 
        });
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {}
    
    public abstract Block getDropBlock();
}
