package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class GarbushBlockUpper extends AbstractXBushBlockUpper{
    public GarbushBlockUpper(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return ModItems.GARBUSH_SEEDS.get().getDefaultInstance();
    }

    @Override
    public Block getDropBlock() {
        return ModBlocks.GARNET_BLOCK.get();
    }

    @Override
    public Block getLowerBlock() {
        return ModBlocks.GARBUSH_BOTTOM.get();
    }

    @Override
    public Block getUpperBlock() {
        return ModBlocks.GARBUSH_TOP.get();
    }
}
