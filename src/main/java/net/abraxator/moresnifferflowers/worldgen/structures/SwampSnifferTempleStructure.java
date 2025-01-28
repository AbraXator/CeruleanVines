package net.abraxator.moresnifferflowers.worldgen.structures;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.worldgen.structures.pieces.SwampSnifferTemplePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class SwampSnifferTempleStructure extends Structure {
    public static final MapCodec<SwampSnifferTempleStructure> CODEC = RecordCodecBuilder.mapCodec(
            swampSnifferTempleStructureInstance -> swampSnifferTempleStructureInstance.group(
                    settingsCodec(swampSnifferTempleStructureInstance)
            )
                    .apply(swampSnifferTempleStructureInstance, SwampSnifferTempleStructure::new)
    );
    
    protected SwampSnifferTempleStructure(StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return onTopOfChunkCenter(pContext, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, structurePiecesBuilder ->  this.generatePieces(structurePiecesBuilder, pContext));
    }

    private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom worldgenRandom = context.random();
        int height = context.chunkGenerator().getFirstOccupiedHeight(chunkPos.x, chunkPos.z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()) + 3;
        BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
        Rotation rotation = Rotation.CLOCKWISE_90;
        SwampSnifferTemplePieces.addPieces(context.structureTemplateManager(), pos, rotation, builder, worldgenRandom);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.SWAMP_SNIFFER_TEMPLE.get();
    }
}
