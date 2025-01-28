package net.abraxator.moresnifferflowers.worldgen.feature;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.Set;
import java.util.function.BiConsumer;

public class VivicusTreeFeature extends TreeFeature {
    public VivicusTreeFeature(Codec<TreeConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> pContext) {
        final WorldGenLevel worldgenlevel = pContext.level();
        RandomSource randomsource = pContext.random();
        BlockPos blockpos = pContext.origin();
        BlockState blockState = pContext.level().getBlockState(blockpos);
        DyeColor saplingColor = blockState.hasProperty(ModStateProperties.COLOR) ? blockState.getValue(ModStateProperties.COLOR) : DyeColor.WHITE;
        TreeConfiguration treeconfiguration = pContext.config();
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        final Set<BlockPos> set2 = Sets.newHashSet();
        Set<BlockPos> set3 = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
            set.add(p_160555_.immutable());
            worldgenlevel.setBlock(p_160555_, p_160556_, 19);
        };
        BiConsumer<BlockPos, BlockState> trunkPlacer = (p_160548_, p_160549_) -> {
            set1.add(p_160548_.immutable());
            if(p_160549_.hasProperty(ModStateProperties.COLOR)) {
                worldgenlevel.setBlock(p_160548_, p_160549_.setValue(ModStateProperties.COLOR, saplingColor), 2);
            }
        };
        FoliagePlacer.FoliageSetter foliageplacer$foliagesetter = new FoliagePlacer.FoliageSetter() {
            @Override
            public void set(BlockPos p_272825_, BlockState p_273311_) {
                set2.add(p_272825_.immutable());
                if(p_273311_.hasProperty(ModStateProperties.COLOR)) {
                    worldgenlevel.setBlock(p_272825_, p_273311_.setValue(ModStateProperties.COLOR, saplingColor), 2);
                }
            }

            @Override
            public boolean isSet(BlockPos p_272999_) {
                return set2.contains(p_272999_);
            }
        };
        BiConsumer<BlockPos, BlockState> biconsumer2 = (p_160543_, p_160544_) -> {
            set3.add(p_160543_.immutable());
            if(p_160544_.hasProperty(ModStateProperties.COLOR)) {
                worldgenlevel.setBlock(p_160543_, p_160544_.setValue(ModStateProperties.COLOR, saplingColor), 2);
            }
        };
        boolean flag = this.doPlace(worldgenlevel, randomsource, blockpos, biconsumer, trunkPlacer, foliageplacer$foliagesetter, treeconfiguration);
        if (flag && (!set1.isEmpty() || !set2.isEmpty())) {
            if (!treeconfiguration.decorators.isEmpty()) {
                TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biconsumer2, randomsource, set1, set2, set);
                treeconfiguration.decorators.forEach(p_225282_ -> p_225282_.place(treedecorator$context));
            }

            return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map(p_225270_ -> {
                DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, p_225270_, set1, set3, set);
                StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, p_225270_.minX(), p_225270_.minY(), p_225270_.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }
}
