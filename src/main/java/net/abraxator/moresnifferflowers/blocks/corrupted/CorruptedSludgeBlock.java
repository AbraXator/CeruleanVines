package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.blockentities.CorruptedSludgeBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ModEntityBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class CorruptedSludgeBlock extends Block implements ModEntityBlock {
    public CorruptedSludgeBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(defaultBlockState().setValue(ModStateProperties.USES_4, 3).setValue(ModStateProperties.CURED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ModStateProperties.USES_4, ModStateProperties.CURED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CorruptedSludgeBlockEntity(pPos, pState);
    }
}
