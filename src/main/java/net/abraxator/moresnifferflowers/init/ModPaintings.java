package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTINGS =
        DeferredRegister.create(Registries.PAINTING_VARIANT, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<PaintingVariant> HATTED_FERGUS_TATER = PAINTINGS.register("hatted_fergus_tater", () -> new PaintingVariant(16, 16));
}
