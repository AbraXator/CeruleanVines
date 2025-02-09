package net.abraxator.moresnifferflowers.data.recipe;

import net.abraxator.moresnifferflowers.data.recipe.builder.CropressingRecipeBuilder;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModCustomRecipeProvider extends RecipeProvider {

    public ModCustomRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    public static void createRecipes(Consumer<FinishedRecipe> recipeOutput) {
        createCropressing(recipeOutput, ModItems.CROPRESSED_CARROT.get(), Items.CARROT);
        createCropressing(recipeOutput, ModItems.CROPRESSED_POTATO.get(), Items.POTATO);
        createCropressing(recipeOutput, ModItems.CROPRESSED_NETHERWART.get(), Items.NETHER_WART);
        createCropressing(recipeOutput, ModItems.CROPRESSED_BEETROOT.get(), Items.BEETROOT);
        createCropressing(recipeOutput, ModItems.CROPRESSED_WHEAT.get(), Items.WHEAT);
    }

    public static void createCropressing(Consumer<FinishedRecipe> recipeOutput, ItemLike result, ItemLike crop) {
        new CropressingRecipeBuilder(result).requiresCrop(crop.asItem()).unlockedBy("has_cropressor", has(ModBlocks.CROPRESSOR_OUT.get())).save(recipeOutput, getItemName(result) + "_from_cropressing");
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        
    }
}
