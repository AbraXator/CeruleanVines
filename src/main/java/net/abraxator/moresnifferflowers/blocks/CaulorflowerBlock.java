package net.abraxator.moresnifferflowers.blocks;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import static net.abraxator.moresnifferflowers.init.ModStateProperties.*;

public class CaulorflowerBlock extends Block implements BonemealableBlock, ModCropBlock, Colorable {
    public CaulorflowerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(FLIPPED, true)
                .setValue(getAgeProperty(), 0)
                .setValue(getColorAndEmptyProperties().getA(), DyeColor.WHITE)
                .setValue(getColorAndEmptyProperties().getB(), true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING, FLIPPED, getAgeProperty(), getColorAndEmptyProperties().getA(), getColorAndEmptyProperties().getB());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if(canSurvive(pState, pLevel, pCurrentPos)) {
            return pState.setValue(FLIPPED, pCurrentPos.getY() % 2 == 0);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        if(state.is(this)) {
            return state.setValue(FLIPPED, pContext.getClickedPos().getY() % 2 == 0);
        }
        return super.getStateForPlacement(pContext).setValue(FLIPPED, pContext.getClickedPos().getY() % 2 == 0).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockPos = pPos.below();
        BlockState blockState = pLevel.getBlockState(blockPos);
        BlockPos wallPos = pPos.relative(pState.getValue(FACING).getOpposite());
        BlockState wallState = pLevel.getBlockState(wallPos);
        return (blockState.is(this) && getAge(blockState) > 0) || blockState.isFaceSturdy(pLevel, blockPos, Direction.UP) || wallState.isFaceSturdy(pLevel, wallPos, pState.getValue(FACING));
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if(pRandom.nextFloat() < 0.15) {
            grow(pLevel, pPos, false);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        Optional<BlockPos> highestPos = highestPos(pLevel, pPos, true);
        
        if(highestPos.isPresent()) {
            BlockState blockState = pLevel.getBlockState(highestPos.get());
            return pLevel.getBlockState(highestPos.get().above()).is(Blocks.AIR) || (blockState.hasProperty(getAgeProperty()) && !isMaxAge(blockState));
        }
        
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        grow(pLevel, pPos, true);
    }

    protected void grow(ServerLevel pLevel, BlockPos originalPos, boolean bonemeal) {
        if(!isMaxAge(pLevel.getBlockState(originalPos))) {
            makeGrowOnBonemeal(pLevel, originalPos, pLevel.getBlockState(originalPos));
        } else {
            highestPos(pLevel, originalPos, bonemeal).ifPresent(highestPos -> {
                var posBelow = highestPos.below();
                var stateBelow = pLevel.getBlockState(posBelow);
                if (isMaxAge(stateBelow)) {
                    pLevel.setBlockAndUpdate(highestPos, this.defaultBlockState()
                            .setValue(FLIPPED, highestPos.getY() % 2 == 0)
                            .setValue(FACING, stateBelow.getValue(FACING))
                            .setValue(getColorAndEmptyProperties().getA(), stateBelow.getValue(getColorAndEmptyProperties().getA()))
                            .setValue(getColorAndEmptyProperties().getB(), stateBelow.getValue(getColorAndEmptyProperties().getB())));
                } else {
                    makeGrowOnBonemeal(pLevel, posBelow, stateBelow);
                }
            });
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (harvestable(pState)) {
            popResource(pLevel, pPos, Dye.stackFromDye(new Dye(pState.getValue(COLOR), 1)));
            pLevel.playSound(
                    null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F
            );
            BlockState blockstate = pState.setValue(getAgeProperty(), 1);
            pLevel.setBlock(pPos, blockstate, 2);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }
    
    private boolean harvestable(BlockState blockState) {
        return isMaxAge(blockState) && !getDyeFromBlock(blockState).isEmpty();
    }

    private Optional<BlockPos> highestPos(BlockGetter level, BlockPos originalPos, boolean bonemeal) {
        var lowestPos = getLowestPos(level, originalPos);
        if(lowestPos.isEmpty()) { return Optional.empty(); }
        var highestPos = getLastConnectedBlock(level, lowestPos.get(), Direction.UP);
        return highestPos.filter(blockPos1 -> bonemeal || !((lowestPos.get().getY() + 5) <= blockPos1.getY())).map(BlockPos::above);
    }

    private Optional<BlockPos> getLowestPos(BlockGetter level, BlockPos originalPos) {
        var posDown = getLastConnectedBlock(level, originalPos, Direction.DOWN).map(BlockPos::above);
        return posDown.filter(blockPos -> level.getBlockState(blockPos).is(this));
    }

    public Optional<BlockPos> getLastConnectedBlock(BlockGetter pGetter, BlockPos pPos, Direction pDirection) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();

        while (pGetter.getBlockState(blockpos$mutableblockpos).is(this)){
            blockpos$mutableblockpos.move(pDirection);
        }

        return pDirection == Direction.DOWN ? Optional.of(blockpos$mutableblockpos) : (pGetter.getBlockState(blockpos$mutableblockpos).is(Blocks.AIR) ? Optional.of(blockpos$mutableblockpos.below()) : Optional.empty());
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(this)) {
            return;
        }

        var stateBelow = level.getBlockState(pos.below());
        if(!stateBelow.is(this) && !stateBelow.is(Blocks.AIR)) {
            popResource(level, pos, new ItemStack(ModItems.CAULORFLOWER_SEEDS.get()));
        }
        
        if(!isColorEmpty(state) && isMaxAge(state)) {
            popResource(level, pos, Dye.stackFromDye(new Dye(state.getValue(COLOR), 1)));
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_2;
    }

    @Override
    public void colorBlock(Level level, BlockPos blockPos, BlockState blockState, Dye dye) {
        Colorable.super.colorBlock(level, blockPos, blockState.setValue(getColorAndEmptyProperties().getB(), false), dye);
        if(level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 6, false) instanceof ServerPlayer serverPlayer)
            ModAdvancementCritters.USED_DYESPRIA.trigger(serverPlayer);
    }

    @Override
    public boolean canBeColored(BlockState blockState, Dye dye) {
        return Colorable.super.canBeColored(blockState, dye) || Colorable.super.isColorEmpty(blockState);
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        return Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
            dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFFFFFFF);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFF9d979b);
            dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFF474f52);
            dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF26262e);
            dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFF835432);
            dyeColorHexFormatMap.put(DyeColor.RED, 0xFFd5544e);
            dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFf89635);
            dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFffee53);
            dyeColorHexFormatMap.put(DyeColor.LIME, 0xFF80c71f);
            dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFF5e7c16);
            dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF00AACC);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFF70d9e4);
            dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFF4753ac);
            dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFFb15fc2);
            dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFd276b9);
            dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFf8b0c4);
        });
    }
    
    @Override
    public void onAddDye(@Nullable ItemStack destinationStack, ItemStack dye, int amount) {
        
    }
}
