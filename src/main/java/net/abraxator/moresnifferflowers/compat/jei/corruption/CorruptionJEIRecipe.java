package net.abraxator.moresnifferflowers.compat.jei.corruption;

import com.google.common.collect.Streams;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public record CorruptionJEIRecipe(List<ItemStack> source, ItemStack corrupted) {
    public static List<CorruptionJEIRecipe> createRecipes() {
        List<CorruptionJEIRecipe> recipes = new ArrayList<>();
        
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.DYESPRIA_PLANT.get().asItem().getDefaultInstance()), ModBlocks.DYESCRAPIA_PLANT.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.DAWNBERRY_VINE.get().asItem().getDefaultInstance()), ModBlocks.GLOOMBERRY_VINE.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.BONMEELIA.get().asItem().getDefaultInstance()), ModBlocks.BONWILTIA.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.BONDRIPIA.get().asItem().getDefaultInstance()), ModBlocks.ACIDRIPIA.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.AMBUSH_BOTTOM.get().asItem().getDefaultInstance()), ModBlocks.GARBUSH_BOTTOM.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(ModBlocks.AMBUSH_TOP.get().asItem().getDefaultInstance()), ModBlocks.GARBUSH_TOP.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(Streams.stream(BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.LEAVES)).map(itemHolder -> itemHolder.get().getDefaultInstance()).toList(), ItemStack.EMPTY));
        recipes.add(new CorruptionJEIRecipe(Streams.stream(BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.LOGS)).map(itemHolder -> itemHolder.get().getDefaultInstance()).toList(), ModBlocks.DECAYED_LOG.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(Blocks.DEEPSLATE.asItem().getDefaultInstance()), Blocks.BLACKSTONE.asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(Blocks.DIRT.asItem().getDefaultInstance()), Blocks.COARSE_DIRT.asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(Blocks.GRASS_BLOCK.asItem().getDefaultInstance()), ModBlocks.CORRUPTED_GRASS_BLOCK.get().asItem().getDefaultInstance()));
        recipes.add(new CorruptionJEIRecipe(List.of(Blocks.STONE.asItem().getDefaultInstance()), Blocks.NETHERRACK.asItem().getDefaultInstance()));
        
        return recipes;
    }
}
