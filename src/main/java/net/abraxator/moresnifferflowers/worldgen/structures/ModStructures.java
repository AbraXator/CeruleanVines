package net.abraxator.moresnifferflowers.worldgen.structures;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModStructures {
    public static final ResourceKey<Structure> SWAMP_SNIFFER_TEMPLE = ResourceKey.create(Registries.STRUCTURE, MoreSnifferFlowers.loc("swamp_sniffer_temple"));
    
    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
        context.register(SWAMP_SNIFFER_TEMPLE, new SwampSnifferTempleStructure(new Structure.StructureSettings(holdergetter.getOrThrow(ModTags.ModBiomeTags.HAS_SWAMP_SNIFFER_TEMPLE))));
    }
}
