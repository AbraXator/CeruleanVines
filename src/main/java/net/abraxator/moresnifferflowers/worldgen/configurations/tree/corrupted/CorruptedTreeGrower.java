package net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted;

import net.abraxator.moresnifferflowers.worldgen.configurations.ModConfiguredFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class CorruptedTreeGrower extends AbstractMegaTreeGrower {
    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_255637_, boolean p_255764_) {
        return ModConfiguredFeatures.CORRUPTED_TREE;
    }

    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource p_255928_) {
        return ModConfiguredFeatures.GIANT_CORRUPTED_TREE;
    }
}
