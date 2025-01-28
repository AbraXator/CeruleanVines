package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.blocks.DawnberryVineBlock;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties DAWNBERRY = new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).fast().build();
    public static final FoodProperties GLOOMBERRY = new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).fast().effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100), 0.8F).effect(() -> new MobEffectInstance(MobEffects.POISON, 100), 0.8F).build();
}
