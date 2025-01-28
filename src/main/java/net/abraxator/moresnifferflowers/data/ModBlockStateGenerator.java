package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class ModBlockStateGenerator extends BlockStateProvider {
    public ModBlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MoreSnifferFlowers.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        VariantBlockStateBuilder rebrewingStandVariantBuilder = getVariantBuilder(ModBlocks.REBREWING_STAND_BOTTOM.get());
        boolean[] bottleConditions = {false, true};
        for (boolean hasBottle0 : bottleConditions) {
            for (boolean hasBottle1 : bottleConditions) {
                for (boolean hasBottle2 : bottleConditions) {
                    String modelCode = (hasBottle0 ? "1" : "0") + (hasBottle1 ? "1" : "0") + (hasBottle2 ? "1" : "0");
                    ModelFile modelFile = rebrewingStandModel(modelCode);
                    rebrewingStandVariantBuilder.partialState()
                            .with(HAS_BOTTLE_0, hasBottle0)
                            .with(HAS_BOTTLE_1, hasBottle1)
                            .with(HAS_BOTTLE_2, hasBottle2)
                            .addModels(new ConfiguredModel(modelFile));
                }
            }
        }
        
        simpleBlock(ModBlocks.POTTED_DYESPRIA.get(), models().withExistingParent(ModBlocks.POTTED_DYESPRIA.getId().getPath(), "block/flower_pot_cross").renderType("cutout").texture("plant", blockTexture(ModBlocks.DYESPRIA_PLANT.get())));
        simpleBlock(ModBlocks.POTTED_CORRUPTED_SAPLING.get(), models().withExistingParent(ModBlocks.POTTED_CORRUPTED_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType("cutout").texture("plant", blockTexture(ModBlocks.CORRUPTED_SAPLING.get())));
        simpleBlock(ModBlocks.POTTED_VIVICUS_SAPLING.get(), models().withExistingParent(ModBlocks.POTTED_VIVICUS_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType("cutout").texture("plant", blockTexture(ModBlocks.VIVICUS_SAPLING.get())));
        signBlock((StandingSignBlock) ModBlocks.CORRUPTED_SIGN.get(), (WallSignBlock) ModBlocks.CORRUPTED_WALL_SIGN.get(), blockTexture(ModBlocks.CORRUPTED_PLANKS.get()));
        signBlock((StandingSignBlock) ModBlocks.VIVICUS_SIGN.get(), (WallSignBlock) ModBlocks.VIVICUS_WALL_SIGN.get(), blockTexture(ModBlocks.VIVICUS_PLANKS.get()));
        hangingSignBlock(ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), blockTexture(ModBlocks.CORRUPTED_PLANKS.get()));
        hangingSignBlock(ModBlocks.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_WALL_HANGING_SIGN.get(), blockTexture(ModBlocks.VIVICUS_PLANKS.get()));
        //logWoodSapling((RotatedPillarBlock) ModBlocks.CORRUPTED_LOG.get(), (RotatedPillarBlock) ModBlocks.STRIPPED_CORRUPTED_LOG.get(), (RotatedPillarBlock) ModBlocks.CORRUPTED_WOOD.get(), (RotatedPillarBlock) ModBlocks.STRIPPED_CORRUPTED_WOOD.get(), ModBlocks.CORRUPTED_SAPLING.get());
        //plankBlocks("corrupted", ModBlocks.CORRUPTED_PLANKS.get(), ModBlocks.CORRUPTED_SLAB.get(), ((StairBlock) ModBlocks.CORRUPTED_STAIRS.get()), ModBlocks.CORRUPTED_BUTTON.get(), ModBlocks.CORRUPTED_FENCE.get(), ModBlocks.CORRUPTED_FENCE_GATE.get(), ModBlocks.CORRUPTED_PRESSURE_PLATE.get(), ((DoorBlock) ModBlocks.CORRUPTED_DOOR.get()), ((TrapDoorBlock) ModBlocks.CORRUPTED_TRAPDOOR.get()), true);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    protected void logWoodSapling(RotatedPillarBlock log, RotatedPillarBlock slog, RotatedPillarBlock wood, RotatedPillarBlock swood, Block sapling) {
        logBlock(log);
        logBlock(slog);
        ResourceLocation sideTex = blockTexture(log);
        axisBlock(wood, sideTex, sideTex);
        ResourceLocation sSideTex = blockTexture(slog);
        axisBlock(swood, sSideTex, sSideTex);

        ResourceLocation saplingTex = prefix("block/" + name(sapling));
        simpleBlock(sapling, models().cross(name(sapling), saplingTex).renderType("cutout"));
    }

    protected void plankBlocks(String variant, Block plank, Block slab, StairBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor) {
        this.plankBlocks(variant, plank, slab, stair, button, fence, gate, plate, door, trapdoor, false);
    }

    protected void plankBlocks(String variant, Block plank, Block slab, StairBlock stair, Block button, Block fence, Block gate, Block plate, DoorBlock door, TrapDoorBlock trapdoor, boolean cutoutDoors) {
        String plankTexName = "block/" + variant + "_planks";
        String plankDir = "block/" + variant + "/";
        ConfiguredModel[] plankModels = ConfiguredModel.builder()
                .modelFile(models().cubeAll(plankDir + name(plank), prefix(plankTexName))).nextModel().build();
        simpleBlock(plank, plankModels);

        String slabDir = variant + "/";
        ConfiguredModel[] bottomSlabModels = ConfiguredModel.builder()
                .weight(10).modelFile(models().slab(slabDir + name(slab), prefix(plankTexName), prefix(plankTexName), prefix(plankTexName))).build();
        ConfiguredModel[] topSlabModels = ConfiguredModel.builder()
                .weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[0].model).nextModel()
                .weight(10).uvLock(true).rotationX(180).modelFile(bottomSlabModels[1].model).nextModel()
                .weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[2].model).nextModel()
                .weight(1).uvLock(true).rotationX(180).modelFile(bottomSlabModels[3].model).build();
        getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(bottomSlabModels);
        getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.TOP).setModels(topSlabModels);
        getVariantBuilder(slab).partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(plankModels);

        woodStairs(stair, plankTexName, variant);
        woodButton(button, plankTexName, variant);
        woodFence(fence, plankTexName, variant);
        woodGate(gate, plankTexName, variant);
        woodPlate(plate, plankTexName, variant);
        String doorDir = variant + "/";
        String trapdoorDir = variant + "/";

        doorBlockWithRenderType(door, doorDir + variant, prefix(variant + "_lower"), prefix(variant + "_upper"), cutoutDoors ? "cutout" : "solid");
        trapdoorBlockWithRenderType(trapdoor, trapdoorDir + variant, prefix(variant + "_trapdoor"), true, cutoutDoors ? "cutout" : "solid");
    }

    private BlockModelBuilder door(String name, String model, ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
        return models().withExistingParent(name, prefix("block/util/" + model))
                .texture("bottom", bottom)
                .texture("top", top)
                .texture("side", side);
    }

    protected void woodGate(Block gate, String texName, String variant) {
        String gateDir = variant + "/";

        ModelFile gate0 = models().fenceGate(gateDir + name(gate), prefix(texName));
        ModelFile open0 = models().fenceGateOpen(gateDir + name(gate) + "_open", prefix(texName));
        ModelFile wall0 = models().fenceGateWall(gateDir + name(gate) + "_wall", prefix(texName));
        ModelFile wallOpen0 = models().fenceGateWallOpen(gateDir + name(gate) + "_wall_open", prefix(texName));

        // [VanillaCopy] super.fenceGateBlock except with more models
        getVariantBuilder(gate).forAllStatesExcept(state -> {
            ModelFile model0 = gate0;
            if (state.getValue(FenceGateBlock.IN_WALL)) {
                model0 = wall0;
            }
            if (state.getValue(FenceGateBlock.OPEN)) {
                model0 = model0 == wall0 ? wallOpen0 : open0;
            }
            return ConfiguredModel.builder()
                    .weight(10).modelFile(model0)
                    .rotationY((int) state.getValue(HorizontalDirectionalBlock.FACING).toYRot())
                    .uvLock(true).nextModel()
                    .build();
        }, FenceGateBlock.POWERED);
    }

    protected void woodFence(Block fence, String texName, String variant) {
        String fenceDir = variant + "/";
        
        ModelFile post0 = models().fencePost(fenceDir + name(fence) + "_post", prefix((texName)));
        ModelFile side0 = models().fenceSide(fenceDir + name(fence) + "_side", prefix((texName)));

        // [VanillaCopy] super.fourWayBlock, but with more models
        MultiPartBlockStateBuilder builder = getMultipartBuilder(fence).part()
                .weight(10).modelFile(post0).nextModel().addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
            if (dir.getAxis().isHorizontal()) {
                builder.part()
                        .weight(10).modelFile(side0).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true)
                        .addModel()
                        .condition(value, true);
            }
        });
    }

    protected void woodPlate(Block plate, String texName, String variant) {
        String plateDir = variant + "/";

        ConfiguredModel[] unpressed = ConfiguredModel.builder()
                .weight(10).modelFile(models().withExistingParent(plateDir + name(plate), "pressure_plate_up").texture("texture", prefix(texName))).nextModel().build();
        ConfiguredModel[] pressed = ConfiguredModel.builder()
                .weight(10).modelFile(models().withExistingParent(plateDir + name(plate) + "_down", "pressure_plate_down").texture("texture", prefix((texName)))).build();

        getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, false).setModels(unpressed);
        getVariantBuilder(plate).partialState().with(PressurePlateBlock.POWERED, true).setModels(pressed);
    }

    protected void woodButton(Block button, String texName, String variant) {
        String buttonDir = variant + "/";
        
        ModelFile unpressed0 = models().withExistingParent(buttonDir + name(button), "button").texture("texture", prefix(texName));
        ModelFile pressed0 = models().withExistingParent(buttonDir + name(button) + "_pressed", "button_pressed").texture("texture", prefix((texName)));

        getVariantBuilder(button).forAllStates(state -> {
            ModelFile model0 = state.getValue(ButtonBlock.POWERED) ? pressed0 : unpressed0;
            int rotX = switch (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE)) {
                case WALL -> 90;
                case FLOOR -> 0;
                case CEILING -> 180;
            };
            int rotY = 0;
            if (state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING) {
                switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                    case NORTH -> rotY = 180;
                    case WEST -> rotY = 90;
                    case EAST -> rotY = 270;
                }
            } else {
                switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                    case SOUTH -> rotY = 180;
                    case WEST -> rotY = 270;
                    case EAST -> rotY = 90;
                }
            }
            boolean uvlock = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.WALL;

            return ConfiguredModel.builder()
                    .weight(10).uvLock(uvlock).rotationX(rotX).rotationY(rotY).modelFile(model0).nextModel().build();
        });
    }

    protected void woodStairs(StairBlock block, String texName, String variant) {
        String stairsDir = variant + "/";
        ModelFile main0 = models().stairs(stairsDir + name(block), prefix(texName), prefix(texName), prefix(texName));
        ModelFile inner0 = models().stairsInner(stairsDir + name(block) + "_inner", prefix(texName), prefix(texName), prefix(texName));
        ModelFile outer0 = models().stairsOuter(stairsDir + name(block) + "_outer", prefix(texName), prefix(texName), prefix(texName));
        // [VanillaCopy] super.stairsBlock, but multiple files returned each time
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(StairBlock.FACING);
                    Half half = state.getValue(StairBlock.HALF);
                    StairsShape shape = state.getValue(StairBlock.SHAPE);
                    int yRot = (int) facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
                    if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                        yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
                    }
                    if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                        yRot += 90; // Top stairs are rotated 90 degrees clockwise
                    }
                    yRot %= 360;
                    boolean uvlock = yRot != 0 || half == Half.TOP; // Don't set uvlock for states that have no rotation
                    return ConfiguredModel.builder()
                            .weight(10)
                            .modelFile(shape == StairsShape.STRAIGHT ? main0 : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? inner0 : outer0)
                            .rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock)
                            .nextModel()
                            .build();
                }, StairBlock.WATERLOGGED);
    }    
    
    private ModelFile rebrewingStandModel(String index) {
        return models().getExistingFile(MoreSnifferFlowers.loc("block/rebrewing_stand_" + index));
    }
    
    private ResourceLocation prefix(String path) {
        return MoreSnifferFlowers.loc("textures/" + path);
    }
    
    protected ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }
}
