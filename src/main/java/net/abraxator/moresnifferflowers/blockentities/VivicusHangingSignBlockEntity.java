package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VivicusHangingSignBlockEntity extends HangingSignBlockEntity {
    public VivicusHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<VivicusHangingSignBlockEntity> getType() {
        return ModBlockEntities.VIVICUS_HANGING_SIGN.get();
    }

}