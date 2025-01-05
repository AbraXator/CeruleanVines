package net.abraxator.moresnifferflowers.data.recipe;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModRecipeSerializers;
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
import net.minecraftforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipesProvider extends RecipeProvider {
    public ModRecipesProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pRecipeOutput) {
        trimSmithing(pRecipeOutput, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        trimSmithing(pRecipeOutput, ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), MoreSnifferFlowers.loc(getItemName(ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get())));
        
        trimCrafting(pRecipeOutput, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.AMBER_SHARD.get());
        trimCrafting(pRecipeOutput, ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.GARNET_SHARD.get());
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


        threeByThreePacker(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_BLOCK.get(), ModItems.AMBER_SHARD.get());
        twoByTwoPacker(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC.get(), ModItems.AMBER_SHARD.get());
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC_SLAB.get(), ModBlocks.AMBER_MOSAIC.get());
        stairBuilder(ModBlocks.AMBER_MOSAIC_STAIRS.get(), Ingredient.of(ModBlocks.AMBER_MOSAIC.get()))
                .unlockedBy("has_amber_mosaic", has(ModBlocks.AMBER_MOSAIC.get()))
                .save(pRecipeOutput);
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC_WALL.get(), ModBlocks.AMBER_MOSAIC.get());
        chiseled(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_AMBER.get(), ModBlocks.AMBER_MOSAIC_SLAB.get());
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_AMBER_SLAB.get(), ModBlocks.CHISELED_AMBER.get());
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.AMBER_MOSAIC.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRACKED_AMBER.get().asItem(), 0.1F, 200)
                .unlockedBy("has_amber_mosaic", has(ModBlocks.AMBER_MOSAIC.get()))
                .save(pRecipeOutput);

        threeByThreePacker(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_BLOCK.get(), ModItems.GARNET_SHARD.get());
        twoByTwoPacker(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC.get(), ModItems.GARNET_SHARD.get());
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC_SLAB.get(), ModBlocks.GARNET_MOSAIC.get());
        stairBuilder(ModBlocks.GARNET_MOSAIC_STAIRS.get(), Ingredient.of(ModBlocks.GARNET_MOSAIC.get()))
                .unlockedBy("has_garnet_mosaic", has(ModBlocks.GARNET_MOSAIC.get()))
                .save(pRecipeOutput);
        wall(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC_WALL.get(), ModBlocks.GARNET_MOSAIC.get());
        chiseled(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GARNET.get(), ModBlocks.GARNET_MOSAIC_SLAB.get());
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GARNET_SLAB.get(), ModBlocks.CHISELED_GARNET.get());
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.GARNET_MOSAIC.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRACKED_GARNET.get().asItem(), 0.1F, 200)
                .unlockedBy("has_garnet_mosaic", has(ModBlocks.GARNET_MOSAIC.get()))
                .save(pRecipeOutput);


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

        woodFromLogs(pRecipeOutput, ModBlocks.CORRUPTED_WOOD.get(), ModBlocks.CORRUPTED_LOG.get());
        woodFromLogs(pRecipeOutput, ModBlocks.STRIPPED_CORRUPTED_WOOD.get(), ModBlocks.STRIPPED_CORRUPTED_LOG.get());
        stairBuilder(ModBlocks.CORRUPTED_STAIRS.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CORRUPTED_SLAB.get(), ModBlocks.CORRUPTED_PLANKS.get());
        fenceBuilder(ModBlocks.CORRUPTED_FENCE.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        fenceGateBuilder(ModBlocks.CORRUPTED_FENCE_GATE.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        doorBuilder(ModBlocks.CORRUPTED_DOOR.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        trapdoorBuilder(ModBlocks.CORRUPTED_TRAPDOOR.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        pressurePlate(pRecipeOutput, ModBlocks.CORRUPTED_PRESSURE_PLATE.get(), ModBlocks.CORRUPTED_PLANKS.get());
        buttonBuilder(ModBlocks.CORRUPTED_BUTTON.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        woodenBoat(pRecipeOutput, ModItems.CORRUPTED_BOAT.get(), ModBlocks.CORRUPTED_PLANKS.get());
        chestBoat(pRecipeOutput, ModItems.CORRUPTED_CHEST_BOAT.get(), ModItems.CORRUPTED_BOAT.get());
        signBuilder(ModBlocks.CORRUPTED_SIGN.get(), Ingredient.of(ModBlocks.CORRUPTED_PLANKS.get()))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS.get()))
                .save(pRecipeOutput);
        hangingSign(pRecipeOutput, ModItems.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_PLANKS.get());


        woodFromLogs(pRecipeOutput, ModBlocks.VIVICUS_WOOD.get(), ModBlocks.VIVICUS_LOG.get());
        woodFromLogs(pRecipeOutput, ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get());
        stairBuilder(ModBlocks.VIVICUS_STAIRS.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_PLANKS.get());
        fenceBuilder(ModBlocks.VIVICUS_FENCE.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        fenceGateBuilder(ModBlocks.VIVICUS_FENCE_GATE.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        doorBuilder(ModBlocks.VIVICUS_DOOR.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        trapdoorBuilder(ModBlocks.VIVICUS_TRAPDOOR.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        pressurePlate(pRecipeOutput, ModBlocks.VIVICUS_PRESSURE_PLATE.get(), ModBlocks.VIVICUS_PLANKS.get());
        buttonBuilder(ModBlocks.VIVICUS_BUTTON.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        woodenBoat(pRecipeOutput, ModItems.VIVICUS_BOAT.get(), ModBlocks.VIVICUS_PLANKS.get());
        chestBoat(pRecipeOutput, ModItems.VIVICUS_CHEST_BOAT.get(), ModItems.VIVICUS_BOAT.get());
        signBuilder(ModBlocks.VIVICUS_SIGN.get(), Ingredient.of(ModBlocks.VIVICUS_PLANKS.get()))
                .unlockedBy("has_vivicus_planks", has(ModBlocks.VIVICUS_PLANKS.get()))
                .save(pRecipeOutput);
        hangingSign(pRecipeOutput, ModItems.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_PLANKS.get());
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VIVICUS_ANTIDOTE.get(), 1)
                        .pattern(" AB")
                        .pattern("ACA")
                        .pattern("DA ")
                        .define('A', Tags.Items.GLASS_COLORLESS)
                        .define('B', ModItems.JAR_OF_ACID.get())
                        .define('C', ModItems.CORRUPTED_BOBLING_CORE.get())
                        .define('D', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_jar_of_acid", has(ModItems.JAR_OF_ACID.get()))
                        .save(pRecipeOutput);

        SpecialRecipeBuilder.special(ModRecipeSerializers.REBREWED_TIPPED_ARROW.get()).save(pRecipeOutput, "rebrewed_tipped_arrow");
        
        ModCustomRecipeProvider.createRecipes(pRecipeOutput);
    }

    private void trimCrafting(Consumer<FinishedRecipe> pRecipeOutput, ItemLike trim, TagKey<Item> ingredient) {
        trimCrafting(pRecipeOutput, trim, Ingredient.of(ingredient));
    }

    private void trimCrafting(Consumer<FinishedRecipe> pRecipeOutput, ItemLike trim, ItemLike ingredient) {
        trimCrafting(pRecipeOutput, trim, Ingredient.of(ingredient));
    }

    private void trimCrafting(Consumer<FinishedRecipe> pRecipeOutput, ItemLike trim, Ingredient ingredient) {
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

    private void partsRecycling(Consumer<FinishedRecipe> pRecipeOutput, Item part, Item result, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
                .requires(part)
                .unlockedBy("has_" + getItemName(part), has(ModItems.BELT_PIECE.get()))
                .save(pRecipeOutput, MoreSnifferFlowers.loc(getItemName(result) + "_from_part_recycling"));
    }
}
