package net.abraxator.moresnifferflowers.worldgen.structures.pieces;

import com.sun.jna.platform.win32.WinBase;
import io.netty.util.Constant;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBuiltinLoottables;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwampSnifferTemplePieces {
    public static final int GENERATION_HEIGHT = 90;
    public static final ResourceLocation BASE_LOCATION = MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_base");
    public static final List<ResourceLocation> CORNER_PIECES_LOCATION = List.of(
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_1"),
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_2"),
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_3"),
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_4"),
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_5"),
            MoreSnifferFlowers.loc("swamp_sniffer_temple/swamp_sniffer_temple_corner_6")
    );
    public static final BlockPos PIVOT = new BlockPos(5, -2, -5);
    
    public static void addPieces(StructureTemplateManager pStructureTemplateManager, BlockPos pStartPos, Rotation pRotation, StructurePieceAccessor pPieces, RandomSource pRandom) {
        int corner_chance = pRandom.nextIntBetweenInclusive(2, 4);
        var list = new ArrayList<>(CORNER_PIECES_LOCATION);
        Collections.shuffle(list);
        var sublist = list.subList(0, 4);
        var rotation = pRotation.getRotated(Rotation.CLOCKWISE_90);
        //BlockPos blockPos = pStartPos.immutable();
        
        for(int i = 0; i < sublist.size(); i++) {
            Direction dir = Direction.fromYRot((i + 1) * 90);
            BlockPos piecePos = pStartPos.relative(dir, 1).relative(dir.getClockWise(), 1);
            pPieces.addPiece(new SwampSnifferTemplePiece(
                    pStructureTemplateManager, 
                    sublist.get(i), 
                    piecePos, 
                    rotation));
            rotation = rotation.getRotated(Rotation.CLOCKWISE_90);
        }
        
        pPieces.addPiece(new SwampSnifferTemplePiece(pStructureTemplateManager, BASE_LOCATION, pStartPos.offset(4, -3, -4), pRotation));
    }
    
    public static class SwampSnifferTemplePiece extends TemplateStructurePiece {
        public SwampSnifferTemplePiece(StructureTemplateManager pStructureTemplateManager, ResourceLocation pLocation, BlockPos startPos, Rotation rotation) {
            super(ModPieceTypes.SWAMP_SNIFFER_TEMPLE.get(), 0, pStructureTemplateManager, pLocation, pLocation.toString(), makeSettings(rotation, pLocation), startPos);
        }

        public SwampSnifferTemplePiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
            super(ModPieceTypes.SWAMP_SNIFFER_TEMPLE.get(), tag, structureTemplateManager, resourceLocation -> makeSettings(Rotation.valueOf(tag.getString("Rot")), resourceLocation));
        }

        private static StructurePlaceSettings makeSettings(Rotation rot, ResourceLocation resourceLocation) {
            return new StructurePlaceSettings()
                    .setRotation(rot)
                    .setMirror(Mirror.NONE)
                    .addProcessor(suspiciateGravel())
                    .addProcessor(addMoss())
                    .addProcessor(addChest());
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
            super.addAdditionalSaveData(pContext, pTag);
            pTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
            int i = pLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.templatePosition.getX(), this.templatePosition.getZ());
            this.templatePosition = this.templatePosition.offset(0, i - 90, 0);
            super.postProcess(pLevel, pStructureManager, pGenerator, pRandom, pBox, pChunkPos, pPos);
        }

        @Override
        protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {}

        private static StructureProcessor suspiciateGravel() {
            return new CappedProcessor(
                    new RuleProcessor(
                            List.of(
                                    new ProcessorRule(
                                            new BlockMatchTest(Blocks.GRAVEL),
                                            AlwaysTrueTest.INSTANCE,
                                            PosAlwaysTrueTest.INSTANCE,
                                            Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(),
                                            new AppendLoot(ModBuiltinLoottables.SWAMP_SNIFFER_TEMPLE)
                                    )
                            )
                    ),
                    UniformInt.of(4, 6)
            );
        }
        
        private static StructureProcessor addMoss() {
            return new CappedProcessor(
                    new RuleProcessor(
                            List.of(
                                    new ProcessorRule(
                                            new RandomBlockMatchTest(Blocks.MOSS_BLOCK, 0.8F),
                                            AlwaysTrueTest.INSTANCE,
                                            Blocks.AIR.defaultBlockState()
                                    ),
                                    new ProcessorRule(
                                            new RandomBlockMatchTest(Blocks.MANGROVE_ROOTS, 0.4F),
                                            AlwaysTrueTest.INSTANCE,
                                            Blocks.MOSS_BLOCK.defaultBlockState()
                                    )
                            )
                    ),
                    ConstantInt.of(6)
            );
        }
        
        private static StructureProcessor addChest() {
            return new CappedProcessor(
                    new RuleProcessor(
                            List.of(
                                    new ProcessorRule(
                                            new BlockMatchTest(Blocks.CHEST),
                                            AlwaysTrueTest.INSTANCE,
                                            PosAlwaysTrueTest.INSTANCE,
                                            Blocks.CHEST.defaultBlockState(),
                                            new AppendLoot(ModBuiltinLoottables.SWAMP_SNIFFER_TEMPLE_CHEST)
                                    )
                            )
                    ),
                    UniformInt.of(4, 6)
            );
        }
    }
}
