package net.abraxator.moresnifferflowers.worldgen.structures;

import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_PIECE = 
            DeferredRegister.create(Registries.STRUCTURE_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<StructureType<SnowSnifferTemple>> SNOW_SNIFFER_TEMPLE = STRUCTURE_PIECE.register("snow_sniffer_temple", () -> explicitStructureTypeTyping(SnowSnifferTemple.CODEC));
    public static final RegistryObject<StructureType<DessertSnifferTemple>> DESSERT_SNIFFER_TEMPLE = STRUCTURE_PIECE.register("dessert_sniffer_temple", () -> explicitStructureTypeTyping(DessertSnifferTemple.CODEC));
    
    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}

