package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBannerPatterns {
    public static final DeferredRegister<BannerPattern> BANNER_PATTERNS =
            DeferredRegister.create(Registries.BANNER_PATTERN, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<BannerPattern> AMBUSH = BANNER_PATTERNS.register("ambush", () -> new BannerPattern("msfa"));
    public static final RegistryObject<BannerPattern> EVIL = BANNER_PATTERNS.register("evil", () -> new BannerPattern("msfe"));
}
