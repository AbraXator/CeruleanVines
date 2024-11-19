package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.BonmeeliaBlock;
import net.abraxator.moresnifferflowers.blocks.DawnberryVineBlock;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.fml.common.Mod;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLoottableProvider extends BlockLootSubProvider {
    public ModBlockLoottableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        add(ModBlocks.DAWNBERRY_VINE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().add(this.applyExplosionDecay(ModItems.DAWNBERRY_VINE_SEEDS.get(), LootItem.lootTableItem(ModItems.DAWNBERRY_VINE_SEEDS.get()).apply(Direction.values(), (p_251536_) ->
                        SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.DAWNBERRY_VINE.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(MultifaceBlock.getFaceProperty(p_251536_), true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))))
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModBlocks.DAWNBERRY_VINE.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.DAWNBERRY_VINE.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DawnberryVineBlock.AGE, 4)))));
        dropSelf(ModBlocks.GLOOMBERRY_VINE.get());

        add(ModBlocks.AMBER_BLOCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(ModBlocks.AMBER_BLOCK.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())
                        //COMMON
                        .add(LootItem.lootTableItem(Items.COAL).setWeight(100))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(100))
                        .add(LootItem.lootTableItem(Items.STICK).setWeight(100))
                        .add(LootItem.lootTableItem(ModItems.AMBER_SHARD.get()).setWeight(100))
                        .add(LootItem.lootTableItem(ModItems.DRAGONFLY.get()).setWeight(100))
                        //UNCOMMON
                        .add(LootItem.lootTableItem(Items.CARROT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.POTATO).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(50))
                        .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(50))
                        //RARE
                        .add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.BELT_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.ENGINE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.SCRAP_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.TUBE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.PRESS_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.AMBUSH_BANNER_PATTERN.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.EXTRACTION_BOTTLE.get()).setWeight(25))
                        //VERY RARE
                        .add(LootItem.lootTableItem(Items.TORCHFLOWER_SEEDS).setWeight(12))
                        .add(LootItem.lootTableItem(Items.PITCHER_POD).setWeight(12))
                        .add(LootItem.lootTableItem(Items.SNIFFER_EGG).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.DAWNBERRY_VINE_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.AMBUSH_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.DYESPRIA_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.BONMEELIA_SEEDS.get()).setWeight(12))));

        add(ModBlocks.GARNET_BLOCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(hasSilkTouch())
                        .add(LootItem.lootTableItem(ModBlocks.GARNET_BLOCK.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(doesNotHaveSilkTouch())
                        //COMMON
                        .add(LootItem.lootTableItem(Items.COAL).setWeight(100))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(100))
                        .add(LootItem.lootTableItem(Items.STICK).setWeight(100))
                        .add(LootItem.lootTableItem(ModItems.AMBER_SHARD.get()).setWeight(100))
                        .add(LootItem.lootTableItem(ModItems.DRAGONFLY.get()).setWeight(100))
                        //UNCOMMON
                        .add(LootItem.lootTableItem(Items.CARROT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.POTATO).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(50))
                        .add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(50))
                        .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(50))
                        //RARE
                        .add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.BELT_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.ENGINE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.SCRAP_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.TUBE_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.PRESS_PIECE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.AMBUSH_BANNER_PATTERN.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.EXTRACTION_BOTTLE.get()).setWeight(25))
                        //VERY RARE
                        .add(LootItem.lootTableItem(Items.TORCHFLOWER_SEEDS).setWeight(12))
                        .add(LootItem.lootTableItem(Items.PITCHER_POD).setWeight(12))
                        .add(LootItem.lootTableItem(Items.SNIFFER_EGG).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.DAWNBERRY_VINE_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.AMBUSH_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.DYESPRIA_SEEDS.get()).setWeight(12))
                        .add(LootItem.lootTableItem(ModItems.BONMEELIA_SEEDS.get()).setWeight(12))));

        dropSelf(ModBlocks.BOBLING_HEAD.get());
        dropSelf(ModBlocks.CRACKED_AMBER.get());
        dropSelf(ModBlocks.CHISELED_AMBER.get());
        dropSelf(ModBlocks.CHISELED_AMBER_SLAB.get());
        dropSelf(ModBlocks.AMBER_MOSAIC.get());
        dropSelf(ModBlocks.AMBER_MOSAIC_SLAB.get());
        dropSelf(ModBlocks.AMBER_MOSAIC_STAIRS.get());
        dropSelf(ModBlocks.AMBER_MOSAIC_WALL.get());

        add(ModBlocks.AMBUSH_TOP.get(), noDrop());
        dropSelf(ModBlocks.AMBUSH_BOTTOM.get());
        add(ModBlocks.GARBUSH_TOP.get(), noDrop());
        dropSelf(ModBlocks.GARBUSH_BOTTOM.get());

        dropSelf(ModBlocks.CAULORFLOWER.get());
        add(ModBlocks.GIANT_CARROT.get(), giantCropLoot(Items.CARROT, ModItems.CROPRESSED_CARROT.get(), Items.AIR, ModItems.BELT_PIECE.get(), ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(ModBlocks.GIANT_POTATO.get(), giantCropLoot(Items.POTATO, ModItems.CROPRESSED_POTATO.get(), Items.AIR, ModItems.TUBE_PIECE.get(), ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(ModBlocks.GIANT_NETHERWART.get(), giantCropLoot(Items.NETHER_WART, ModItems.CROPRESSED_NETHERWART.get(), ModItems.BROKEN_REBREWING_STAND.get(), ModItems.ENGINE_PIECE.get(), ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(ModBlocks.GIANT_BEETROOT.get(), giantCropLoot(Items.BEETROOT, ModItems.CROPRESSED_BEETROOT.get(), Items.AIR, ModItems.PRESS_PIECE.get(), ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(ModBlocks.GIANT_WHEAT.get(), giantCropLoot(Items.WHEAT, ModItems.CROPRESSED_WHEAT.get(), Items.AIR, ModItems.SCRAP_PIECE.get(), ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get()));
        add(ModBlocks.BONMEELIA.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.BONMEELIA_SEEDS.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.JAR_OF_BONMEEL.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE)
                                        .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE))
                                .invert()
                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                        .setProperties((StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true)))))));

        add(ModBlocks.BONWILTIA.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.BONMEELIA_SEEDS.get())))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.JAR_OF_BONMEEL.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE)
                                        .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BonmeeliaBlock.AGE, BonmeeliaBlock.MAX_AGE))
                                .invert()
                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BONMEELIA.get())
                                        .setProperties((StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BonmeeliaBlock.HAS_BOTTLE, true)))))));

        dropSelf(ModBlocks.CROPRESSOR_OUT.get());
        dropSelf(ModBlocks.CROPRESSOR_CENTER.get());
        dropSelf(ModBlocks.MORE_SNIFFER_FLOWER.get());
        dropSelf(ModBlocks.REBREWING_STAND_BOTTOM.get());
        add(ModBlocks.REBREWING_STAND_TOP.get(), noDrop());
        add(ModBlocks.DYESPRIA_PLANT.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.DYESPRIA_SEEDS.get()))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.DYESPRIA_PLANT.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(ModStateProperties.AGE_3, 3))
                                .invert())));
        dropSelf(ModBlocks.DYESCRAPIA_PLANT.get());

        dropSelf(ModBlocks.CORRUPTED_LOG.get());
        dropSelf(ModBlocks.CORRUPTED_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_CORRUPTED_LOG.get());
        dropSelf(ModBlocks.STRIPPED_CORRUPTED_WOOD.get());
        dropSelf(ModBlocks.CORRUPTED_PLANKS.get());
        dropSelf(ModBlocks.CORRUPTED_STAIRS.get());
        add(ModBlocks.CORRUPTED_SLAB.get(), this::createSlabItemTable);
        dropSelf(ModBlocks.CORRUPTED_FENCE.get());
        dropSelf(ModBlocks.CORRUPTED_FENCE_GATE.get());
        add(ModBlocks.CORRUPTED_DOOR.get(), this::createDoorTable);
        dropSelf(ModBlocks.CORRUPTED_TRAPDOOR.get()); 
        dropSelf(ModBlocks.CORRUPTED_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.CORRUPTED_BUTTON.get());
        add(ModBlocks.CORRUPTED_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.CORRUPTED_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        dropSelf(ModBlocks.CORRUPTED_SAPLING.get());
        add(ModBlocks.CORRUPTED_SLUDGE.get(), block -> this.createSilkTouchDispatchTable(
                block, this.applyExplosionCondition(
                        block, LootItem.lootTableItem(ModItems.CORRUPTED_SLIME_BALL)
                                .when(LootItemRandomChanceCondition.randomChance(0.175F))
                )
        ));
        dropSelf(ModBlocks.DECAYED_LOG.get());

        dropSelf(ModBlocks.VIVICUS_LOG.get());
        dropSelf(ModBlocks.VIVICUS_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_VIVICUS_LOG.get());
        dropSelf(ModBlocks.STRIPPED_VIVICUS_WOOD.get());
        dropSelf(ModBlocks.VIVICUS_PLANKS.get());
        dropSelf(ModBlocks.VIVICUS_STAIRS.get());
        add(ModBlocks.VIVICUS_SLAB.get(), this::createSlabItemTable);
        dropSelf(ModBlocks.VIVICUS_FENCE.get());
        dropSelf(ModBlocks.VIVICUS_FENCE_GATE.get());
        add(ModBlocks.VIVICUS_DOOR.get(), this::createDoorTable);
        dropSelf(ModBlocks.VIVICUS_TRAPDOOR.get());
        dropSelf(ModBlocks.VIVICUS_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.VIVICUS_BUTTON.get());
        add(ModBlocks.VIVICUS_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.VIVICUS_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        dropSelf(ModBlocks.VIVICUS_SAPLING.get());
        add(ModBlocks.SPROUTING_VIVICUS_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.VIVICUS_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        add(ModBlocks.VIVICUS_LEAVES_SPROUT.get(), block -> createLeavesDrops(block, ModBlocks.VIVICUS_LEAVES_SPROUT.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        add(ModBlocks.CORRUPTED_LEAVES_BUSH.get(), noDrop());

        add(ModBlocks.BOBLING_SACK.get(), noDrop());
        add(ModBlocks.CORRUPTED_SLIME_LAYER.get(), noDrop());

        dropPottedContents(ModBlocks.POTTED_DYESPRIA.get());
        dropPottedContents(ModBlocks.POTTED_CORRUPTED_SAPLING.get());
        dropPottedContents(ModBlocks.POTTED_VIVICUS_SAPLING.get());
        
        dropOther(ModBlocks.CORRUPTED_SIGN.get(), ModItems.CORRUPTED_SIGN);
        dropOther(ModBlocks.CORRUPTED_WALL_SIGN.get(), ModItems.CORRUPTED_SIGN);
        dropOther(ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModItems.CORRUPTED_HANGING_SIGN);
        dropOther(ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), ModItems.CORRUPTED_HANGING_SIGN);
        
        dropOther(ModBlocks.VIVICUS_SIGN.get(), ModItems.VIVICUS_SIGN);
        dropOther(ModBlocks.VIVICUS_WALL_SIGN.get(), ModItems.VIVICUS_SIGN);
        dropOther(ModBlocks.VIVICUS_HANGING_SIGN.get(), ModItems.VIVICUS_HANGING_SIGN);
        dropOther(ModBlocks.VIVICUS_WALL_HANGING_SIGN.get(), ModItems.VIVICUS_HANGING_SIGN);

        add(ModBlocks.BONDRIPIA.get(), noDrop());
        add(ModBlocks.ACIDRIPIA.get(), noDrop());
    }

    private LootTable.Builder giantCropLoot(Item crop, Item cropressed, Item special, Item piece, Item trim) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 4))
                        .add(LootItem.lootTableItem(crop)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(piece).setWeight(50))
                        .add(LootItem.lootTableItem(trim).setWeight(22))
                        .add(LootItem.lootTableItem(cropressed).setWeight(100))
                        .add(LootItem.lootTableItem(special).setWeight(50)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MoreSnifferFlowers.MOD_ID))
                .collect(Collectors.toSet());
    }
}
