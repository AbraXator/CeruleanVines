package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DyespriaPlantBlock extends BushBlock implements ModCropBlock, ModEntityBlock {
    public static final MapCodec<DyespriaPlantBlock> CODEC = simpleCodec(DyespriaPlantBlock::new);
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

    public DyespriaPlantBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(getAgeProperty(), 0)
                .setValue(ModStateProperties.SHEARED, false)
                .setValue(ModStateProperties.COLOR, DyeColor.WHITE));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(getAgeProperty()).add(ModStateProperties.COLOR).add(ModStateProperties.SHEARED);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if(pPlacer instanceof ServerPlayer serverPlayer) {
            ModAdvancementCritters.PLACED_DYESPRIA_PLANT.trigger(serverPlayer);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pStack.is(Items.BONE_MEAL)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        } else if(isMaxAge(pState) && pLevel.getBlockEntity(pPos) instanceof DyespriaPlantBlockEntity entity) {
            if(pStack.getItem() instanceof DyeItem) {
                return addDye(pStack, pPlayer, pLevel, entity);
            } else if(pStack.is(Items.SHEARS)) {
                shear(pPlayer, pLevel, pPos, pState, pHand);
                return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
            }
        }
        
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if(isMaxAge(pState) && pLevel.getBlockEntity(pPos) instanceof DyespriaPlantBlockEntity entity) {
            pPlayer.addItem(Dye.stackFromDye(entity.removeDye()));

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    private ItemInteractionResult addDye(ItemStack dye, Player player, Level level, DyespriaPlantBlockEntity entity) {
        if(!level.isClientSide) {
            var stack = dye.copy();
            dye.setCount(-1);
            player.addItem(entity.add(null, entity.dye, stack));
        }
        
        level.playSound(null, entity.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, (float) (1.0F + level.random.nextFloat() * 0.2));
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return canSurvive(pState, pLevel, pCurrentPos) ? pState : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return mayPlaceOn(pLevel.getBlockState(pPos.below()));
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pNewState.is(ModBlocks.DYESCRAPIA_PLANT) && !pState.is(pNewState.getBlock()) && pLevel.getBlockEntity(pPos) instanceof DyespriaPlantBlockEntity entity && isMaxAge(pState)) {
            var dyespria = ModItems.DYESPRIA.get().getDefaultInstance();
            var dye = new ItemStack(DyeItem.byColor(entity.dye.color()), entity.dye.amount());

            Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), dyespria);
            Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), dye);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public boolean mayPlaceOn(BlockState pState) {
        return pState.is(BlockTags.DIRT) && !(pState.getBlock() instanceof FarmBlock);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_3;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return isMaxAge(pState);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        makeGrowOnTick(this, pState, pLevel, pPos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return !isMaxAge(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        makeGrowOnBonemeal(pLevel, pPos, pState);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if(player.isShiftKeyDown() && isMaxAge(state) && level.getBlockEntity(pos) instanceof DyespriaPlantBlockEntity entity) {
            var stack =  ModItems.DYESPRIA.toStack();
            stack.set(ModDataComponents.DYE, entity.dye);
            return stack;
        }
        
        return ModItems.DYESPRIA_SEEDS.toStack();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DyespriaPlantBlockEntity(pPos, pState);
    }
}
