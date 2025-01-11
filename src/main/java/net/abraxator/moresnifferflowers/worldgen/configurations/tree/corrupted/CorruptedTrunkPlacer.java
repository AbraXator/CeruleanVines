package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.*;
import java.util.function.BiConsumer;

public class CorruptedTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<CorruptedTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_70161_ -> trunkPlacerParts(p_70161_).apply(p_70161_, CorruptedTrunkPlacer::new)
    );
    
    public CorruptedTrunkPlacer(int pBaseHeight, int pHeightRandA, int pBranchCount) {
        super(pBaseHeight, pHeightRandA, pBranchCount);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.CORRUPTED_TRUNK_PLACER.get();
    }


    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        int lastLogHeight = pFreeTreeHeight - ((int) Mth.randomBetween(pRandom, 2, 3));
        BlockPos.MutableBlockPos mainTrunk = pPos.mutable();
        Direction growthDir = null;
        int outerHeight = pRandom.nextInt(3);;
        int cornerHeight = outerHeight + (pRandom.nextInt(2) - 1);
        int innerHeight =  Math.min(outerHeight + pRandom.nextIntBetweenInclusive(3, 5), pFreeTreeHeight - 2);

        for(int i = 0; i < pFreeTreeHeight; i++) {
            
            this.placeLog(pLevel, pBlockSetter, pRandom, mainTrunk, pConfig);

            if (i == pFreeTreeHeight - 1){
                for(int branchOrder = 0; branchOrder < heightRandB; branchOrder++) {
                    addBranch(mainTrunk.immutable(), ret, pBlockSetter, branchOrder, pLevel, pConfig, pRandom, pFreeTreeHeight);
                }
            }

            if (i == 0) {
                fattenTrunk(pLevel, pBlockSetter, pRandom, pPos, pConfig, innerHeight, ret, cornerHeight, outerHeight);
            }

            mainTrunk.move(Direction.UP);
        }

       // ret.add(new FoliagePlacer.FoliageAttachment(mainTrunk.above(1), 0, false));
        return ret;
    }

    private void fattenTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, int innerHeight, List<FoliagePlacer.FoliageAttachment> ret, int cornerHeight, int outerHeight) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if(pRandom.nextDouble() <= 0.90D) {
                BlockPos blockPosInner = pPos.relative(direction);
                for(int j = 0; j < innerHeight; j++) {
                    this.placeLog(pLevel, pBlockSetter, pRandom, blockPosInner.above(j), pConfig);
                    //addBranch(blockPosInner, direction, branchesPos, branchesDir, pRandom, j);
                }

               /* if(pRandom.nextDouble() <= 0.8D) {
                    ret.add(new FoliagePlacer.FoliageAttachment(blockPosInner.above(innerHeight), 1, false));
                } */

                if(pRandom.nextDouble() <= 0.90D) {
                    BlockPos blockPosCorner = blockPosInner.relative(pRandom.nextDouble() > 0.5D ? direction.getClockWise() : direction.getCounterClockWise());
                    for(int j = 0; j < cornerHeight; j++) {
                        this.placeLog(pLevel, pBlockSetter, pRandom, blockPosCorner.above(j), pConfig);
                        //addBranch(blockPosInner, direction, branchesPos, branchesDir, pRandom, j);
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

    private void addBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, int branchOrder, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int pFreeTreeHeight) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = Math.min(random.nextIntBetweenInclusive(5, 7), pFreeTreeHeight);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder == 0) ? 1 : (branchOrder == 2) ? -1 : 0;
        int v3 = (branchOrder == 1) ? 1 : (branchOrder == 3) ? -1 : 0;

        for(int x = 0; x < branchLength; x++) {
            float branchHeightRand = (float)x / branchLength;

            if (branchHeightRand < random.nextFloat() & branchHeightRand > 0) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, 1, 0), config);
            }

            if (branchHeightRand > random.nextFloat()/1.5 & branchHeightRand > 0.5F) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, -1, 0), config);
            }

            if (x == 0) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, 0, 0), config);
            }

            this.placeLog(level, pBlockSetter, random, pos.move(v1, 0, v3), config);
            ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

            if(x == branchLength - 1) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, -1, 0), config);
           //     ret.add(new FoliagePlacer.FoliageAttachment(pos.below(1), 0, false));
            }
        }

    }


    //Old Branch maker//
 /*   private void addBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int pFreeTreeHeight) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        int branchHeight = 5+Math.min(random.nextIntBetweenInclusive(3, 6), pFreeTreeHeight);

        for (int i = 0; i < branchHeight; i++) {
            this.placeLog(level, pBlockSetter, random, pos, config);
            //pBlockSetter.accept(branch.pos, Blocks.ORANGE_WOOL.defaultBlockState());
            if(random.nextDouble() >= 0.66D) {
                this.placeLog(level, pBlockSetter, random, pos.move(direction), config);
                //pBlockSetter.accept(branch.pos.relative(branch.direction), Blocks.ORANGE_WOOL.defaultBlockState());
            }

            pos.move(Direction.UP);

            if(i == branchHeight - 1) {
                ret.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
            }
        }
    } */

    /*private void placeBranch(List<Branch> branches, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int height, int pFreeTreeHeight) {
        for (int i = 0; i < branches.size(); i++) {
            Branch branch = branches.get(i);
            
            if(branch.last) {
            } else {
                this.placeLog(level, pBlockSetter, random, branch.pos, config);
                //pBlockSetter.accept(branch.pos, Blocks.ORANGE_WOOL.defaultBlockState());
                if(random.nextBoolean()) {
                    this.placeLog(level, pBlockSetter, random, branch.pos.relative(branch.direction), config);
                    //pBlockSetter.accept(branch.pos.relative(branch.direction), Blocks.ORANGE_WOOL.defaultBlockState());
                }

                Direction direction = Branch.computeBranchDir(branch.direction, random, 0.3D);
                BlockPos blockPos = branch.pos.relative(direction).above();
                boolean last = branch.lenght > 3 && height >= pFreeTreeHeight - random.nextInt(1);
                
                branches.set(i, new Branch(blockPos, direction, 1, height == pFreeTreeHeight - 1));
            }
        }
    }*/

    private static Direction computeBranchDir(RandomSource random) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction clockAdjusted = random.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();
        return random.nextBoolean() ? direction : clockAdjusted;
    }
    
    @Override
    protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return true;
    }
}