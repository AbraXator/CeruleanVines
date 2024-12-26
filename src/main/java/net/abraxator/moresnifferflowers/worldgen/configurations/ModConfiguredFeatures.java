package net.abraxator.moresnifferflowers.worldgen.configurations;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.boblingtree.BoblingTreeTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedSludgeDecorator;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.feature.ModFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MangrovePropaguleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORRUPTED_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, MoreSnifferFlowers.loc("corrupted_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CURED_VIVICUS_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, MoreSnifferFlowers.loc("cured_vivicus_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORRUPTED_VIVICUS_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, MoreSnifferFlowers.loc("corrupted_vivicus_tree"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BOBLING_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, MoreSnifferFlowers.loc("bobling_tree"));
    
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> blockHolderGetter = context.lookup(Registries.BLOCK);
        
        FeatureUtils.register(
                context, CORRUPTED_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                        new WeightedStateProvider(
                                SimpleWeightedRandomList.<BlockState>builder()
                                        .add(ModBlocks.CORRUPTED_LOG.get().defaultBlockState(), 10)
                                        .add(ModBlocks.STRIPPED_CORRUPTED_LOG.get().defaultBlockState(), 4)
                        ),
                        new CorruptedTrunkPlacer(6, 1, 1),
                        new WeightedStateProvider(
                                SimpleWeightedRandomList.<BlockState>builder()
                                        .add(ModBlocks.CORRUPTED_LEAVES.get().defaultBlockState(), 10)
                                        .add(ModBlocks.CORRUPTED_LEAVES_BUSH.get().defaultBlockState(), 2)
                        ),
                        new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                        //new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), UniformInt.of(2, 3), 20),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .decorators(
                                List.of(
                                        new AttachedToLeavesDecorator(
                                                0.2F,
                                                2,
                                                0,
                                                new RandomizedIntStateProvider(
                                                        BlockStateProvider.simple(
                                                                ModBlocks.CORRUPTED_SLUDGE.get().defaultBlockState()
                                                        ),
                                                        MangrovePropaguleBlock.AGE,
                                                        UniformInt.of(0, 4)
                                                ),
                                                6,
                                                List.of(Direction.DOWN)
                                        )
                                )
                        )
                        .ignoreVines()
                        .build());

        FeatureUtils.register(
                context, CURED_VIVICUS_TREE, ModFeatures.VIVICUS_TREE.get(), vivicusTree()
                        .ignoreVines()
                        .decorators(List.of(
                                new AttachedToLeavesDecorator(
                                        0.14F,
                                        1,
                                        0,
                                        BlockStateProvider.simple(ModBlocks.VIVICUS_LEAVES_SPROUT.get().defaultBlockState()
                                                .setValue(ModStateProperties.VIVICUS_TYPE, BoblingEntity.Type.CURED)),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        ))
                        .build());
        FeatureUtils.register(
                context, CORRUPTED_VIVICUS_TREE, ModFeatures.VIVICUS_TREE.get(), vivicusTree()
                        .ignoreVines()
                        .decorators(List.of(
                                new AttachedToLeavesDecorator(
                                        0.14F,
                                        1,
                                        0,
                                        BlockStateProvider.simple(ModBlocks.VIVICUS_LEAVES_SPROUT.get().defaultBlockState()
                                                .setValue(ModStateProperties.VIVICUS_TYPE, BoblingEntity.Type.CORRUPTED)),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        ))
                        .build());
        FeatureUtils.register(
                context, BOBLING_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                        SimpleStateProvider.simple(Blocks.STONE.defaultBlockState()),
                        new BoblingTreeTrunkPlacer(4, 2, 1),
                        SimpleStateProvider.simple(Blocks.AIR.defaultBlockState()),
                        //new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                        new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), UniformInt.of(2, 3), 20),
                        new TwoLayersFeatureSize(2, 0, 2)
                )
                        .ignoreVines()
                        .build());
    }
    
    private static TreeConfiguration.TreeConfigurationBuilder vivicusTree() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.VIVICUS_LOG.get()),
                new VivicusTrunkPlacer(8, 2, 2),
                //new VivicusTrunkPlacer(6, 3, 3, UniformInt.of(3, 5), 1F, UniformInt.of(0, 1), blockHolderGetter.getOrThrow(ModTags.ModBlockTags.VIVICUS_TREE_REPLACABLE)),
                BlockStateProvider.simple(ModBlocks.VIVICUS_LEAVES.get().defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 4),
                //new RandomSpreadFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(3), 90),
                //new CherryFoliagePlacer(UniformInt.of(3, 6), ConstantInt.ZERO, UniformInt.of(4, 5), 0.5F, 0.4F, 0.7F, 0.4F),
                //new RandomSpreadFoliagePlacer(UniformInt.of(3, 6), ConstantInt.ZERO, UniformInt.of(2, 4), 40),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        );
    }
}
