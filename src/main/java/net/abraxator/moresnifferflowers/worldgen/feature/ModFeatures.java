package net.abraxator.moresnifferflowers.worldgen.feature;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = 
            DeferredRegister.create(ForgeRegistries.FEATURES, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<Feature<TreeConfiguration>> VIVICUS_TREE = FEATURES.register("vivicus_tree", () -> new VivicusTreeFeature(TreeConfiguration.CODEC));
}
