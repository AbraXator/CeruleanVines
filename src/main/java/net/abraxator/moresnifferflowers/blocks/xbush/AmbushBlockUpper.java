package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class AmbushBlockUpper extends AbstractXBushBlockUpper{
    public AmbushBlockUpper(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModItems.AMBUSH_SEEDS.get().getDefaultInstance();
    }

    @Override
    public Block getDropBlock() {
        return ModBlocks.AMBER_BLOCK.get();
    }

    @Override
    public Block getLowerBlock() {
        return ModBlocks.AMBUSH_BOTTOM.get();
    }


    @Override
    public Block getCorruptedLowerBlock() {
        return ModBlocks.GARBUSH_BOTTOM.get();
    }

    @Override
    public Block getUpperBlock() {
        return ModBlocks.AMBUSH_TOP.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return ModBlocks.GARBUSH_TOP.get();
    }
}
