package net.abraxator.moresnifferflowers.compat.jei.corruption;

import com.mojang.datafixers.util.Pair;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CorruptionJEIRecipe(ItemStack source, ItemStack corrupted, int chance) {
    public static List<CorruptionJEIRecipe> createRecipes() {
        List<CorruptionJEIRecipe> recipes = new ArrayList<>();
        
        /*for(Map.Entry<Block, Block> entry : CorruptionRecipe.HARDCODED_BLOCK.entrySet()) {
            if(entry.getKey() == ModBlocks.AMBUSH_BOTTOM.get()) {
                recipes.add(new CorruptionJEIRecipe(ModItems.AMBUSH_SEEDS.get().getDefaultInstance(), ModItems.GARBUSH_SEEDS.get().getDefaultInstance(), 100));
            } else if(entry.getKey() == ModBlocks.AMBUSH_TOP.get()) {
                continue;
            }
            
            recipes.add(new CorruptionJEIRecipe(entry.getKey().asItem().getDefaultInstance(), entry.getValue().asItem().getDefaultInstance(), 100));
        }*/
        
        return recipes;
    }
}
