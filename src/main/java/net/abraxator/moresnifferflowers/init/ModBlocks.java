package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.*;
import net.abraxator.moresnifferflowers.blocks.ambush.AmbushBlockBase;
import net.abraxator.moresnifferflowers.blocks.ambush.AmbushBlockUpper;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockOut;
import net.abraxator.moresnifferflowers.blocks.giantcrops.GiantCropBlock;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockTop;
import net.abraxator.moresnifferflowers.blocks.signs.ModHangingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModStandingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallHangingSign;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallSignBlock;
import net.abraxator.moresnifferflowers.blocks.vivicus.*;
import net.abraxator.moresnifferflowers.items.GiantCropItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreSnifferFlowers.MOD_ID);

    public static final DeferredBlock<Block> DAWNBERRY_VINE = registerBlockNoItem("dawnberry_vine", () -> new DawnberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).replaceable().noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion(), false));
    public static final DeferredBlock<Block> GLOOMBERRY_VINE = registerBlockNoItem("gloomberry_vine", () -> new GloomberryVineBlock(BlockBehaviour.Properties.ofFullCopy(DAWNBERRY_VINE.get())));
    
    public static final DeferredBlock<Block> AMBUSH_BOTTOM = registerBlockNoItem("ambush_bottom", () -> new AmbushBlockBase(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> AMBUSH_TOP = registerBlockNoItem("ambush_top", () -> new AmbushBlockUpper(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));

    public static final DeferredBlock<Block> AMBER_BLOCK = registerBlockWithItem("amber_block", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_AMBER = registerBlockWithItem("chiseled_amber", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_AMBER_SLAB = registerBlockWithItem("chiseled_amber_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHISELED_AMBER.get())));
    public static final DeferredBlock<Block> CRACKED_AMBER = registerBlockWithItem("cracked_amber", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC = registerBlockWithItem("amber_mosaic", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC_SLAB = registerBlockWithItem("amber_mosaic_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> AMBER_MOSAIC_STAIRS = registerBlockWithItem("amber_mosaic_stairs", () -> stair(AMBER_MOSAIC.get()));
    public static final DeferredBlock<Block> AMBER_MOSAIC_WALL = registerBlockWithItem("amber_mosaic_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get())));

    public static final DeferredBlock<Block> CAULORFLOWER = registerBlockNoItem("caulorflower", () ->  new CaulorflowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion().randomTicks()));
    
    public static final DeferredBlock<Block> GIANT_CARROT = registerGiantCrop("giant_carrot", () ->  new GiantCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Block> GIANT_POTATO = registerGiantCrop("giant_potato", () ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final DeferredBlock<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart", () ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final DeferredBlock<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot", () ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final DeferredBlock<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat", () ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    
    public static final DeferredBlock<Block> BONMEELIA = registerBlockNoItem("bonmeelia", () ->  new BonmeeliaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion()));
    public static final DeferredBlock<Block> BONDRIPIA = registerBlockWithItem("bondripia", () ->  new BondripiaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks()));
    
    public static final DeferredBlock<Block> CROPRESSOR_CENTER = registerBlockNoItem("cropressor_center", () ->  new CropressorBlockBase(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL), CropressorBlockBase.Part.CENTER));
    public static final DeferredBlock<Block> CROPRESSOR_OUT = registerBlockNoItem("cropressor_out", () ->  new CropressorBlockOut(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL), CropressorBlockBase.Part.OUT));
    
    public static final DeferredBlock<Block> MORE_SNIFFER_FLOWER = registerBlockNoItem("more_sniffer_flower", () -> new MoreSnifferFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion()));
    
    public static final DeferredBlock<Block> REBREWING_STAND_BOTTOM = registerBlockNoItem("rebrewing_stand_bottom", () -> new RebrewingStandBlockBase(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    public static final DeferredBlock<Block> REBREWING_STAND_TOP = registerBlockNoItem("rebrewing_stand_top", () -> new RebrewingStandBlockTop(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    
    public static final DeferredBlock<Block> DYESPRIA_PLANT = registerBlockNoItem("dyespria_plant", () ->  new DyespriaPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> DYESCRAPIA_PLANT = registerBlockNoItem("dyescrapia_plant", () ->  new DyescrapiaPlantBlock(BlockBehaviour.Properties.ofFullCopy(DYESPRIA_PLANT.get())));

    public static final DeferredBlock<Block> CORRUPTED_LOG = registerBlockWithItem("corrupted_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM)));
    public static final DeferredBlock<Block> CORRUPTED_WOOD = registerBlockWithItem("corrupted_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_LOG = registerBlockWithItem("stripped_corrupted_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_WOOD = registerBlockWithItem("stripped_corrupted_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE)));
    public static final DeferredBlock<Block> CORRUPTED_PLANKS = registerBlockWithItem("corrupted_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS)));
    public static final DeferredBlock<Block> CORRUPTED_STAIRS = registerBlockWithItem("corrupted_stairs", () -> stair(CORRUPTED_PLANKS.get()));
    public static final DeferredBlock<Block> CORRUPTED_SLAB = registerBlockWithItem("corrupted_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SLAB)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE = registerBlockWithItem("corrupted_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE_GATE = registerBlockWithItem("corrupted_fence_gate", () -> new FenceGateBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE_GATE)));
    public static final DeferredBlock<Block> CORRUPTED_DOOR = registerBlockWithItem("corrupted_door", () -> new DoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_DOOR)));
    public static final DeferredBlock<Block> CORRUPTED_TRAPDOOR = registerBlockWithItem("corrupted_trapdoor", () -> new TrapDoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_TRAPDOOR)));
    public static final DeferredBlock<Block> CORRUPTED_PRESSURE_PLATE = registerBlockWithItem("corrupted_pressure_plate", () -> new PressurePlateBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PRESSURE_PLATE)));
    public static final DeferredBlock<Block> CORRUPTED_BUTTON = registerBlockWithItem("corrupted_button", () -> new ButtonBlock(BlockSetType.WARPED, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_BUTTON)));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES = registerBlockWithItem("corrupted_leaves", () -> new CorruptedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK).noOcclusion()));
    public static final DeferredBlock<Block> CORRUPTED_SAPLING = registerBlockWithItem("corrupted_sapling", () -> new SaplingBlock(ModTreeGrowers.CORRUPTED_TREE, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FUNGUS)));
    public static final DeferredBlock<Block> CORRUPTED_SLUDGE = registerBlockWithItem("corrupted_sludge", () -> new CorruptedSludgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SLIME_LAYER = registerBlockWithItem("corrupted_slime_layer", () -> new CorruptedSlimeLayerBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CORRUPTED_SLUDGE.get()).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SIGN = registerBlockNoItem("corrupted_sign", () -> new ModStandingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_WALL_SIGN = registerBlockNoItem("corrupted_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_HANGING_SIGN = registerBlockNoItem("corrupted_hanging_sign", () -> new ModHangingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HANGING_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_WALL_HANGING_SIGN = registerBlockNoItem("corrupted_wall_hanging_sign", () -> new ModWallHangingSign(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_HANGING_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES_BUSH = registerBlockWithItem("corrupted_leaves_bush", () -> new CorruptedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK).noOcclusion()));

    public static final DeferredBlock<Block> DECAYED_LOG = registerBlockWithItem("decayed_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CORRUPTED_LOG.get())));

    public static final DeferredBlock<Block> VIVICUS_LOG = registerBlockWithItem("vivicus_log", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG)));
    public static final DeferredBlock<Block> VIVICUS_WOOD = registerBlockWithItem("vivicus_wood", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_LOG = registerBlockWithItem("stripped_vivicus_log", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_LOG)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_WOOD = registerBlockWithItem("stripped_vivicus_wood", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD)));
    public static final DeferredBlock<Block> VIVICUS_PLANKS = registerBlockWithItem("vivicus_planks", () -> new VivicusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));
    public static final DeferredBlock<Block> VIVICUS_STAIRS = registerBlockWithItem("vivicus_stairs", () -> vivicusStair(VIVICUS_PLANKS.get()));
    public static final DeferredBlock<Block> VIVICUS_SLAB = registerBlockWithItem("vivicus_slab", () -> new VivicusSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB)));
    public static final DeferredBlock<Block> VIVICUS_FENCE = registerBlockWithItem("vivicus_fence", () -> new VivicusFenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE)));
    public static final DeferredBlock<Block> VIVICUS_FENCE_GATE = registerBlockWithItem("vivicus_fence_gate", () -> new VivicusFenceGateBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE_GATE)));
    public static final DeferredBlock<Block> VIVICUS_DOOR = registerBlockWithItem("vivicus_door", () -> new VivicusDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR)));
    public static final DeferredBlock<Block> VIVICUS_TRAPDOOR = registerBlockWithItem("vivicus_trapdoor", () -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_TRAPDOOR)));
    public static final DeferredBlock<Block> VIVICUS_PRESSURE_PLATE = registerBlockWithItem("vivicus_pressure_plate", () -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PRESSURE_PLATE)));
    public static final DeferredBlock<Block> VIVICUS_BUTTON = registerBlockWithItem("vivicus_button", () -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON)));
    public static final DeferredBlock<Block> VIVICUS_LEAVES = registerBlockWithItem("vivicus_leaves", () -> new VivicusLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
    public static final DeferredBlock<Block> VIVICUS_SAPLING = registerBlockWithItem("vivicus_sapling", () -> new VivicusSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING)));
    public static final DeferredBlock<Block> SPROUTING_VIVICUS_LEAVES = registerBlockWithItem("sprouting_vivicus_leaves", () -> new VivicusSproutingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
    public static final DeferredBlock<Block> VIVICUS_LEAVES_SPROUT = registerBlockWithItem("vivicus_leaves_sprout", () -> new VivicusSproutingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
    public static final DeferredBlock<Block> VIVICUS_SIGN = registerBlockNoItem("vivicus_sign", () -> new VivicusStandingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_WALL_SIGN = registerBlockNoItem("vivicus_wall_sign", () -> new VivicusWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_HANGING_SIGN = registerBlockNoItem("vivicus_hanging_sign", () -> new VivicusHangingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_HANGING_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_WALL_HANGING_SIGN = registerBlockNoItem("vivicus_wall_hanging_sign", () -> new VivicusHangingWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_HANGING_SIGN)));

    public static final DeferredBlock<Block> BOBLING_HEAD = registerBlockNoItem("bobling_head", () -> new BoblingHeadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).noOcclusion()));
    public static final DeferredBlock<Block> BOBLING_SACK = registerBlockNoItem("bobling_sack", () -> new BoblingSackBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
        
    public static final DeferredBlock<Block> POTTED_DYESPRIA = registerBlockNoItem("potted_dyespria", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)));
    public static final DeferredBlock<Block> POTTED_CORRUPTED_SAPLING = registerBlockNoItem("potted_corrupted_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)));
    public static final DeferredBlock<Block> POTTED_VIVICUS_SAPLING = registerBlockNoItem("potted_vivicus_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)));
    
    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> registerGiantCrop(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerGiantCropItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerGiantCropItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new GiantCropItem(block.get(),
                new Item.Properties()));
    }

    public static Block vivicusStair(Block pBaseBlock) {
        return new VivicusStairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock));
    }
    
    public static Block stair(Block pBaseBlock) {
        return new StairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock));
    }
}
