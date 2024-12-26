package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.blockentities.XbushBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ModEntityBlock;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractXBushBlockUpper extends AbstractXBushBlockBase implements ModEntityBlock {
    public static final int AGE_TO_GROW_UP = 4;

    public AbstractXBushBlockUpper(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        level.setBlockAndUpdate(pos, corruptedBlock.withPropertiesOf(oldState));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        ENTITY_POS = pPos;
        return new XbushBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return tickerHelper(pLevel);
    }
}
