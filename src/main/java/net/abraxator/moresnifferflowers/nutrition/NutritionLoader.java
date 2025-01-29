package net.abraxator.moresnifferflowers.nutrition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class NutritionLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Codec<Map<Either<TagKey<Item>, Item>, Integer>> CODEC = Codec.unboundedMap(
            Codec.either(
                    TagKey.hashedCodec(Registries.ITEM), ForgeRegistries.ITEMS.getCodec()
            ), Codec.INT
    );
    
    private Map<String, List<Nutrition>> modNutritions = ImmutableMap.of();
    private List<Nutrition> allNutritions = ImmutableList.of();
    
    public NutritionLoader() {
        super(GSON, "nutrition");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<String, List<Nutrition>> modNutritions = ImmutableMap.of();
        List<Nutrition> allNutritions = ImmutableList.of();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            Map<Item, List<NutritionEntry>> currentModNutritions = Maps.newHashMap();
            ResourceLocation path = entry.getKey();
            
            try {
                for (NutritionType nutritionType : NutritionType.values()) {
                    JsonObject entries = entry.getValue().getAsJsonObject().getAsJsonObject(nutritionType.name);
                    var values = CODEC.parse(JsonOps.INSTANCE, entries).get();
                    if(values.left().isPresent()) {
                        var map = values.left().get().entrySet().stream()
                                .map(mapEntry -> {
                                    List<Item> itemList = new ArrayList<>();
                                    Map<Item, NutritionEntry> list = new HashMap<>();
                                    if(mapEntry.getKey().left().isPresent()) {
                                        itemList = (Arrays.stream(Ingredient.of(mapEntry.getKey().left().get()).getItems())
                                                .map(ItemStack::getItem)
                                                .toList()
                                        );
                                    } 
                                    if(mapEntry.getKey().right().isPresent()) {
                                        itemList = List.of(mapEntry.getKey().right().get());
                                    }

                                    for (Item item : itemList) {
                                        list.put(item, new NutritionEntry(nutritionType, mapEntry.getValue()));
                                    }
                                    
                                    return list;
                                }).findAny().orElseGet(HashMap::new);
                        
                        map.forEach((item, nutritionEntry) -> {
                            if (currentModNutritions.containsKey(item)) {
                                List<NutritionEntry> nutritions = new ArrayList<>(currentModNutritions.get(item));
                                nutritions.add(nutritionEntry);
                                currentModNutritions.put(item, nutritions);
                            }
                        });
                    }
                }
            } catch (IllegalStateException | JsonParseException e) {
                MoreSnifferFlowers.LOGGER.error("Error parsing nutrition {}", path, e);
            }
        
            var nutritionList = currentModNutritions.entrySet().stream()
                    .map(itemListEntry -> new Nutrition(itemListEntry.getKey(), itemListEntry.getValue()))
                    .toList();
            modNutritions.put(
                    path.getPath(), 
                    nutritionList);
            allNutritions.addAll(nutritionList);
        }
        
        this.modNutritions = modNutritions;
        this.allNutritions = allNutritions;
    }
}
