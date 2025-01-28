package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class ModBuiltinLoottables {
    public static final Set<ResourceKey<LootTable>> RESOURCES = new HashSet<>();

    public static final ResourceKey<LootTable> SNOW_SNIFFER_TEMPLE = register("snow_sniffer_temple");
    public static final ResourceKey<LootTable> DESSERT_SNIFFER_TEMPLE = register("dessert_sniffer_temple");
    public static final ResourceKey<LootTable> SWAMP_SNIFFER_TEMPLE = register("swamp_sniffer_temple");
    public static final ResourceKey<LootTable> SWAMP_SNIFFER_TEMPLE_CHEST = register("swamp_sniffer_temple_chest");
    public static final ResourceKey<LootTable> SNIFFER_EGG = register("sniffer_egg");

    private static ResourceKey<LootTable> register(String name) {
        var ret = ResourceKey.create(Registries.LOOT_TABLE, MoreSnifferFlowers.loc(name));    
        RESOURCES.add(ret);
        return ret;
    }
    
    public static Set<ResourceKey<LootTable>> all() {
        return RESOURCES;
    }
}
