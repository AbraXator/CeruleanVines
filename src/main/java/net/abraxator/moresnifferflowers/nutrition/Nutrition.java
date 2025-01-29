package net.abraxator.moresnifferflowers.nutrition;

import com.mojang.datafixers.util.Either;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

public class Nutrition {
    private Item item;
    private List<NutritionEntry> nutritionEntries;

    public Nutrition(Item item, List<NutritionEntry> nutritionEntries) {
        this.item = item;
        this.nutritionEntries = nutritionEntries;
    }

    public Item getItem() {
        return item;
    }

    public List<NutritionEntry> getNutritionEntries() {
        return nutritionEntries;
    }
}
