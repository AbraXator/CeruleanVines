package net.abraxator.moresnifferflowers.recipes.serializers;

import com.google.common.collect.Streams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CorruptionSerializer implements RecipeSerializer<CorruptionRecipe> {
    @Override
    public CorruptionRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        String source = GsonHelper.getAsString(jsonObject, "source");
        
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
        String source = buf.readUtf();
        int count = buf.readInt();

        for (int i = 0; i < count; i++) {
            CorruptionRecipe.Entry entry = new CorruptionRecipe.Entry(buf.readRegistryId(), buf.readInt());
            list.add(entry);
        }
        
        return new CorruptionRecipe(resourceLocation, source, list);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CorruptionRecipe recipe) {
        buf.writeUtf(recipe.source());
        
        buf.writeInt(recipe.list().size());
        recipe.list().forEach(entry -> {
            buf.writeRegistryId(ForgeRegistries.BLOCKS, entry.block());
            buf.writeInt(entry.weight());
        });
    }
}
