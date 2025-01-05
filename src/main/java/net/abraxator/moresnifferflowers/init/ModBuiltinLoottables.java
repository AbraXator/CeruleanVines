package net.abraxator.moresnifferflowers.init;

import com.google.common.collect.Sets;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class ModBuiltinLoottables {
    public static final Set<ResourceLocation> MOD_LOOT_TABLES = Sets.newHashSet();

    public static final ResourceLocation SNOW_SNIFFER_TEMPLE = register("snow_sniffer_temple");
    public static final ResourceLocation DESSERT_SNIFFER_TEMPLE = register("dessert_sniffer_temple");
    public static final ResourceLocation SWAMP_SNIFFER_TEMPLE = register("swamp_sniffer_temple");
    public static final ResourceLocation SWAMP_SNIFFER_TEMPLE_CHEST = register("swamp_sniffer_temple_chest");
    public static final ResourceLocation SNIFFER_EGG = register("sniffer_egg");

    private static ResourceLocation register(String name) {
        if(MOD_LOOT_TABLES.add(MoreSnifferFlowers.loc(name))) {
            return MoreSnifferFlowers.loc(name);
        } else throw new IllegalArgumentException("Unknown loot table: " + name);
    }
    
    public static Set<ResourceLocation> all() {
        return Collections.unmodifiableSet(MOD_LOOT_TABLES);
    }
}
