package net.abraxator.moresnifferflowers.data.datamaps;

import com.mojang.datafixers.util.Pair;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModDataMapsProvider extends DataMapProvider {
    public ModDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void gather() {
        var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        compostables.add(ModItems.DAWNBERRY_VINE_SEEDS, new Compostable(0.3F), false);
        compostables.add(ModItems.DAWNBERRY, new Compostable(0.3F), false);
        compostables.add(ModItems.AMBUSH_SEEDS, new Compostable(0.3F), false);
        compostables.add(ModItems.CAULORFLOWER_SEEDS, new Compostable(0.4F), false);
        compostables.add(ModItems.DYESPRIA_SEEDS, new Compostable(0.4F), false);
        compostables.add(ModItems.BONMEELIA_SEEDS, new Compostable(0.5F), false);
        compostables.add(ModItems.CROPRESSED_BEETROOT, new Compostable(1.0F), false);
        compostables.add(ModItems.CROPRESSED_NETHERWART, new Compostable(1.0F), false);
        compostables.add(ModItems.CROPRESSED_WHEAT, new Compostable(1.0F), false);
        compostables.add(ModItems.CROPRESSED_POTATO, new Compostable(1.0F), false);
        compostables.add(ModItems.CROPRESSED_CARROT, new Compostable(1.0F), false);
        compostables.add(ModBlocks.CORRUPTED_SAPLING.asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
        compostables.add(ModBlocks.VIVICUS_SAPLING.asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
        compostables.add(ModBlocks.CORRUPTED_LEAVES.asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
        compostables.add(ModBlocks.VIVICUS_LEAVES.asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
        
        var corruptables = this.builder(ModDataMaps.CORRUPTABLE);
        corruptables.add(Blocks.GRASS_BLOCK.builtInRegistryHolder(), new Corruptable(ModBlocks.CORRUPTED_GRASS_BLOCK.get()), false);
        corruptables.add(Blocks.DIRT.builtInRegistryHolder(), new Corruptable(Blocks.COARSE_DIRT), false);
        corruptables.add(Blocks.STONE.builtInRegistryHolder(), new Corruptable(Blocks.NETHERRACK), false);
        corruptables.add(Blocks.DEEPSLATE.builtInRegistryHolder(), new Corruptable(Blocks.BLACKSTONE), false);
        corruptables.add(BlockTags.LOGS, new Corruptable(ModBlocks.DECAYED_LOG.get()), false);
        corruptables.remove(ModBlocks.CORRUPTED_LOG);
        corruptables.remove(ModBlocks.CORRUPTED_WOOD);
        corruptables.remove(ModBlocks.STRIPPED_CORRUPTED_LOG);
        corruptables.remove(ModBlocks.STRIPPED_CORRUPTED_WOOD);
        corruptables.add(BlockTags.LEAVES, new Corruptable(Blocks.AIR), false);
        corruptables.remove(ModBlocks.CORRUPTED_LEAVES);
        corruptables.remove(ModBlocks.CORRUPTED_LEAVES_BUSH);
    }
}
