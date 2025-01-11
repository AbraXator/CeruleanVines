package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.worldgen.configurations.ModConfiguredFeatures;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTreeGrower;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower CORRUPTED_TREE = new TreeGrower("corrupted_tree",
            Optional.of(ModConfiguredFeatures.GIANT_CORRUPTED_TREE),
            Optional.of(ModConfiguredFeatures.CORRUPTED_TREE),
            Optional.empty());

    public static final VivicusTreeGrower VIVICUS_TREE = new VivicusTreeGrower("vivicus_tree",
            ModConfiguredFeatures.CURED_VIVICUS_TREE,
            ModConfiguredFeatures.CORRUPTED_VIVICUS_TREE);
}
