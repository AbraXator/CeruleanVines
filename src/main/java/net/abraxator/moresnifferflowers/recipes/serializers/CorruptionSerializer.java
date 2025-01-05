package net.abraxator.moresnifferflowers.recipes.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CorruptionSerializer implements RecipeSerializer<CorruptionRecipe> {
    @Override
    public CorruptionRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        Block source = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "source")));
        List<CorruptionRecipe.Entry> list = new ArrayList<>();
        for (JsonElement corrupted : GsonHelper.getAsJsonArray(jsonObject, "corrupted")) {
            CorruptionRecipe.Entry entry = CorruptionRecipe.Entry.fromJsonElement(corrupted);
            list.add(entry);
        }
        
        return new CorruptionRecipe(resourceLocation, source, list);
    }

    @Override
    public @Nullable CorruptionRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        List<CorruptionRecipe.Entry> list = new ArrayList<>();
        
        for (int i = 0; i < buf.readInt(); i++) {
            CorruptionRecipe.Entry entry = new CorruptionRecipe.Entry(buf.readRegistryId(), buf.readInt());
            list.add(entry);
        }
        
        return new CorruptionRecipe(resourceLocation, buf.readRegistryId(), list);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CorruptionRecipe recipe) {
        buf.writeInt(recipe.list().size());
        recipe.list().forEach(entry -> {
            buf.writeRegistryId(ForgeRegistries.BLOCKS, entry.block());
            buf.writeInt(entry.weight());
        });
        buf.writeRegistryId(ForgeRegistries.BLOCKS, recipe.source());
    }
}
