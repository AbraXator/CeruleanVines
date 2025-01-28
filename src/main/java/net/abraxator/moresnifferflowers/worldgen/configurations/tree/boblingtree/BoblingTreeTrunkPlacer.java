package net.abraxator.moresnifferflowers.worldgen.configurations.tree.boblingtree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTrunkPlacer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.openjdk.nashorn.internal.objects.NativeWeakMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BoblingTreeTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<BoblingTreeTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_338099_ -> trunkPlacerParts(p_338099_).apply(p_338099_, BoblingTreeTrunkPlacer::new)
    );
    
    public static final BlockStateProvider BLACK_STONE = new WeightedStateProvider(
            SimpleWeightedRandomList.<BlockState>builder()
                    .add(Blocks.BLACKSTONE.defaultBlockState(), 100)
                    .add(Blocks.GILDED_BLACKSTONE.defaultBlockState(), 60)
    );
    public static final BlockStateProvider DEEPSLATE = new WeightedStateProvider(
            SimpleWeightedRandomList.<BlockState>builder()
                    .add(Blocks.DEEPSLATE.defaultBlockState(), 100)
                    .add(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), 80)
                    .add(Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(), 50)
                    .add(Blocks.EMERALD_ORE.defaultBlockState(), 50)
    );
    public static final BlockStateProvider STONE = new WeightedStateProvider(
            SimpleWeightedRandomList.<BlockState>builder()
                    .add(Blocks.STONE.defaultBlockState(), 100)
                    .add(Blocks.COAL_ORE.defaultBlockState(), 80)
                    .add(Blocks.GOLD_ORE.defaultBlockState(), 50)
                    .add(Blocks.COPPER_ORE.defaultBlockState(), 50)
    );
    
    public BoblingTreeTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacerTypes.BOBLING_TREE_TRUNK.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, int freeHeight, BlockPos startPos, TreeConfiguration treeConfiguration) {
        List<Branch> branches = new ArrayList<>();
        
        for(int height = 0; height < freeHeight; ++height) {
            double chance = 0.6D; 
            List<Direction> directions = Direction.Plane.HORIZONTAL.shuffledCopy(randomSource);
                    
            do {
                chance = chance - 0.2D;
                Direction direction = directions.get(randomSource.nextInt(directions.size()));
                BlockPos pos = startPos.above(height);
                int h = (randomSource.nextInt(4) + 1) - (height / 2);
                Branch branch = new Branch(pos, direction, 0, height);
                branches.add(branch);
            } while (randomSource.nextDouble() < chance && !directions.isEmpty());
            
            for(int b = 0; b < branches.size(); ++b) {
                Branch branch = branches.get(b);
                BlockPos.MutableBlockPos mutableBlockPos = branch.blockPos.mutable();
                int h = branch.height;
                placeBlock(biConsumer, randomSource, mutableBlockPos.move(branch.direction), startPos);
                
                if(randomSource.nextDouble() < 0.66D) {
                    placeBlock(biConsumer, randomSource, mutableBlockPos.move(branch.direction), startPos);
                }
                if(branch.height < height) {
                    placeBlock(biConsumer, randomSource, mutableBlockPos.move(Direction.UP), startPos);
                    h++;
                }
                
                Branch newBranch = new Branch(mutableBlockPos.immutable(), branch.direction, h, branch.maxHeight);
                if(!newBranch.equals(branch)) {
                    branches.set(b, branch);
                }
            }

            placeBlock(biConsumer, randomSource, startPos.above(height), startPos);
        }
        
        return List.of();
    }

    protected void placeBlock(BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, BlockPos startPos) {
        pBlockSetter.accept(pPos, determineBlockVariation(startPos, pPos, pRandom));
    }
    
    public BlockState determineBlockVariation(BlockPos startPos, BlockPos blockPos, RandomSource random) {
        int dist = startPos.distManhattan(blockPos);
        if(dist > 5 && random.nextDouble() < 0.8D) {
            return BLACK_STONE.getState(random, blockPos);
        } 
        
        if(dist <= 5 && dist > 2  && random.nextDouble() < 0.8D) {
            return DEEPSLATE.getState(random, blockPos);
        }

        return STONE.getState(random, blockPos);
    }

    @Override
    protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return super.validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, blockState -> blockState.is(ModTags.ModBlockTags.VIVICUS_TREE_REPLACABLE));
    }

    public static record Branch(BlockPos blockPos, Direction direction, int height, int maxHeight) {
    }
}
