package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blockentities.VivicusHangingSignBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.VivicusSignBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModHangingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModStandingSignBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WoodType;

public class VivicusHangingSignBlock extends ModHangingSignBlock implements ColorableVivicusBlock {
    public VivicusHangingSignBlock(WoodType p_56991_, Properties p_56990_) {
        super(p_56991_, p_56990_);
        this.defaultBlockState().setValue(ModStateProperties.COLOR, DyeColor.WHITE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ModStateProperties.COLOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new VivicusHangingSignBlockEntity(pPos, pState);
    }
}
