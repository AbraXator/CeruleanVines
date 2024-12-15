package net.abraxator.moresnifferflowers.data.datamaps;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.jarjar.nio.pathfs.PathFileSystem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Corruptable(List<Pair<Block, Integer>> list) {
    public static final Codec<Pair<Block, Integer>> PAIR_CODEC = Codec.pair(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").codec(),
            Codec.INT.fieldOf("weight").codec()
    );
    public static final Codec<Corruptable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PAIR_CODEC.listOf().fieldOf("value").forGetter(Corruptable::list)
    ).apply(instance, Corruptable::new));
    public static final Map<Block, Block> HARDCODED_BLOCK = Util.make(Maps.newHashMap(), map -> {
        map.put(ModBlocks.DYESPRIA_PLANT.get(), ModBlocks.DYESCRAPIA_PLANT.get());
        map.put(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.GLOOMBERRY_VINE.get());
        map.put(ModBlocks.BONMEELIA.get(), ModBlocks.BONWILTIA.get());
        map.put(ModBlocks.BONDRIPIA.get(), ModBlocks.ACIDRIPIA.get());
        map.put(ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.GARBUSH_BOTTOM.get());
        map.put(ModBlocks.AMBUSH_TOP.get(), ModBlocks.GARBUSH_TOP.get());
    });
    
    public Corruptable(Block block) {
        this(List.of(Pair.of(block, 100)));
    }
    
    public static Optional<Block> getCorruptedBlock(Block block, RandomSource random) {
        Holder<Block> holder = block.builtInRegistryHolder();
        Corruptable corruptable = holder.getData(ModDataMaps.CORRUPTABLE);
        Block hardcodedBlock = HARDCODED_BLOCK.get(block);
        Optional<Block> ret = Optional.empty();
        
        if(hardcodedBlock != Blocks.AIR) {
            ret = Optional.of(hardcodedBlock);
        }
        
        if (corruptable != null) {
            List<Pair<Block, Integer>> corruptableList = corruptable.list();
            int totalWeight = corruptableList.stream().mapToInt(Pair::getSecond).sum();
            int randomValue = random.nextInt(totalWeight);
            int cumulativeWeight = 0;
            
            for (Pair<Block, Integer> pair : corruptableList) {
                cumulativeWeight += pair.getSecond();
                if(randomValue < cumulativeWeight) {
                    ret = Optional.of(pair.getFirst());
                }
            }
        }
        
        MoreSnifferFlowers.LOGGER.error("No block selected for corruption, probably missing entry");
        return ret;
    }
    
    public static boolean canBeCorrupted(Block block, RandomSource random) {
        if(block == null) {
            return false;
        }
        
        return getCorruptedBlock(block, random).isPresent();
    }
}
