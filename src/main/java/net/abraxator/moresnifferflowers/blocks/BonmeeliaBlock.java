package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;

public class BonmeeliaBlock extends BushBlock implements ModCropBlock {
    public static final MapCodec<BonmeeliaBlock> CODEC = simpleCodec(pProperties -> new BonmeeliaBlock(pProperties, false));
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
    public static final BooleanProperty HAS_BOTTLE = BooleanProperty.create("bottle");
    public static final BooleanProperty SHOW_HINT = BooleanProperty.create("hint");
    public static final BooleanProperty HAS_JAR = BooleanProperty.create("jar");
    public static final int MAX_AGE = AGE
            .getAllValues()
            .map(Property.Value::value)
            .max(Integer::compare)
            .orElse(0);
    
    private final boolean wilted;

    public BonmeeliaBlock(Properties pProperties, boolean wilted) {
        super(pProperties);
        registerDefaultState(this.defaultBlockState().setValue(HAS_BOTTLE, false).setValue(SHOW_HINT, false).setValue(AGE, 0).setValue(HAS_JAR, false));
        this.wilted = wilted;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE, HAS_BOTTLE, SHOW_HINT, HAS_JAR);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return this.mayPlaceOn(pLevel.getBlockState(pPos.below()));
    }

    @Override
    public boolean mayPlaceOn(BlockState pState) {
        return ModCropBlock.super.mayPlaceOn(pState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pStack.is(Items.GLASS_BOTTLE) && canInsertBottle(pState)) {
            return addBottle(pLevel, pPos, pState, pStack);
        } else if(pStack.is(Items.BONE_MEAL)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        } 
            
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pState.getValue(HAS_BOTTLE) && pState.getValue(AGE) >= MAX_AGE) {
            return takeJarOfBonmeel(pLevel, pPos, pState);
        } else if (!pState.getValue(HAS_BOTTLE) && getAge(pState) >= 3) {
            return hint(pLevel, pPos, pState);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.setBlock(pPos, pState.setValue(SHOW_HINT, false), 3);
    }
    
    private ItemInteractionResult addBottle(Level level, BlockPos blockPos, BlockState blockState, ItemStack stack) {
        if(!level.isClientSide) {
            level.setBlock(blockPos, blockState.setValue(HAS_BOTTLE, true), 3);
            stack.shrink(1);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private InteractionResult takeJarOfBonmeel(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(AGE, 3).setValue(HAS_BOTTLE, false), 3);
        popResource(level, blockPos, wilted ? ModItems.JAR_OF_ACID.toStack() : ModItems.JAR_OF_BONMEEL.toStack());
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResult hint(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(SHOW_HINT, true), 3);
        level.getBlockTicks().schedule(new ScheduledTick<>(this, blockPos, level.getGameTime() + 40, level.nextSubTickCount()));
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return getAge(pState) < 3 || (getAge(pState) >= 3 && pState.getValue(HAS_BOTTLE));
    }

    private boolean canInsertBottle(BlockState blockState) {
        return blockState.getValue(AGE) == 3 && !blockState.getValue(HAS_BOTTLE);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!isMaxAge(pState)) {
            pLevel.setBlockAndUpdate(pPos, pState
                    .setValue(AGE, getAge(pState) + 1)
                    .setValue(HAS_JAR, (getAge(pState) + 1) == MAX_AGE && pState.getValue(HAS_BOTTLE)));
            var particle = new DustParticleOptions(wilted ? Vec3.fromRGB24(0xaeff5c).toVector3f() : Vec3.fromRGB24(11162034).toVector3f(), 1F);
            if (getAge(pState) >= 3) {
                for (int i = 0; i <= pRandom.nextIntBetweenInclusive(5, 10); i++) {
                    pLevel.sendParticles(
                            particle,
                            pPos.getX() + pRandom.nextDouble(),
                            pPos.getY() + pRandom.nextDouble(),
                            pPos.getZ() + pRandom.nextDouble(),
                            1, 0, 0, 0, 0.3D);
                }
            }
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return getAge(pState) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        makeGrowOnBonemeal(pLevel, pPos, pState);
    }
}
