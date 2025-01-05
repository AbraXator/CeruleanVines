package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.recipes.RebrewedTippedArrowRecipe;
import net.abraxator.moresnifferflowers.recipes.serializers.CorruptionSerializer;
import net.abraxator.moresnifferflowers.recipes.serializers.CropressingSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<CropressingSerializer> CROPRESSING = RECIPE_SERIALIZERS.register("cropressing", CropressingSerializer::new);
    public static final RegistryObject<RecipeSerializer<RebrewedTippedArrowRecipe>> REBREWED_TIPPED_ARROW = RECIPE_SERIALIZERS.register("rebrewed_tipped_arrow", () -> new SimpleCraftingRecipeSerializer<RebrewedTippedArrowRecipe>(RebrewedTippedArrowRecipe::new));
    public static final RegistryObject<CorruptionSerializer> CORRUPTION = RECIPE_SERIALIZERS.register("corruption", CorruptionSerializer::new);
}