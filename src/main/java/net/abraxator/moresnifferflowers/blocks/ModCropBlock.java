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
import net.minecraft.world.entity.LivingEntity;
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
import net.neoforged.neoforge.common.CommonHooks;

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
            if (CommonHooks.canCropGrow(level, blockPos, blockState, level.getRandom().nextInt((int)(25.0F / f) + 1) == 0)) {
                level.setBlock(blockPos, blockState.setValue(getAgeProperty(), (blockState.getValue(getAgeProperty()) + 1)), 2);
                CommonHooks.fireCropGrowPost(level, blockPos, blockState);
            }
        }
    }

    default void makeGrowOnBonemeal(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(getAgeProperty(), isMaxAge(blockState) ? getAge(blockState) : getAge(blockState) + 1), 2);
    }

    default boolean mayPlaceOn(BlockState pState) {
        return pState.is(Blocks.FARMLAND) || pState.getBlock() instanceof FarmBlock || pState.is(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("supplementaries", "planters")));
    }
    
    default void shear(Player player, Level level, BlockPos blockPos, BlockState blockState, InteractionHand hand) {
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, player.getItemInHand(hand));
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.SHEARED, true));
        level.playSound(null, blockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, level.getBlockState(blockPos)));
        player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
    }

    public static float getGrowthSpeed(BlockState blockState, BlockGetter p_52274_, BlockPos p_52275_) {
        Block p_52273_ = blockState.getBlock();
        float f = 1.0F;
        BlockPos blockpos = p_52275_.below();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                float f1 = 0.0F;
                BlockState blockstate = p_52274_.getBlockState(blockpos.offset(i, 0, j));
                net.neoforged.neoforge.common.util.TriState soilDecision = blockstate.canSustainPlant(p_52274_, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, blockState);
                if (soilDecision.isDefault() ? blockstate.getBlock() instanceof net.minecraft.world.level.block.FarmBlock : soilDecision.isTrue()) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(p_52274_, p_52275_.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = p_52275_.north();
        BlockPos blockpos2 = p_52275_.south();
        BlockPos blockpos3 = p_52275_.west();
        BlockPos blockpos4 = p_52275_.east();
        boolean flag = p_52274_.getBlockState(blockpos3).is(p_52273_) || p_52274_.getBlockState(blockpos4).is(p_52273_);
        boolean flag1 = p_52274_.getBlockState(blockpos1).is(p_52273_) || p_52274_.getBlockState(blockpos2).is(p_52273_);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = p_52274_.getBlockState(blockpos3.north()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos4.north()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos4.south()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos3.south()).is(p_52273_);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    record PosAndState(BlockPos blockPos, BlockState state) {}
}
