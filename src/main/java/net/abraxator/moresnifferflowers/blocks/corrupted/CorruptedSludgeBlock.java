package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.blockentities.CorruptedSludgeBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ModEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CorruptedSludgeBlock extends Block implements ModEntityBlock {
    public CorruptedSludgeBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CorruptedSludgeBlockEntity(pPos, pState);
    }
}
