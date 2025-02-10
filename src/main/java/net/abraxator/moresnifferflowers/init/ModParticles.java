package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<SimpleParticleType> FLY = PARTICLES.register("fly", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CARROT = PARTICLES.register("carrot", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> AMBUSH = PARTICLES.register("ambush", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GIANT_CROP = PARTICLES.register("giant_crop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GARBUSH = PARTICLES.register("garbush", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> BONDRIPIA_DRIP = PARTICLES.register("bondripia_drip", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BONDRIPIA_FALL = PARTICLES.register("bondripia_fall", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BONDRIPIA_LAND = PARTICLES.register("bondripia_land", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ACIDRIPIA_DRIP = PARTICLES.register("acidripia_drip", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ACIDRIPIA_FALL = PARTICLES.register("acidripia_fall", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ACIDRIPIA_LAND = PARTICLES.register("acidripia_land", () -> new SimpleParticleType(false));


}
