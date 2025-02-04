package net.abraxator.moresnifferflowers.blocks.xbush;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class GarbushBlockLower extends AbstractXBushBlockBase {
    public GarbushBlockLower(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModItems.GARBUSH_SEEDS.get().getDefaultInstance();
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(getAge(pState) == 7 && pRandom.nextInt(100) < 10 && isLower(pState)) {
            pLevel.addAlwaysVisibleParticle(
                    ModParticles.GARBUSH.get(),
                    true,
                    (double)pPos.getX() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                    (double)pPos.getY() + pRandom.nextDouble() + pRandom.nextDouble(),
                    (double)pPos.getZ() + 0.5 + pRandom.nextDouble() / 3.0 * (double)(pRandom.nextBoolean() ? 1 : -1),
                    0.0,
                    0.07,
                    0.0
            );
        }
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
    public Block getCorruptedLowerBlock() {
        return ModBlocks.AMBUSH_BOTTOM.get();
    }

    @Override
    public Block getUpperBlock() {
        return ModBlocks.GARBUSH_TOP.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return ModBlocks.AMBUSH_TOP.get();
    }
}
