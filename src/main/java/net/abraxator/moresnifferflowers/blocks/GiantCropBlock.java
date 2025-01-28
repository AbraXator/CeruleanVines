package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class GiantCropBlock extends Block implements ModEntityBlock, Bonmeelable {
    public GiantCropBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(ModStateProperties.CENTER, false));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pLevel.getBlockEntity(pPos) instanceof GiantCropBlockEntity entity) {
            if(entity.state == 1) {
                entity.canGrow = true;
            }
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(pState.getValue(ModStateProperties.CENTER)) {
            pLevel.getBlockTicks().schedule(new ScheduledTick<>(this, pPos, pLevel.getGameTime() + 7, pLevel.nextSubTickCount()));
            if(pLevel.getBlockEntity(pPos) instanceof GiantCropBlockEntity entity && entity.state == 0) {
                entity.state = 1;
            }

            if(pLevel instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ModParticles.GIANT_CROP.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ModStateProperties.CENTER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GiantCropBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return tickerHelper(pLevel);
    }

    @Override
    public void performBonmeel(BlockPos blockPos, BlockState blockState, Level level, Player player) {
        this.blockPosList(blockPos).forEach(pos -> {
            pos = pos.immutable();
            level.destroyBlock(pos, false);
            level.setBlockAndUpdate(pos, this.cropMap().get(blockState.getBlock()).getA().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(blockPos.above())));
            if(level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                entity.center = blockPos.above();
            }
        });

        if(player != null) {
            if (!player.getAbilities().instabuild) {
                player.getMainHandItem().shrink(1);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancementCritters.USED_BONMEEL.trigger(serverPlayer);
            }
        }

        level.playLocalSound(blockPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @Override
    public boolean canBonmeel(BlockPos blockPos, BlockState blockState, Level level) {
        Block crop = blockState.getBlock();

        return StreamSupport.stream(this.blockPosList(blockPos).spliterator(), false).allMatch(pos -> {
            BlockState state = level.getBlockState(pos);
            int cropY = blockPos.getY();
            var PROPERTY = this.cropMap().get(crop).getB().getA();
            int MAX_AGE = this.cropMap().get(crop).getB().getB();

            if(pos.getY() == cropY) {
                return state.is(blockState.getBlock()) && state.is(ModTags.ModBlockTags.BONMEELABLE) && state.getValue(PROPERTY) == MAX_AGE;
            } else {
                return state.canBeReplaced();
            }
        });
    }

    private Map<Block, Pair<Block, Pair<IntegerProperty, Integer>>> cropMap() {
        return Map.of(
                Blocks.CARROTS, new Pair<>(ModBlocks.GIANT_CARROT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.POTATOES, new Pair<>(ModBlocks.GIANT_POTATO.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.NETHER_WART, new Pair<>(ModBlocks.GIANT_NETHERWART.get(), new Pair<>(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE)),
                Blocks.BEETROOTS, new Pair<>(ModBlocks.GIANT_BEETROOT.get(), new Pair<>(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE)),
                Blocks.WHEAT, new Pair<>(ModBlocks.GIANT_WHEAT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE))
        );
    }

    private Iterable<BlockPos> blockPosList(BlockPos blockPos) {
        return BlockPos.betweenClosed(
                blockPos.getX() - 1,
                blockPos.getY() - 0,
                blockPos.getZ() - 1,
                blockPos.getX() + 1,
                blockPos.getY() + 2,
                blockPos.getZ() + 1
        );
    }
}