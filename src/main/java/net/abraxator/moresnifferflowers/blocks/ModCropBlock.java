package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;

;

public interface ModCropBlock extends BonemealableBlock {
    IntegerProperty getAgeProperty();

    default boolean isMaxAge (BlockState blockState) {
        return getAge(blockState) >= getMaxAge();
    }

    default int getMaxAge() {
        return getAgeProperty().getPossibleValues().stream().toList().get(getAgeProperty().getPossibleValues().size() - 1);
    }

    default int getAge(BlockState blockState) {
        return blockState.getValue(getAgeProperty());
    }

    default void makeGrowOnTick(Block block, BlockState blockState, Level level, BlockPos blockPos) {
        if (!isMaxAge(blockState) && level.isAreaLoaded(blockPos, 1) && level.getRawBrightness(blockPos, 0) >= 9) {
            float f = getGrowthSpeed(blockState, level, blockPos);
            if (ForgeHooks.onCropsGrowPre(level, blockPos, blockState, level.getRandom().nextInt((int)(25.0F / f) + 1) == 0)) {
                level.setBlock(blockPos, blockState.setValue(getAgeProperty(), (blockState.getValue(getAgeProperty()) + 1)), 2);
                ForgeHooks.onCropsGrowPost(level, blockPos, blockState);
            }
        }
    }

    default void makeGrowOnBonemeal(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(getAgeProperty(), isMaxAge(blockState) ? getAge(blockState) : getAge(blockState) + 1), 2);
    }

    default boolean mayPlaceOn(BlockState pState) {
        return pState.is(Blocks.FARMLAND) || pState.getBlock() instanceof FarmBlock || pState.is(TagKey.create(Registries.BLOCK, new ResourceLocation("supplementaries", "planters")));
    }
    
    default void shear(Player player, Level level, BlockPos blockPos, BlockState blockState, InteractionHand hand) {
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, player.getItemInHand(hand));
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.SHEARED, true));
        level.playSound(null, blockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, level.getBlockState(blockPos)));
        player.getItemInHand(hand).hurtAndBreak(1, player, player1 -> {
            player1.broadcastBreakEvent(hand);
        });
    }

    public static float getGrowthSpeed(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        float f = 1.0F;
        BlockPos blockpos = pPos.below();
        var pBlock = pState.getBlock();
        
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = pLevel.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.canSustainPlant(pLevel, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, (net.minecraftforge.common.IPlantable) pState.getBlock())) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(pLevel, pPos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pPos.north();
        BlockPos blockpos2 = pPos.south();
        BlockPos blockpos3 = pPos.west();
        BlockPos blockpos4 = pPos.east();
        boolean flag = pLevel.getBlockState(blockpos3).is(pBlock) || pLevel.getBlockState(blockpos4).is(pBlock);
        boolean flag1 = pLevel.getBlockState(blockpos1).is(pBlock) || pLevel.getBlockState(blockpos2).is(pBlock);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = pLevel.getBlockState(blockpos3.north()).is(pBlock) || pLevel.getBlockState(blockpos4.north()).is(pBlock) || pLevel.getBlockState(blockpos4.south()).is(pBlock) || pLevel.getBlockState(blockpos3.south()).is(pBlock);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    record PosAndState(BlockPos blockPos, BlockState state) {}
}
