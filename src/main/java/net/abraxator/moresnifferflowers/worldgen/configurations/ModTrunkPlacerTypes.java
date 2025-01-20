package net.abraxator.moresnifferflowers.worldgen.configurations;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.boblingtree.BoblingTreeTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedGiantTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedTrunkPlacer;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus.VivicusTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTrunkPlacerTypes {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNKS = 
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<TrunkPlacerType<CorruptedTrunkPlacer>> CORRUPTED_TRUNK_PLACER = TRUNKS.register("corrupted_trunk_placer", () -> new TrunkPlacerType<>(CorruptedTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<CorruptedGiantTrunkPlacer>> CORRUPTED_GIANT_TRUNK_PLACER = TRUNKS.register("giant_corrupted_trunk_placer", () -> new TrunkPlacerType<>(CorruptedGiantTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<VivicusTrunkPlacer>> VIVICUS_TRUNK_PLACER = TRUNKS.register("vivicus_trunk_placer", () -> new TrunkPlacerType<>(VivicusTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<BoblingTreeTrunkPlacer>> BOBLING_TREE_TRUNK = TRUNKS.register("bobling_tree_trunk", () -> new TrunkPlacerType<>(BoblingTreeTrunkPlacer.CODEC));
}
