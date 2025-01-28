package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CorruptedGiantTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<CorruptedGiantTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_70161_ -> trunkPlacerParts(p_70161_).apply(p_70161_, CorruptedGiantTrunkPlacer::new)
    );

    public CorruptedGiantTrunkPlacer(int pBaseHeight, int pHeightRandA, int pBranchCount) {
        super(pBaseHeight, pHeightRandA, pBranchCount);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.CORRUPTED_GIANT_TRUNK_PLACER.get();
    }


    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
        List<FoliagePlacer.FoliageAttachment> ret = new ArrayList<>();
        BlockPos.MutableBlockPos mainTrunk = pPos.mutable();
        BlockPos.MutableBlockPos tempTrunk = pPos.mutable();
        int trunkRadius = 2;
        int trunkHeight = 3;
        int treeHeight = pRandom.nextIntBetweenInclusive(0, heightRandA) + pFreeTreeHeight;
        int branchRnd = pRandom.nextIntBetweenInclusive(0, 3);

        for (int d = 0; d < 12; d++){
            addDirt(mainTrunk.immutable(), ret, pBlockSetter, pLevel, pConfig, pRandom, d);
        }
        for(int i = 0; i < treeHeight; i++) {

            if (i == 0) {
                tempTrunk.set(mainTrunk);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk, pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.NORTH), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.EAST), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.SOUTH), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.SOUTH), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.WEST), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.WEST), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.NORTH), pConfig);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk.move(Direction.NORTH), pConfig);

                for(int trunkOrder = 0; trunkOrder < (trunkRadius*2)*4; trunkOrder++)
                    fattenTrunk(pLevel, pBlockSetter, pRandom, pPos, pConfig, trunkOrder, ret, trunkHeight, trunkRadius);
                for (int u = 0; u < trunkHeight*2; u++){
                    mainTrunk.move(Direction.UP);
                }

            }
            for(int u = 0 ; u < 4 ; u++){
                tempTrunk.set(mainTrunk);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk, pConfig);
                int z = (u == 0) ? 1 : (u == 1) ? -1 : 0;
                int x = (u == 2) ? 1 : (u == 3) ? -1 : 0;
                tempTrunk.move(x,0,z);
                this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk, pConfig);

                if (pRandom.nextFloat() < 0.3F && i < (treeHeight-4) ){
                    tempTrunk.set(mainTrunk);
                    Direction randDir = Direction.getRandom(pRandom);
                    if (randDir.getAxis() != Direction.Axis.Y) {
                        tempTrunk.move(randDir);
                        tempTrunk.move(randDir.getCounterClockWise());
                        for (int p = 0; p < pRandom.nextIntBetweenInclusive(2, 4); p++) {
                            this.placeLog(pLevel, pBlockSetter, pRandom, tempTrunk, pConfig);
                            tempTrunk.move(Direction.UP);
                        }
                    }
                }
            }
                if (i % 4 == 2 && i < treeHeight-5) {
                    int branchOrder = branchRnd + Mth.floor((float)i / 4);
                    if (pRandom.nextFloat() < 0.3F && branchOrder > 1) branchOrder = branchOrder - 2 ;
                    addSmallBranch(mainTrunk.immutable(), ret, pBlockSetter, pLevel, pConfig, pRandom, branchOrder);
                }
                if (i == treeHeight-1)
                    for (int c = 0; c < heightRandB; c++){
                        addTopBranch(mainTrunk.immutable(), ret, pBlockSetter, pLevel, pConfig, pRandom, c);
                    }


            mainTrunk.move(Direction.UP);
        }
        return ret;
    }

    private void fattenTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, int trunkOrder, List<FoliagePlacer.FoliageAttachment> ret, int trunkRadius, int trunkHeight) {
        BlockPos.MutableBlockPos pos = pPos.mutable();
        boolean hasDoor = pRandom.nextFloat() < 0.3F && trunkOrder % 4 == 1;
        final Vec3i trunkOffset =
                switch (Mth.floor((float) trunkOrder / 4)) {
                    case 0 -> new Vec3i((trunkOrder % 4)-1,0,1);
                    case 1 -> new Vec3i(-1,0,((trunkOrder % 4)-1));
                    case 2 -> new Vec3i(-((trunkOrder % 4)-1),0,-1);
                    case 3 -> new Vec3i(1,0,-((trunkOrder % 4)-1));
                    default -> new Vec3i(0,0,0);
        };
        Direction trunkDir =
                switch (Mth.floor((float) trunkOrder / 4)) {
                    case 0 -> Direction.NORTH;
                    case 1 -> Direction.EAST;
                    case 2 -> Direction.SOUTH;
                    case 3 -> Direction.WEST;
                    default -> Direction.NORTH;
        };

        for (int i = 0; i < trunkRadius+1; i++){
            pos.move(trunkDir);
        }

        pos.move(trunkOffset);

        if(trunkOrder % 4 == 3){
            pos.move(trunkDir.getOpposite());
        }

        for (int x = 0; (trunkOrder % 4 == 1) ? x <= trunkHeight+1 : x <= trunkHeight ; x++){
            if (hasDoor && x < 2){
                pos.move(Direction.UP);
            }
            else {
                this.placeLog(pLevel, pBlockSetter, pRandom, pos, pConfig);
                pos.move(Direction.UP);
            }
        }
        if (trunkOrder % 4 != 3) {
            for (int y = 0; y < 3; y++) {
                pos.move(trunkDir.getOpposite());
                this.placeLog(pLevel, pBlockSetter, pRandom, pos, pConfig);
                if (y == 0) {
                    pos.move(Direction.UP);
                    this.placeLog(pLevel, pBlockSetter, pRandom, pos, pConfig);
                }
                pos.move(Direction.UP);
            }
        }
    }

    private void addSmallBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int branchOrder) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = random.nextIntBetweenInclusive(7, 9);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder % 4 == 0) ? 1 : (branchOrder % 4 == 2) ? -1 : 0;
        int v3 = (branchOrder % 4 == 1) ? 1 : (branchOrder % 4 == 3) ? -1 : 0;

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
           if (x > 1) ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

           if(x == branchLength - 1) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, -1, 0), config);
           //     ret.add(new FoliagePlacer.FoliageAttachment(pos.below(1), 0, false));
            }
        }

    }
    private void addTopBranch(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int branchOrder) {
        Direction direction = computeBranchDir(random);
        BlockPos.MutableBlockPos pos = blockPos.relative(direction).mutable();
        BlockPos.MutableBlockPos defaultPos = blockPos.relative(direction).mutable();
        int branchLength = (branchOrder < 4) ? random.nextIntBetweenInclusive(10, 12) : random.nextIntBetweenInclusive(7, 9);
        // int branchLength = 6;
        int branchDir = (int)(360f/heightRandB)*branchOrder;
        int v1 = (branchOrder % 4 == 0) ? 1 : (branchOrder % 4 == 2) ? -1 : 0;
        int v3 = (branchOrder % 4 == 1) ? 1 : (branchOrder % 4 == 3) ? -1 : 0;
        int v4 = (branchOrder % 2 == 0) ? 1 : -1 ;

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
            if (branchOrder > 3)
                this.placeLog(level, pBlockSetter, random, pos.move(v4*v3, 0, v4*v1), config);
             ret.add(new FoliagePlacer.FoliageAttachment(pos.above(), 0, false));

            if(x == branchLength - 1) {
                this.placeLog(level, pBlockSetter, random, pos.move(0, -1, 0), config);
                ret.add(new FoliagePlacer.FoliageAttachment(pos, 0, false));
            }
        }

    }
    private void addDirt(BlockPos blockPos, List<FoliagePlacer.FoliageAttachment> ret, BiConsumer<BlockPos, BlockState> pBlockSetter, LevelSimulatedReader level, TreeConfiguration config, RandomSource random, int dirtOrder) {
        int dirtLength = 5;
        BlockPos.MutableBlockPos pos = blockPos.below().mutable();

        int v1 = (dirtOrder % 4 == 0) ? 1 : (dirtOrder % 4 == 2) ? -1 : 0;
        int v3 = (dirtOrder % 4 == 1) ? 1 : (dirtOrder % 4 == 3) ? -1 : 0;
        int v4 = (dirtOrder % 2 == 0) ? 1 : -1 ;

         setDirtAt(level, pBlockSetter, random, pos, config);

        for(int x = 0; x < dirtLength; x++) {
            setDirtAt(level, pBlockSetter, random, pos.move(v1, 0, v3), config);
            if ( x == 0 && dirtOrder < 4) setDirtAt(level, pBlockSetter, random, pos.move(v1, 0, v3), config);
            if (dirtOrder > 3 && dirtOrder < 8) {
                setDirtAt(level, pBlockSetter, random, pos.move(v4 * v3, 0, v4 * v1), config);
            }
            if (dirtOrder > 7) {
                setDirtAt(level, pBlockSetter, random, pos.move(-v4 * v3, 0, -v4 * v1), config);
            }
        }
    }

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