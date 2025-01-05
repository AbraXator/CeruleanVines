package net.abraxator.moresnifferflowers.compat.jei.rebrewing;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public record JeiRebrewingRecipe(ItemStack extractedPotion, ItemStack rebrewedPotion, ItemStack ingredient) {
    public static List<JeiRebrewingRecipe> createRecipes() {
        List<JeiRebrewingRecipe> ret = new ArrayList<>();
        List<ItemStack> ingredients = List.of(
                Items.REDSTONE.getDefaultInstance(),
                Items.GLOWSTONE_DUST.getDefaultInstance(),
                Items.GUNPOWDER.getDefaultInstance(),
                Items.DRAGON_BREATH.getDefaultInstance()
        );
        List<MobEffect> mobEffects = new ArrayList<>(ForgeRegistries.MOB_EFFECTS.getValues());

        for (ItemStack item : ingredients) {
            for (MobEffect effect : mobEffects) {
                int duration = item.is(Items.REDSTONE) ? 12000 : 6000;
                int amplifier = item.is(Items.GLOWSTONE_DUST) ? 2 : 1;

                MobEffectInstance extractedEffect = new MobEffectInstance(effect, 1200, 0);
                MobEffectInstance rebrewedEffect = new MobEffectInstance(effect, 1200 + duration, amplifier);

                ItemStack extractedPotion = ModItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
                var rebrewedPotion = item.is(Items.GUNPOWDER) ? ModItems.REBREWED_SPLASH_POTION.get().getDefaultInstance() :
                        item.is(Items.DRAGON_BREATH) ? ModItems.REBREWED_LINGERING_POTION.get().getDefaultInstance() :
                                ModItems.REBREWED_POTION.get().getDefaultInstance();

                PotionUtils.setCustomEffects(extractedPotion, List.of(extractedEffect));
                PotionUtils.setCustomEffects(rebrewedPotion, List.of(rebrewedEffect));

                ret.add(new JeiRebrewingRecipe(extractedPotion, rebrewedPotion, item));
            }
        }
        return ret;
    }
}