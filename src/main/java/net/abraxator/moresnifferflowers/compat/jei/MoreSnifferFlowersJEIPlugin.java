package net.abraxator.moresnifferflowers.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionCategory;
import net.abraxator.moresnifferflowers.compat.jei.corruption.CorruptionJEIRecipe;
import net.abraxator.moresnifferflowers.compat.jei.cropressing.CropressingRecipeCategory;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.JeiRebrewingRecipe;
import net.abraxator.moresnifferflowers.compat.jei.rebrewing.RebrewingCategory;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModRecipeTypes;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@JeiPlugin
public class MoreSnifferFlowersJEIPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation("jei", MoreSnifferFlowers.MOD_ID);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.CROPRESSOR.get().getDefaultInstance(), CropressingRecipeCategory.CROPRESSING);
        registration.addRecipeCatalyst(ModItems.REBREWING_STAND.get().getDefaultInstance(), RebrewingCategory.REBREWING);
        registration.addRecipeCatalyst(ModItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance(), CorruptionCategory.CORRUPTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(RebrewingStandScreen.class, 123, 17, 9, 28, RebrewingCategory.REBREWING);
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CropressingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RebrewingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CorruptionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<CropressingRecipe> cropressingRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.CROPRESSING.get()));
        
        registration.addRecipes(CropressingRecipeCategory.CROPRESSING, cropressingRecipes);
        registration.addRecipes(RebrewingCategory.REBREWING, JeiRebrewingRecipe.createRecipes());
        registration.addRecipes(CorruptionCategory.CORRUPTING, CorruptionJEIRecipe.createRecipes());
    }
}
