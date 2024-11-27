package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> AMBER = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("amber"));
    public static final ResourceKey<TrimMaterial> GARNET = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("garnet"));
    public static final ResourceKey<TrimMaterial> NETHER_WART = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("nether_wart"));
    public static final ResourceKey<TrimMaterial> POTATO = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("potato"));
    public static final ResourceKey<TrimMaterial> WHEAT = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("wheat"));
    public static final ResourceKey<TrimMaterial> BEETROOT = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("beetroot"));
    public static final ResourceKey<TrimMaterial> CARROT = ResourceKey.create(Registries.TRIM_MATERIAL, MoreSnifferFlowers.loc("carrot"));



    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, AMBER, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.AMBER_SHARD.get()), Style.EMPTY.withColor(TextColor.parseColor("#df910b").getOrThrow()), 0.6F, Map.of());
        register(context, GARNET, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.GARNET_SHARD.get()), Style.EMPTY.withColor(TextColor.parseColor("#8d182b").getOrThrow()), 0.4F, Map.of());
        register(context, NETHER_WART, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.CROPRESSED_NETHERWART.get()), Style.EMPTY.withColor(TextColor.parseColor("#831c20").getOrThrow()), 0.4F, Map.of());
        register(context, POTATO, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.CROPRESSED_POTATO.get()), Style.EMPTY.withColor(TextColor.parseColor("#d9aa51").getOrThrow()), 0.6F, Map.of());
        register(context, WHEAT, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.CROPRESSED_WHEAT.get()), Style.EMPTY.withColor(TextColor.parseColor("#cdb159").getOrThrow()), 0.6F, Map.of());
        register(context, BEETROOT, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.CROPRESSED_BEETROOT.get()), Style.EMPTY.withColor(TextColor.parseColor("#a4272c").getOrThrow()), 0.4F, Map.of());
        register(context, CARROT, BuiltInRegistries.ITEM.wrapAsHolder(ModItems.CROPRESSED_CARROT.get()), Style.EMPTY.withColor(TextColor.parseColor("#e67022").getOrThrow()), 0.5F, Map.of());
    }

    private static void register(BootstrapContext<TrimMaterial> p_268244_, ResourceKey<TrimMaterial> p_268139_, Holder<Item> p_268311_, Style p_268232_, float p_268197_, Map<ArmorMaterials, String> p_268352_) {
        TrimMaterial trimmaterial = new TrimMaterial(p_268139_.location().getPath(), p_268311_, p_268197_, Map.of(), Component.translatable(Util.makeDescriptionId("trim_material", p_268139_.location())).withStyle(p_268232_));
        p_268244_.register(p_268139_, trimmaterial);
    }
}
