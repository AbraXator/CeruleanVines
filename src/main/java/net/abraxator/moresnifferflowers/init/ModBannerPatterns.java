package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

public class ModBannerPatterns {
    public static final ResourceKey<BannerPattern> AMBUSH = create("ambush");
    public static final ResourceKey<BannerPattern> EVIL = create("evil");

    private static ResourceKey<BannerPattern> create(String pName) {
        return ResourceKey.create(Registries.BANNER_PATTERN, MoreSnifferFlowers.loc(pName));
    }
    
    public static void bootstrap(BootstrapContext<BannerPattern> context) {
        context.register(AMBUSH, new BannerPattern(MoreSnifferFlowers.loc("ambush"), "block.minecraft.banner.moresnifferflowers.ambush"));
        context.register(EVIL, new BannerPattern(MoreSnifferFlowers.loc("evil"), "block.minecraft.banner.moresnifferflowers.evil"));
    }
}
