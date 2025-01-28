package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoliagePlacer.class)
public class FoliagePlacerMixin {
    @Inject(method = "tryPlaceLeaf", at = @At("HEAD"), cancellable = true)
    private static void moresnifferflowers$tryPlaceLeaf(LevelSimulatedReader pLevel, FoliagePlacer.FoliageSetter pFoliageSetter, RandomSource pRandom, TreeConfiguration pTreeConfiguration, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        if (pTreeConfiguration.trunkProvider.getState(pRandom, pPos).is(ModBlocks.CORRUPTED_LOG.get())) {
            BlockState blockstate = pTreeConfiguration.foliageProvider.getState(pRandom, pPos);
            if (blockstate.hasProperty(BlockStateProperties.WATERLOGGED)) {
                blockstate = blockstate.setValue(
                        BlockStateProperties.WATERLOGGED,
                        Boolean.valueOf(pLevel.isFluidAtPosition(pPos, p_225638_ -> p_225638_.isSourceOfType(Fluids.WATER)))
                );
            }

            pFoliageSetter.set(pPos, blockstate);
            cir.setReturnValue(true);
        }
    }
}
