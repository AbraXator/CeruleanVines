package net.abraxator.moresnifferflowers.data.recipe;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.recipes.RebrewedTippedArrowRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider {
    public ModRecipesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> future) {
        super(pOutput, future);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        trimSmithing(pRecipeOutput, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get())));

        trimCrafting(pRecipeOutput, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModTags.ModItemTags.AROMA_TRIM_TEMPLATE_INGREDIENT);
        trimCrafting(pRecipeOutput, ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_NETHERWART.get());
        trimCrafting(pRecipeOutput, ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_CARROT.get());
        trimCrafting(pRecipeOutput, ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_POTATO.get());
        trimCrafting(pRecipeOutput, ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_WHEAT.get());
        trimCrafting(pRecipeOutput, ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_BEETROOT.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXTRACTION_BOTTLE.get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.GLASS)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AMBUSH_BANNER_PATTERN.get())
                .requires(Items.PAPER)
                .requires(ModItems.AMBER_SHARD.get())
                .unlockedBy("has_amber_shard", has(ModItems.AMBER_SHARD.get()))
                .save(pRecipeOutput);

        twoByTwoPacker(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_BLOCK.get(), ModItems.AMBER_SHARD.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CROPRESSOR.get())
                .requires(ModItems.TUBE_PIECE.get())
                .requires(ModItems.SCRAP_PIECE.get())
                .requires(ModItems.ENGINE_PIECE.get())
                .requires(ModItems.PRESS_PIECE.get())
                .requires(ModItems.BELT_PIECE.get())
                .unlockedBy("has_cropressor_piece", has(ModTags.ModItemTags.CROPRESSOR_PIECES))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REBREWING_STAND.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern("BCB")
                .define('A', ModItems.CROPRESSED_NETHERWART.get())
                .define('B', ModItems.BROKEN_REBREWING_STAND.get())
                .define('C', ModItems.TUBE_PIECE.get())
                .unlockedBy("has_broken_rebrewing_stand", has(ModItems.BROKEN_REBREWING_STAND.get()))
                .save(pRecipeOutput);

        partsRecycling(pRecipeOutput, ModItems.BELT_PIECE.get(), Items.LEATHER, 8);
        partsRecycling(pRecipeOutput, ModItems.SCRAP_PIECE.get(), Items.COPPER_INGOT, 8);
        partsRecycling(pRecipeOutput, ModItems.ENGINE_PIECE.get(), Items.GOLD_INGOT, 8);
        partsRecycling(pRecipeOutput, ModItems.TUBE_PIECE.get(), Items.IRON_INGOT, 8);
        partsRecycling(pRecipeOutput, ModItems.PRESS_PIECE.get(), Items.NETHERITE_SCRAP, 1);
        partsRecycling(pRecipeOutput, ModItems.BROKEN_REBREWING_STAND.get(), ModItems.CROPRESSED_NETHERWART.get(), 4);

        woodFromLogs(pRecipeOutput, ModBlocks.CORRUPTED_WOOD, ModBlocks.CORRUPTED_LOG);
        woodFromLogs(pRecipeOutput, ModBlocks.STRIPPED_CORRUPTED_WOOD, ModBlocks.STRIPPED_CORRUPTED_LOG);
        stairBuilder(ModBlocks.CORRUPTED_STAIRS, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CORRUPTED_SLAB, ModBlocks.CORRUPTED_PLANKS);
        fenceBuilder(ModBlocks.CORRUPTED_FENCE, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);
        fenceGateBuilder(ModBlocks.CORRUPTED_FENCE_GATE, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);
        doorBuilder(ModBlocks.CORRUPTED_DOOR, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);
        trapdoorBuilder(ModBlocks.CORRUPTED_TRAPDOOR, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);
        pressurePlate(pRecipeOutput, ModBlocks.CORRUPTED_PRESSURE_PLATE, ModBlocks.CORRUPTED_PLANKS);
        buttonBuilder(ModBlocks.CORRUPTED_BUTTON, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(pRecipeOutput);

        woodFromLogs(pRecipeOutput, ModBlocks.VIVICUS_WOOD, ModBlocks.VIVICUS_LOG);
        woodFromLogs(pRecipeOutput, ModBlocks.STRIPPED_VIVICUS_WOOD, ModBlocks.STRIPPED_VIVICUS_LOG);
        stairBuilder(ModBlocks.VIVICUS_STAIRS, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VIVICUS_SLAB, ModBlocks.VIVICUS_PLANKS);
        fenceBuilder(ModBlocks.VIVICUS_FENCE, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        fenceGateBuilder(ModBlocks.VIVICUS_FENCE_GATE, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        doorBuilder(ModBlocks.VIVICUS_DOOR, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        trapdoorBuilder(ModBlocks.VIVICUS_TRAPDOOR, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        pressurePlate(pRecipeOutput, ModBlocks.VIVICUS_PRESSURE_PLATE, ModBlocks.VIVICUS_PLANKS);
        buttonBuilder(ModBlocks.VIVICUS_BUTTON, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(pRecipeOutput);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VIVICUS_ANTIDOTE, 1)
                        .pattern(" AB")
                        .pattern("ACA")
                        .pattern("DA ")
                        .define('A', Tags.Items.GLASS_BLOCKS_COLORLESS)
                        .define('B', ModItems.CORRUPTED_SLIME_BALL)
                        .define('C', ModItems.BOBLING_CORE)
                        .define('D', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_corrupted_slime_ball", has(ModItems.CORRUPTED_SLIME_BALL))
                        .save(pRecipeOutput);

        SpecialRecipeBuilder.special(RebrewedTippedArrowRecipe::new).save(pRecipeOutput, "rebrewed_tipped_arrow");
        
        ModCustomRecipeProvider.createRecipes(pRecipeOutput);
    }

    private void trimCrafting(RecipeOutput pRecipeOutput, ItemLike trim, TagKey<Item> ingredient) {
        trimCrafting(pRecipeOutput, trim, Ingredient.of(ingredient));
    }

    private void trimCrafting(RecipeOutput pRecipeOutput, ItemLike trim, ItemLike ingredient) {
        trimCrafting(pRecipeOutput, trim, Ingredient.of(ingredient));
    }

    private void trimCrafting(RecipeOutput pRecipeOutput, ItemLike trim, Ingredient ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, trim, 2)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("AAA")
                .define('A', Items.DIAMOND)
                .define('B', trim)
                .define('C', ingredient)
                .unlockedBy("has_" + getItemName(trim) + "_trim_template", has(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get()))
                .save(pRecipeOutput, MoreSnifferFlowers.loc(getItemName(trim) + "_from_trim_crafting"));
    }

    private void partsRecycling(RecipeOutput pRecipeOutput, Item part, Item result, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
                .requires(part)
                .unlockedBy("has_" + getItemName(part), has(ModItems.BELT_PIECE.get()))
                .save(pRecipeOutput, MoreSnifferFlowers.loc(getItemName(result) + "_from_part_recycling"));
    }
}
