package net.abraxator.moresnifferflowers.worldgen.structures.pieces;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;

public class ModPieceTypes {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE = 
            DeferredRegister.create(BuiltInRegistries.STRUCTURE_PIECE, MoreSnifferFlowers.MOD_ID);
    
    public static final DeferredHolder<StructurePieceType, StructurePieceType> SWAMP_SNIFFER_TEMPLE = setTemplatePieceId(SwampSnifferTemplePieces.SwampSnifferTemplePiece::new, "SST");

    private static DeferredHolder<StructurePieceType, StructurePieceType> setFullContextPieceId(StructurePieceType pPieceType, String pPieceId) {
        return STRUCTURE_PIECE.register(pPieceId.toLowerCase(Locale.ROOT), () -> pPieceType);
    }

    private static DeferredHolder<StructurePieceType, StructurePieceType> setPieceId(StructurePieceType.ContextlessType pType, String pKey) {
        return setFullContextPieceId(pType, pKey);
    }

    private static DeferredHolder<StructurePieceType, StructurePieceType> setTemplatePieceId(StructurePieceType.StructureTemplateType pTemplateType, String pPieceId) {
        return setFullContextPieceId(pTemplateType, pPieceId);
    }
}
