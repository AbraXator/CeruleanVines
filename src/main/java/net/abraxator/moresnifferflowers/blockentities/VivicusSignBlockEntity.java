package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VivicusSignBlockEntity extends SignBlockEntity {
    public VivicusSignBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.VIVICUS_SIGN.get(), pPos, pBlockState);
    }
        @Override
     public BlockEntityType<?> getType() {
            return ModBlockEntities.VIVICUS_SIGN.get();
    }
}
