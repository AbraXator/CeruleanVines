package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class VivicusTrunkPlacerUnused extends TrunkPlacer {
    public static final MapCodec<VivicusTrunkPlacerUnused> CODEC = RecordCodecBuilder.mapCodec(
            p_259008_ -> trunkPlacerParts(p_259008_)
                    .and(
                            p_259008_.group(
                                    IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter(p_226242_ -> p_226242_.extraBranchSteps),
                                    Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter(p_226240_ -> p_226240_.placeBranchPerLogProbability),
                                    IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter(p_226238_ -> p_226238_.extraBranchLength),
                                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(p_226234_ -> p_226234_.canGrowThrough)
                                    )
                    )
                    .apply(p_259008_, VivicusTrunkPlacerUnused::new)
    );
    
    private final IntProvider extraBranchSteps;
    private final float placeBranchPerLogProbability;
    private final IntProvider extraBranchLength;
    private final HolderSet<Block> canGrowThrough;
    private final IntProvider stumpChance;

    public VivicusTrunkPlacerUnused(int pBaseHeight, int pHeightRandA, int pHeightRandB, IntProvider extraBranchSteps, float placeBranchPerLogProbability, IntProvider extraBranchLength, HolderSet<Block> canGrowThrough) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
        this.extraBranchSteps = extraBranchSteps;
        this.placeBranchPerLogProbability = placeBranchPerLogProbability;
        this.extraBranchLength = extraBranchLength;
        this.canGrowThrough = canGrowThrough;
        this.stumpChance = new WeightedListInt(new SimpleWeightedRandomList.Builder<IntProvider>()
                .add(ConstantInt.of(0), 3)
                .add(ConstantInt.of(1), 5)
                .add(ConstantInt.of(2), 7)
                .add(ConstantInt.of(3), 8)
                .add(ConstantInt.of(4), 4).build());
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        List<Direction> directions = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        
        for (int i = 0; i < pFreeTreeHeight; i++) {
            int j = pPos.getY() + i;

            if(i == 0) {
                var intchance = this.stumpChance.sample(pRandom);
                for(int stump = 0; stump < intchance; stump++) {
                    Collections.shuffle(directions);
                    Direction stumpDir = directions.getFirst();
                    this.placeLog(pLevel, pBlockSetter, pRandom, pPos.relative(stumpDir), pConfig);
                    directions.removeFirst();
                }
            }
            
            if (this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(pPos.getX(), j, pPos.getZ()), pConfig)
                    && i < pFreeTreeHeight - 1
                    && pRandom.nextFloat() < this.placeBranchPerLogProbability) {
                Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
                int k = this.extraBranchLength.sample(pRandom);
                int l = Math.max(0, k - this.extraBranchLength.sample(pRandom) - 1);
                int i1 = this.extraBranchSteps.sample(pRandom);
                this.placeBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pConfig, list, blockpos$mutableblockpos, j, direction, l, i1);
            }

            if (i == pFreeTreeHeight - 1) {
                list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos.set(pPos.getX(), j + 1, pPos.getZ()), 0, false));
            }
        }

        return list;
    }

    private void placeBranch(
            LevelSimulatedReader pLevel,
            BiConsumer<BlockPos, BlockState> pBlockSetter,
            RandomSource pRandom,
            int pFreeTreeHeight,
            TreeConfiguration pTreeConfig,
            List<FoliagePlacer.FoliageAttachment> pFoliageAttachments,
            BlockPos.MutableBlockPos pPos,
            int pY,
            Direction pDirection,
            int pExtraBranchLength,
            int pExtraBranchSteps
    ) {
        int i = pY + pExtraBranchLength;
        int j = pPos.getX();
        int k = pPos.getZ();
        int l = pExtraBranchLength;

        while (l < pFreeTreeHeight && pExtraBranchSteps > 0) {
            if (l >= 1) {
                int i1 = pY + l;
                j += pDirection.getStepX();
                k += pDirection.getStepZ();
                i = i1;
                if (this.placeLog(pLevel, pBlockSetter, pRandom, pPos.set(j, i1, k), pTreeConfig)) {
                    i = i1 + 1;
                }

                pFoliageAttachments.add(new FoliagePlacer.FoliageAttachment(pPos.immutable(), 0, false));
            }

            l++;
            pExtraBranchSteps--;
        }

        if (i - pY > 1) {
            BlockPos blockpos = new BlockPos(j, i, k);
            pFoliageAttachments.add(new FoliagePlacer.FoliageAttachment(blockpos, 0, false));
            pFoliageAttachments.add(new FoliagePlacer.FoliageAttachment(blockpos.below(2), 0, false));
        }
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.VIVICUS_TRUNK_PLACER.get();
    }

    @Override
    protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return super.validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, p_226232_ -> p_226232_.is(this.canGrowThrough));
    }
}
