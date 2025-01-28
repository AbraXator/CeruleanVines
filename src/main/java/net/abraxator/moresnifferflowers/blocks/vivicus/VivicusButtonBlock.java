package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

public class VivicusButtonBlock extends ButtonBlock implements ColorableVivicusBlock {
    public VivicusButtonBlock(BlockSetType p_273462_, int p_273212_, Properties p_273290_) {
        super(p_273462_, p_273212_, p_273290_);
        defaultBlockState().setValue(ModStateProperties.COLOR, DyeColor.WHITE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ModStateProperties.COLOR);
    }
}
