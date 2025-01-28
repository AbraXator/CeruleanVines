package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<RecipeType<CropressingRecipe>> CROPRESSING = register("cropressing");
    public static final RegistryObject<RecipeType<CorruptionRecipe>> CORRUPTION = register("corruption");

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(final String id) {
        return RECIPE_TYPES.register(id, () -> RecipeType.simple(MoreSnifferFlowers.loc(id)));
    }
}
