package net.abraxator.moresnifferflowers.recipes;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModRecipeSerializers;
import net.abraxator.moresnifferflowers.init.ModRecipeTypes;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

public record CorruptionRecipe(ResourceLocation id, String source, List<Entry> list) implements Recipe<Container> {
    public static final Map<Block, Block> HARDCODED_BLOCK = Util.make(Maps.newHashMap(), map -> {
        map.put(ModBlocks.DYESPRIA_PLANT.get(), ModBlocks.DYESCRAPIA_PLANT.get());
        map.put(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.GLOOMBERRY_VINE.get());
        map.put(ModBlocks.BONMEELIA.get(), ModBlocks.BONWILTIA.get());
        map.put(ModBlocks.BONDRIPIA.get(), ModBlocks.ACIDRIPIA.get());
        map.put(ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.GARBUSH_BOTTOM.get());
        map.put(ModBlocks.AMBUSH_TOP.get(), ModBlocks.GARBUSH_TOP.get());
    });
    public static final List<Block> BLACKLIST = List.of(ModBlocks.CORRUPTED_LOG.get(), ModBlocks.STRIPPED_CORRUPTED_LOG.get(), ModBlocks.CORRUPTED_LEAVES.get(), ModBlocks.CORRUPTED_LEAVES_BUSH.get());
    
    public boolean tagOrBlock() {
        return source.charAt(0) == '#';
    }
    
    public Optional<TagKey<Block>> inputTag() {
        return Optional.of(TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(source.replace("#", ""))));
    }
    
    public Optional<Block> inputBlock() {
        return Optional.of(ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(source)));
    }
    
    @Override
    public boolean matches(Container container, Level level) {
        var block = Block.byItem(container.getItem(0).getItem()).defaultBlockState();
        if(tagOrBlock()) {
            return block.is(inputTag().get());
        } else if(ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(source)) != null) {
            return block.is(inputBlock().get());
        } else throw (new ResourceLocationException(ResourceLocation.tryParse(source) + "must be a block or block tag"));
    }
    
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return getResultItem(registryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    public static Optional<Block> getCorruptedBlock(Block block, Level level) {
        Block hardcoded = HARDCODED_BLOCK.getOrDefault(block, Blocks.AIR);
        
        if(hardcoded != Blocks.AIR) {
            return Optional.of(hardcoded);
        }
        
        Optional<CorruptionRecipe> optionalRecipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.CORRUPTION.get(), new SimpleContainer(block.asItem().getDefaultInstance()), level);
        return optionalRecipe.map(corruptionRecipe -> corruptionRecipe.getResultBlock(level.random));

        //for (Map.Entry<String, Block> entry : HARDCODED_BLOCK.entrySet()) {
        //    var source = entry.getKey();
        //    if(source.contains("#")) {
        //        TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(source.replace("#", "")));
        //        if(block.defaultBlockState().is(tagKey)) return Optional.of(entry.getValue());
        //    } else {
        //        var block1 = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryBuild(source.split(":")[0], source.split(":")[1]));
        //        if(block1.defaultBlockState().is(block)) return Optional.of(entry.getValue());
        //    }
        //}
        //
        //return Optional.empty();
    }

    public static boolean canBeCorrupted(Block block, Level level) {
        if(block == null || BLACKLIST.contains(block)) {
            return false;
        }

        return getCorruptedBlock(block, level).isPresent();
    }
    
    public Block getResultBlock(RandomSource randomSource) {
        int totalWeight = list.stream().mapToInt(Entry::weight).sum();
        int randomValue = randomSource.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (Entry entry : list) {
            cumulativeWeight += entry.weight;
            if(randomValue < cumulativeWeight) {
                return entry.block;
            }
        }
        
        return Blocks.AIR;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultBlock(Minecraft.getInstance().level.getRandom()).asItem().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CORRUPTION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CORRUPTION.get();
    }
    
    public record Entry(Block block, int weight) {
        public static Entry fromJsonElement(JsonElement element) {
            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(element.getAsJsonObject(), "block")));
            int weight = GsonHelper.getAsInt(element.getAsJsonObject(), "weight");
            
            return new Entry(block, weight);
        }
    }
}
