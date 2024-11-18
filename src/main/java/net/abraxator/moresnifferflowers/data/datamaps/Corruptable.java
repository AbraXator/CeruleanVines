package net.abraxator.moresnifferflowers.data.datamaps;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

public record Corruptable(List<Pair<Block, Integer>> list) {
    public static final Codec<Pair<Block, Integer>> PAIR_CODEC = Codec.pair(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").codec(),
            Codec.INT.fieldOf("weight").codec()
    );
    public static final Codec<Corruptable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PAIR_CODEC.listOf().fieldOf("value").forGetter(Corruptable::list)
    ).apply(instance, Corruptable::new));
    
    public Corruptable(Block block) {
        this(List.of(Pair.of(block, 100)));
    }
    
    public static Optional<Block> getCorruptedBlock(Block block, RandomSource random) {
        Holder<Block> holder = block.builtInRegistryHolder();
        Corruptable corruptable = holder.getData(ModDataMaps.CORRUPTABLE);
        Block hardcodedBlock = hardcodedBlock(block);
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
    
    private static Block hardcodedBlock(Block block) {
        if(block == ModBlocks.DYESPRIA_PLANT.get()) {
            return ModBlocks.DYESCRAPIA_PLANT.get();
        } else if(block == ModBlocks.DAWNBERRY_VINE.get()) {
            return ModBlocks.GLOOMBERRY_VINE.get();
        } else if(block == ModBlocks.BONMEELIA.get()) {
            return ModBlocks.BONWILTIA.get();
        } else if(block == ModBlocks.BONDRIPIA.get()) {
            return ModBlocks.BONDRIPIA.get();
        } else if(block == ModBlocks.AMBUSH_BOTTOM.get() || block == ModBlocks.AMBUSH_TOP.get()) {
            return ModBlocks.GARBUSH_BOTTOM.get();
        }
        
        return Blocks.AIR;
    }
    
    public static boolean canBeCorrupted(Block block, RandomSource random) {
        if(block == null) {
            return false;
        }
        
        return getCorruptedBlock(block, random).isPresent();
    }
}
