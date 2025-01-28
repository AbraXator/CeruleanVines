package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.antlr.v4.runtime.atn.LL1Analyzer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class VivicusTrunkPlacer extends TrunkPlacer {
    public static final Codec<VivicusTrunkPlacer> CODEC = RecordCodecBuilder.create(
            p_338099_ -> trunkPlacerParts(p_338099_).apply(p_338099_, VivicusTrunkPlacer::new)
    );

    public VivicusTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.VIVICUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        Map<BlockPos, BlockPos> blocks = new LinkedHashMap<>();
        int lastLogHeight = pFreeTreeHeight - ((int) Mth.randomBetween(pRandom, 2, 3));
        BlockPos.MutableBlockPos mainTrunk = pPos.mutable();
        for(int i = 0; i < (pFreeTreeHeight); i++) {
            this.placeLog(pLevel, pBlockSetter, pRandom, mainTrunk, pConfig);
            
            if (i == 0) {
                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    int outerHeight = pRandom.nextInt(3);;
                    int cornerHeight = outerHeight + (pRandom.nextInt(2) - 1);
                    int innerHeight = Math.min(outerHeight + pRandom.nextIntBetweenInclusive(5, 7), pFreeTreeHeight - 1);
                    if(pRandom.nextDouble() <= 0.90D) {
                        BlockPos blockPosInner = pPos.relative(direction);
                        for(int j = 0; j < innerHeight; j++) {
                            this.placeLog(pLevel, pBlockSetter, pRandom, blockPosInner.above(j), pConfig);
                        }

                        if(pRandom.nextDouble() <= 0.8D) {
                            ret.add(new FoliagePlacer.FoliageAttachment(blockPosInner.above(innerHeight), 4, false));
                        }

                        if(pRandom.nextDouble() <= 0.90D) {
                            BlockPos blockPosCorner = blockPosInner.relative(pRandom.nextDouble() > 0.5D ? direction.getClockWise() : direction.getCounterClockWise());
                            for(int j = 0; j < cornerHeight; j++) {
                                this.placeLog(pLevel, pBlockSetter, pRandom, blockPosCorner.above(j), pConfig);
                            }
                        }

                        if(pRandom.nextDouble() <= 0.90D) {
                            BlockPos blockPosOuter = blockPosInner.relative(direction);
                            for(int j = 0; j < outerHeight; j++) {
                                this.placeLog(pLevel, pBlockSetter, pRandom, blockPosOuter.above(j), pConfig);
                            }
                        }
                    }
                }
            }
            
            mainTrunk.move(Direction.UP);
        }
        
        ret.add(new FoliagePlacer.FoliageAttachment(mainTrunk.below(1), 0, false));
        return ret;
    }

    private void placeStump(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos.MutableBlockPos stumpPos, TreeConfiguration pConfig) {
        if (pRandom.nextDouble() <= 0.3D) {
            for (int height = 0; height < pRandom.nextInt(3); height++) {
                this.placeLog(pLevel, pBlockSetter, pRandom, stumpPos.immutable().above(height), pConfig);
            }
        }
    }
    
    @Override
    protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return super.validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, blockState -> blockState.is(ModTags.ModBlockTags.VIVICUS_TREE_REPLACABLE));
    }
}
