package net.abraxator.moresnifferflowers.worldgen.feature;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = 
            DeferredRegister.create(BuiltInRegistries.FEATURE, MoreSnifferFlowers.MOD_ID);
    
    public static final DeferredHolder<Feature<?>, VivicusTreeFeature> VIVICUS_TREE = FEATURES.register("vivicus_tree", () -> new VivicusTreeFeature(TreeConfiguration.CODEC));
}
