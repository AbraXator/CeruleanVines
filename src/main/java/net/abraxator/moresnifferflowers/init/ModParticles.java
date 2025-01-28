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
    public static final RegistryObject<SimpleParticleType> BONDRIPIA = PARTICLES.register("bondripia", () -> new SimpleParticleType(false));
}
