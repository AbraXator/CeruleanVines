package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.*;
import net.abraxator.moresnifferflowers.blocks.corrupted.CorruptedGrassBlock;
import net.abraxator.moresnifferflowers.blocks.corrupted.CorruptedLeavesBlock;
import net.abraxator.moresnifferflowers.blocks.corrupted.CorruptedSlimeLayerBlock;
import net.abraxator.moresnifferflowers.blocks.corrupted.CorruptedSludgeBlock;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockUpper;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockOut;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockTop;
import net.abraxator.moresnifferflowers.blocks.signs.ModHangingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModStandingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallHangingSign;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallSignBlock;
import net.abraxator.moresnifferflowers.blocks.vivicus.*;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockUpper;
import net.abraxator.moresnifferflowers.items.GiantCropItem;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<Block> DAWNBERRY_VINE = registerBlockNoItem("dawnberry_vine", () -> new DawnberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).replaceable().noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion(), false));
    public static final RegistryObject<Block> GLOOMBERRY_VINE = registerBlockNoItem("gloomberry_vine", () -> new GloomberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).replaceable().noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion()));
    
    public static final RegistryObject<Block> AMBUSH_BOTTOM = registerBlockNoItem("ambush_bottom", () -> new AmbushBlockLower(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F)));
    public static final RegistryObject<Block> AMBUSH_TOP = registerBlockNoItem("ambush_top", () -> new AmbushBlockUpper(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F)));
    public static final RegistryObject<Block> GARBUSH_BOTTOM = registerBlockNoItem("garbush_bottom", () -> new GarbushBlockLower(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F)));
    public static final RegistryObject<Block> GARBUSH_TOP = registerBlockNoItem("garbush_top", () -> new GarbushBlockUpper(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F)));
    
    public static final RegistryObject<Block> AMBER_BLOCK = registerBlockWithItem("amber_block", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> CHISELED_AMBER = registerBlockWithItem("chiseled_amber", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> CHISELED_AMBER_SLAB = registerBlockWithItem("chiseled_amber_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.CHISELED_AMBER.get())));
    public static final RegistryObject<Block> CRACKED_AMBER = registerBlockWithItem("cracked_amber", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> AMBER_MOSAIC = registerBlockWithItem("amber_mosaic", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> AMBER_MOSAIC_SLAB = registerBlockWithItem("amber_mosaic_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.AMBER_MOSAIC.get())));
    public static final RegistryObject<Block> AMBER_MOSAIC_STAIRS = registerBlockWithItem("amber_mosaic_stairs", () -> stair(AMBER_MOSAIC.get()));
    public static final RegistryObject<Block> AMBER_MOSAIC_WALL = registerBlockWithItem("amber_mosaic_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.AMBER_MOSAIC.get())));
    public static final RegistryObject<Block> GARNET_BLOCK = registerBlockWithItem("garnet_block", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(5.0F).noOcclusion()));
    public static final RegistryObject<Block> CHISELED_GARNET = registerBlockWithItem("chiseled_garnet", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> CHISELED_GARNET_SLAB = registerBlockWithItem("chiseled_garnet_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.CHISELED_GARNET.get())));
    public static final RegistryObject<Block> CRACKED_GARNET = registerBlockWithItem("cracked_garnet", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> GARNET_MOSAIC = registerBlockWithItem("garnet_mosaic", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final RegistryObject<Block> GARNET_MOSAIC_SLAB = registerBlockWithItem("garnet_mosaic_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.GARNET_MOSAIC.get())));
    public static final RegistryObject<Block> GARNET_MOSAIC_STAIRS = registerBlockWithItem("garnet_mosaic_stairs", () -> stair(GARNET_MOSAIC.get()));
    public static final RegistryObject<Block> GARNET_MOSAIC_WALL = registerBlockWithItem("garnet_mosaic_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.GARNET_MOSAIC.get())));

    public static final RegistryObject<Block> CAULORFLOWER = registerBlockNoItem("caulorflower", () ->  new CaulorflowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion().randomTicks()));
    
    public static final RegistryObject<Block> GIANT_CARROT = registerGiantCrop("giant_carrot", () ->  new GiantCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final RegistryObject<Block> GIANT_POTATO = registerGiantCrop("giant_potato", () ->  new GiantCropBlock(BlockBehaviour.Properties.copy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final RegistryObject<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart", () ->  new GiantCropBlock(BlockBehaviour.Properties.copy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final RegistryObject<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot", () ->  new GiantCropBlock(BlockBehaviour.Properties.copy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    public static final RegistryObject<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat", () ->  new GiantCropBlock(BlockBehaviour.Properties.copy(ModBlocks.GIANT_CARROT.get()).noOcclusion()));
    
    public static final RegistryObject<Block> BONMEELIA = registerBlockNoItem("bonmeelia", () ->  new BonmeeliaBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion(), false));
    public static final RegistryObject<Block> BONWILTIA = registerBlockNoItem("bonwiltia", () ->  new BonmeeliaBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion(), true));
    public static final RegistryObject<Block> BONDRIPIA = registerBlockNoItem("bondripia", () ->  new BondripiaBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> ACIDRIPIA = registerBlockNoItem("acidripia", () ->  new AciddripiaBlock(BlockBehaviour.Properties.copy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> BONMEEL_FILLED_CAULDRON = registerBlockNoItem("bonmeel_filled_cauldron", () ->  new LayeredCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), precipitation -> precipitation == Biome.Precipitation.NONE, ModCauldronInteractions.BONMEEL));
    public static final RegistryObject<Block> ACID_FILLED_CAULDRON = registerBlockNoItem("acid_filled_cauldron", () ->  new LayeredCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), precipitation -> precipitation == Biome.Precipitation.NONE, ModCauldronInteractions.ACID));
    
    public static final RegistryObject<Block> CROPRESSOR_CENTER = registerBlockNoItem("cropressor_center", () ->  new CropressorBlockBase(BlockBehaviour.Properties.copy(Blocks.ANVIL), CropressorBlockBase.Part.CENTER));
    public static final RegistryObject<Block> CROPRESSOR_OUT = registerBlockNoItem("cropressor_out", () ->  new CropressorBlockOut(BlockBehaviour.Properties.copy(Blocks.ANVIL), CropressorBlockBase.Part.OUT));
    
    public static final RegistryObject<Block> MORE_SNIFFER_FLOWER = registerBlockNoItem("more_sniffer_flower", () -> new MoreSnifferFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion()));
    
    public static final RegistryObject<Block> REBREWING_STAND_BOTTOM = registerBlockNoItem("rebrewing_stand_bottom", () -> new RebrewingStandBlockBase(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    public static final RegistryObject<Block> REBREWING_STAND_TOP = registerBlockNoItem("rebrewing_stand_top", () -> new RebrewingStandBlockTop(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    
    public static final RegistryObject<Block> DYESPRIA_PLANT = registerBlockNoItem("dyespria_plant", () ->  new DyespriaPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> DYESCRAPIA_PLANT = registerBlockNoItem("dyescrapia_plant", () ->  new DyescrapiaPlantBlock(BlockBehaviour.Properties.copy(DYESPRIA_PLANT.get())));

    public static final RegistryObject<Block> CORRUPTED_LOG = registerBlockWithItem("corrupted_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_STEM)));
    public static final RegistryObject<Block> CORRUPTED_WOOD = registerBlockWithItem("corrupted_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_HYPHAE)));
    public static final RegistryObject<Block> STRIPPED_CORRUPTED_LOG = registerBlockWithItem("stripped_corrupted_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_STEM)));
    public static final RegistryObject<Block> STRIPPED_CORRUPTED_WOOD = registerBlockWithItem("stripped_corrupted_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_HYPHAE)));
    public static final RegistryObject<Block> CORRUPTED_PLANKS = registerBlockWithItem("corrupted_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
    public static final RegistryObject<Block> CORRUPTED_STAIRS = registerBlockWithItem("corrupted_stairs", () -> stair(CORRUPTED_PLANKS.get()));
    public static final RegistryObject<Block> CORRUPTED_SLAB = registerBlockWithItem("corrupted_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_SLAB)));
    public static final RegistryObject<Block> CORRUPTED_FENCE = registerBlockWithItem("corrupted_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_FENCE)));
    public static final RegistryObject<Block> CORRUPTED_FENCE_GATE = registerBlockWithItem("corrupted_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_FENCE_GATE), ModWoodTypes.CORRUPTED));
    public static final RegistryObject<Block> CORRUPTED_DOOR = registerBlockWithItem("corrupted_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_DOOR), BlockSetType.WARPED));
    public static final RegistryObject<Block> CORRUPTED_TRAPDOOR = registerBlockWithItem("corrupted_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_TRAPDOOR), BlockSetType.WARPED));
    public static final RegistryObject<Block> CORRUPTED_PRESSURE_PLATE = registerBlockWithItem("corrupted_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.WARPED_PRESSURE_PLATE), BlockSetType.WARPED));
    public static final RegistryObject<Block> CORRUPTED_BUTTON = registerBlockWithItem("corrupted_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_BUTTON), BlockSetType.WARPED, 30, true));
    public static final RegistryObject<Block> CORRUPTED_LEAVES = registerBlockWithItem("corrupted_leaves", () -> new CorruptedLeavesBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_LEAVES).noOcclusion()));
    public static final RegistryObject<Block> CORRUPTED_SAPLING = registerBlockWithItem("corrupted_sapling", () -> new SaplingBlock(new CorruptedTreeGrower(), BlockBehaviour.Properties.copy(Blocks.ACACIA_SAPLING)));
    public static final RegistryObject<Block> CORRUPTED_SLUDGE = registerBlockWithItem("corrupted_sludge", () -> new CorruptedSludgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(2.0F).friction(0.8F).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final RegistryObject<Block> CORRUPTED_SLIME_LAYER = registerBlockWithItem("corrupted_slime_layer", () -> new CorruptedSlimeLayerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(0.5F).friction(0.8F).noOcclusion().randomTicks().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final RegistryObject<Block> CORRUPTED_SIGN = registerBlockNoItem("corrupted_sign", () -> new ModStandingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.copy(Blocks.WARPED_SIGN)));
    public static final RegistryObject<Block> CORRUPTED_WALL_SIGN = registerBlockNoItem("corrupted_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.copy(Blocks.WARPED_WALL_SIGN)));
    public static final RegistryObject<Block> CORRUPTED_HANGING_SIGN = registerBlockNoItem("corrupted_hanging_sign", () -> new ModHangingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.copy(Blocks.WARPED_HANGING_SIGN)));
    public static final RegistryObject<Block> CORRUPTED_WALL_HANGING_SIGN = registerBlockNoItem("corrupted_wall_hanging_sign", () -> new ModWallHangingSign(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.copy(Blocks.WARPED_WALL_HANGING_SIGN)));
    public static final RegistryObject<Block> CORRUPTED_LEAVES_BUSH = registerBlockWithItem("corrupted_leaves_bush", () -> new CorruptedLeavesBlock(BlockBehaviour.Properties.copy(ModBlocks.CORRUPTED_LEAVES.get()).noOcclusion()));

    public static final RegistryObject<Block> DECAYED_LOG = registerBlockWithItem("decayed_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> CORRUPTED_GRASS_BLOCK = registerBlockWithItem("corrupted_grass_block", () -> new CorruptedGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS)));

    public static final RegistryObject<Block> VIVICUS_LOG = registerBlockWithItem("vivicus_log", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LOG)));
    public static final RegistryObject<Block> VIVICUS_WOOD = registerBlockWithItem("vivicus_wood", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_WOOD)));
    public static final RegistryObject<Block> STRIPPED_VIVICUS_LOG = registerBlockWithItem("stripped_vivicus_log", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CHERRY_LOG)));
    public static final RegistryObject<Block> STRIPPED_VIVICUS_WOOD = registerBlockWithItem("stripped_vivicus_wood", () -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CHERRY_WOOD)));
    public static final RegistryObject<Block> VIVICUS_PLANKS = registerBlockWithItem("vivicus_planks", () -> new VivicusBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_PLANKS)));
    public static final RegistryObject<Block> VIVICUS_STAIRS = registerBlockWithItem("vivicus_stairs", () -> vivicusStair(VIVICUS_PLANKS.get()));
    public static final RegistryObject<Block> VIVICUS_SLAB = registerBlockWithItem("vivicus_slab", () -> new VivicusSlabBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_SLAB)));
    public static final RegistryObject<Block> VIVICUS_FENCE = registerBlockWithItem("vivicus_fence", () -> new VivicusFenceBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_FENCE)));
    public static final RegistryObject<Block> VIVICUS_FENCE_GATE = registerBlockWithItem("vivicus_fence_gate", () -> new VivicusFenceGateBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.copy(Blocks.CHERRY_FENCE_GATE)));
    public static final RegistryObject<Block> VIVICUS_DOOR = registerBlockWithItem("vivicus_door", () -> new VivicusDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.copy(Blocks.CHERRY_DOOR)));
    public static final RegistryObject<Block> VIVICUS_TRAPDOOR = registerBlockWithItem("vivicus_trapdoor", () -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.copy(Blocks.CHERRY_TRAPDOOR)));
    public static final RegistryObject<Block> VIVICUS_PRESSURE_PLATE = registerBlockWithItem("vivicus_pressure_plate", () -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.copy(Blocks.CHERRY_PRESSURE_PLATE)));
    public static final RegistryObject<Block> VIVICUS_BUTTON = registerBlockWithItem("vivicus_button", () -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, BlockBehaviour.Properties.copy(Blocks.CHERRY_BUTTON)));
    public static final RegistryObject<Block> VIVICUS_LEAVES = registerBlockWithItem("vivicus_leaves", () -> new VivicusLeavesBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES)));
    public static final RegistryObject<Block> VIVICUS_SAPLING = registerBlockWithItem("vivicus_sapling", () -> new VivicusSaplingBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_SAPLING)));
    public static final RegistryObject<Block> VIVICUS_LEAVES_SPROUT = registerBlockWithItem("vivicus_leaves_sprout", () -> new VivicusSproutingBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_PROPAGULE)));
    public static final RegistryObject<Block> VIVICUS_SIGN = registerBlockNoItem("vivicus_sign", () -> new VivicusStandingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.copy(Blocks.CHERRY_SIGN)));
    public static final RegistryObject<Block> VIVICUS_WALL_SIGN = registerBlockNoItem("vivicus_wall_sign", () -> new VivicusWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.copy(Blocks.CHERRY_WALL_SIGN)));
    public static final RegistryObject<Block> VIVICUS_HANGING_SIGN = registerBlockNoItem("vivicus_hanging_sign", () -> new VivicusHangingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.copy(Blocks.CHERRY_HANGING_SIGN)));
    public static final RegistryObject<Block> VIVICUS_WALL_HANGING_SIGN = registerBlockNoItem("vivicus_wall_hanging_sign", () -> new VivicusHangingWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.copy(Blocks.CHERRY_WALL_HANGING_SIGN)));

    public static final RegistryObject<Block> BOBLING_HEAD = registerBlockNoItem("bobling_head", () -> new BoblingHeadBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> BOBLING_SACK = registerBlockNoItem("bobling_sack", () -> new BoblingSackBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES)));
        
    public static final RegistryObject<Block> POTTED_DYESPRIA = registerBlockNoItem("potted_dyespria", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_CORRUPTED_SAPLING = registerBlockNoItem("potted_corrupted_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_VIVICUS_SAPLING = registerBlockNoItem("potted_vivicus_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    
    private static <T extends Block> RegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerGiantCrop(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerGiantCropItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerGiantCropItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new GiantCropItem(block.get(),
                new Item.Properties()));
    }

    public static Block vivicusStair(Block pBaseBlock) {
        return new VivicusStairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.copy(pBaseBlock));
    }
    
    public static Block stair(Block pBaseBlock) {
        return new StairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.copy(pBaseBlock));
    }
}
