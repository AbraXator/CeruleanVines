package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTINGS =
        DeferredRegister.create(Registries.PAINTING_VARIANT, MoreSnifferFlowers.MOD_ID);
    
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HATTED_FERGUS_TATER = PAINTINGS.register("hatted_fergus_tater", () -> new PaintingVariant(16, 16, MoreSnifferFlowers.loc("hatted_fergus_tater")));
}
