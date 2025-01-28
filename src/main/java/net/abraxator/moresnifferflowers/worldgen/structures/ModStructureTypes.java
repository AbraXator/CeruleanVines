package net.abraxator.moresnifferflowers.worldgen.structures;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.structures.DessertSnifferTemple;
import net.abraxator.moresnifferflowers.worldgen.structures.SnowSnifferTemple;
import net.abraxator.moresnifferflowers.worldgen.structures.SwampSnifferTempleStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_PIECE = DeferredRegister.create(Registries.STRUCTURE_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<SnowSnifferTemple>> SNOW_SNIFFER_TEMPLE = STRUCTURE_PIECE.register("snow_sniffer_temple", () -> explicitStructureTypeTyping(SnowSnifferTemple.CODEC));
    public static final DeferredHolder<StructureType<?>, StructureType<DessertSnifferTemple>> DESSERT_SNIFFER_TEMPLE = STRUCTURE_PIECE.register("dessert_sniffer_temple", () -> explicitStructureTypeTyping(DessertSnifferTemple.CODEC));
    public static final DeferredHolder<StructureType<?>, StructureType<SwampSnifferTempleStructure>> SWAMP_SNIFFER_TEMPLE = STRUCTURE_PIECE.register("swamp_sniffer_temple", () -> () -> SwampSnifferTempleStructure.CODEC);

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}

