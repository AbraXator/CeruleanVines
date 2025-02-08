package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.AABB;

import java.util.concurrent.atomic.AtomicInteger;

public class CuredGrassBlock extends SpreadingSnowyDirtBlock {
    public CuredGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        double d0 = Math.abs(pEntity.getDeltaMovement().y);
        if (d0 < 0.1 && !pEntity.isSteppingCarefully()) {
            double d1 = 0.8;
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    levelReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(levelReader, blockpos)
            );
            return i < levelReader.getMaxLightLevel();
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) return;
            level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) return;
            BlockState blockstate = this.defaultBlockState();

            for (int i = 0; i < 4; i++) {
                BlockPos blockpos = pos.offset(random.nextInt(5) - 3, random.nextInt(5) - 3, random.nextInt(5) - 5);
                if (level.getBlockState(blockpos).is(ModBlocks.CORRUPTED_GRASS_BLOCK.get()) && canBeGrass(blockstate, level, blockpos)) {
                    level.setBlockAndUpdate(
                            blockpos, blockstate.setValue(SNOWY, level.getBlockState(blockpos.above()).is(Blocks.SNOW))
                    );
                }
            }
            AtomicInteger CorruptedCount = new AtomicInteger();
            var aabb = AABB.ofSize(pos.getCenter(), 2, 2, 2);
            BlockPos.betweenClosedStream(aabb).forEach(blockPos -> {
                if (level.getBlockState(blockPos).is(ModBlocks.CORRUPTED_GRASS_BLOCK.get())) {
                    level.setBlockAndUpdate(
                            blockPos, blockstate.setValue(SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW))
                    );
                    CorruptedCount.getAndIncrement();
                }
            });
            if (CorruptedCount.get() == 0)
                level.setBlockAndUpdate(
                        pos, Blocks.GRASS_BLOCK.defaultBlockState().setValue(SNOWY, level.getBlockState(pos.above()).is(Blocks.SNOW))
                );
        }
    }

}
