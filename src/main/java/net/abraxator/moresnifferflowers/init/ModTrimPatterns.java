package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTrimPatterns {
    public static final ResourceKey<TrimPattern> AROMA = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("aroma"));
    public static final ResourceKey<TrimPattern> CARNAGE = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("carnage"));
    public static final ResourceKey<TrimPattern> NETHER_WART = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("nether_wart"));
    public static final ResourceKey<TrimPattern> TATER = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("tater"));
    public static final ResourceKey<TrimPattern> CAROTENE = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("carotene"));
    public static final ResourceKey<TrimPattern> GRAIN = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("grain"));
    public static final ResourceKey<TrimPattern> BEAT = ResourceKey.create(Registries.TRIM_PATTERN, MoreSnifferFlowers.loc("beat"));



    public static void bootstrap(BootstapContext<TrimPattern> context) {
        register(context, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), AROMA);
        register(context, ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CARNAGE);
        register(context, ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), NETHER_WART);
        register(context, ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), TATER);
        register(context, ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), CAROTENE);
        register(context, ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), GRAIN);
        register(context, ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), BEAT);
    }

    private static void register(BootstapContext<TrimPattern> context, Item item, ResourceKey<TrimPattern> key) {
        TrimPattern trimPattern = new TrimPattern(key.location(), ForgeRegistries.ITEMS.getHolder(item).get(), Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())));
        context.register(key, trimPattern);
    }
}
