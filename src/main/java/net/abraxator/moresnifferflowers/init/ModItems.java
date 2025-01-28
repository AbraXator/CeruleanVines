package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<Item> DAWNBERRY_VINE_SEEDS = ITEMS.register("dawnberry_vine_seeds", () -> new ItemNameBlockItem(ModBlocks.DAWNBERRY_VINE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GLOOMBERRY_VINE_SEEDS = ITEMS.register("gloomberry_vine_seeds", () -> new ItemNameBlockItem(ModBlocks.GLOOMBERRY_VINE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DAWNBERRY = ITEMS.register("dawnberry", () -> new Item(new Item.Properties().food(ModFoods.DAWNBERRY)));
    public static final RegistryObject<Item> GLOOMBERRY = ITEMS.register("gloomberry", () -> new Item(new Item.Properties().food(ModFoods.GLOOMBERRY)));

    public static final RegistryObject<Item> AMBUSH_SEEDS = ITEMS.register("ambush_seeds", () -> new ItemNameBlockItem(ModBlocks.AMBUSH_BOTTOM.get(), new Item.Properties()));
    public static final RegistryObject<Item> GARBUSH_SEEDS = ITEMS.register("garbush_seeds", () -> new ItemNameBlockItem(ModBlocks.GARBUSH_BOTTOM.get(), new Item.Properties()));

    public static final RegistryObject<Item> AMBUSH_BANNER_PATTERN = ITEMS.register("ambush_banner_pattern", () -> new BannerPatternItem(ModTags.ModBannerPatternTags.AMBUSH_BANNER_PATTERN, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> EVIL_BANNER_PATTERN = ITEMS.register("evil_banner_pattern", () -> new BannerPatternItem(ModTags.ModBannerPatternTags.EVIL_BANNER_PATTERN, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> AMBER_SHARD = ITEMS.register("amber_shard", () -> new TrimMaterialItem(new Item.Properties()));
    public static final RegistryObject<Item> GARNET_SHARD = ITEMS.register("garnet_shard", () -> new TrimMaterialItem(new Item.Properties()));

    public static final RegistryObject<Item> AROMA_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("aroma_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.AROMA));
    public static final RegistryObject<Item> CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carnage_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.CARNAGE));
    public static final RegistryObject<Item> DRAGONFLY = ITEMS.register("dragonfly", () -> new DragonflyItem(new Item.Properties()));
    public static final RegistryObject<Item> DYESPRIA = ITEMS.register("dyespria", () -> new DyespriaItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DYESCRAPIA = ITEMS.register("dyescrapia", () -> new DyescrapiaItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DYESPRIA_SEEDS = ITEMS.register("dyespria_seeds", () -> new ItemNameBlockItem(ModBlocks.DYESPRIA_PLANT.get(), new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
            pTooltip.add(Component.translatableWithFallback("dyespria_seeds.tooltip", "Use shears to hide the dye item").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final RegistryObject<Item> BONMEELIA_SEEDS = ITEMS.register("bonmeelia_seeds", () -> new ItemNameBlockItem(ModBlocks.BONMEELIA.get(), new Item.Properties()));
    public static final RegistryObject<Item> JAR_OF_BONMEEL = ITEMS.register("jar_of_bonmeel", () -> new JarOfBonmeelItem(new Item.Properties()));
    public static final RegistryObject<Item> BONDRIPIA_SEEDS = ITEMS.register("bondripia_seeds", () -> new ItemNameBlockItem(ModBlocks.BONDRIPIA.get(), new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
            pTooltip.add(Component.translatableWithFallback("tooltip.bondripia_seeds", "Plantable underneath an area of 5 blocks in a + shape").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final RegistryObject<Item> BONWILTIA_SEEDS = ITEMS.register("bonwiltia_seeds", () -> new ItemNameBlockItem(ModBlocks.BONWILTIA.get(), new Item.Properties()));
    public static final RegistryObject<Item> JAR_OF_ACID = ITEMS.register("jar_of_acid", () -> new JarOfAcidItem(new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
            pTooltip.add(Component.translatableWithFallback("tooltip.acid_jar", "Ungrows organic blocks").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final RegistryObject<Item> ACIDRIPIA_SEEDS = ITEMS.register("acidripia_seeds", () -> new ItemNameBlockItem(ModBlocks.ACIDRIPIA.get(), new Item.Properties()));

    public static final RegistryObject<Item> CROPRESSOR = ITEMS.register("cropressor", () -> new ItemNameBlockItem(ModBlocks.CROPRESSOR_OUT.get(), new Item.Properties()));
    public static final RegistryObject<Item> TUBE_PIECE = ITEMS.register("tube_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BELT_PIECE = ITEMS.register("belt_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCRAP_PIECE = ITEMS.register("scrap_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENGINE_PIECE = ITEMS.register("engine_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRESS_PIECE = ITEMS.register("press_piece", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> REBREWING_STAND = ITEMS.register("rebrewing_stand", () -> new ItemNameBlockItem(ModBlocks.REBREWING_STAND_BOTTOM.get(), new Item.Properties()));
    public static final RegistryObject<Item> BROKEN_REBREWING_STAND = ITEMS.register("broken_rebrewing_stand", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXTRACTION_BOTTLE = ITEMS.register("extraction_bottle", () -> new BottleOfExtractionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> EXTRACTED_BOTTLE = ITEMS.register("extracted_bottle", () -> new PotionItem(new Item.Properties().stacksTo(1)) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
            pTooltip.add(Component.translatableWithFallback("tooltip.extracted_bottle.obtain", "Obtainable using Bottle o' Extraction").withStyle(ChatFormatting.GOLD));
        }
    });
    public static final RegistryObject<Item> REBREWED_POTION = ITEMS.register("rebrewed_potion", () -> new PotionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REBREWED_SPLASH_POTION = ITEMS.register("rebrewed_splash_potion", () -> new SplashPotionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REBREWED_LINGERING_POTION = ITEMS.register("rebrewed_lingering_potion", () -> new LingeringPotionItem(new Item.Properties().stacksTo(1)));
    
    public static final RegistryObject<Item> CROPRESSED_POTATO = ITEMS.register("cropressed_potato", () -> new TrimMaterialItem(new Item.Properties()));
    public static final RegistryObject<Item> CROPRESSED_CARROT = ITEMS.register("cropressed_carrot", () -> new TrimMaterialItem(new Item.Properties()));
    public static final RegistryObject<Item> CROPRESSED_BEETROOT = ITEMS.register("cropressed_beetroot", () -> new TrimMaterialItem(new Item.Properties()));
    public static final RegistryObject<Item> CROPRESSED_NETHERWART = ITEMS.register("cropressed_nether_wart", () -> new TrimMaterialItem(new Item.Properties()));
    public static final RegistryObject<Item> CROPRESSED_WHEAT = ITEMS.register("cropressed_wheat", () -> new TrimMaterialItem(new Item.Properties()));

    public static final RegistryObject<Item> TATER_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("tater_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.TATER));
    public static final RegistryObject<Item> CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carotene_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.CAROTENE));
    public static final RegistryObject<Item> BEAT_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("beat_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.BEAT));
    public static final RegistryObject<Item> NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("nether_wart_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.NETHER_WART));
    public static final RegistryObject<Item> GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("grain_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(ModTrimPatterns.GRAIN));

    public static final RegistryObject<Item> VIVICUS_ANTIDOTE = ITEMS.register("vivicus_antidote", () -> new VivicusAntidoteItem(new Item.Properties()) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
            pTooltip.add(Component.translatableWithFallback("tooltip.wip", "WIP").withStyle(ChatFormatting.RED));
        }
    });
    public static final RegistryObject<Item> CORRUPTED_BOBLING_CORE = ITEMS.register("corrupted_bobling_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BOBLING_CORE = ITEMS.register("bobling_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORRUPTED_SLIME_BALL = ITEMS.register("corrupted_slime_ball", () -> new CorruptedSlimeBallItem(new Item.Properties()));
    
    public static final RegistryObject<Item> CORRUPTED_SIGN = ITEMS.register("corrupted_sign", () -> new SignItem(new Item.Properties(), ModBlocks.CORRUPTED_SIGN.get(), ModBlocks.CORRUPTED_WALL_SIGN.get()));
    public static final RegistryObject<Item> CORRUPTED_HANGING_SIGN = ITEMS.register("corrupted_hanging_sign", () -> new HangingSignItem(ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> CORRUPTED_BOAT = ITEMS.register("corrupted_boat", () -> new ModBoatItem(false, ModBoatEntity.Type.CORRUPTED, new Item.Properties()));
    public static final RegistryObject<Item> CORRUPTED_CHEST_BOAT = ITEMS.register("corrupted_chest_boat", () -> new ModBoatItem(true, ModBoatEntity.Type.CORRUPTED, new Item.Properties()));

    public static final RegistryObject<Item> VIVICUS_SIGN = ITEMS.register("vivicus_sign", () -> new SignItem(new Item.Properties(), ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_WALL_SIGN.get()));
    public static final RegistryObject<Item> VIVICUS_HANGING_SIGN = ITEMS.register("vivicus_hanging_sign", () -> new HangingSignItem(ModBlocks.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> VIVICUS_BOAT = ITEMS.register("vivicus_boat", () -> new ModBoatItem(false, ModBoatEntity.Type.VIVICUS, new Item.Properties()));
    public static final RegistryObject<Item> VIVICUS_CHEST_BOAT = ITEMS.register("vivicus_chest_boat", () -> new ModBoatItem(true, ModBoatEntity.Type.VIVICUS, new Item.Properties()));

    public static final RegistryObject<Item> BOBLING_SPAWN_EGG = ITEMS.register("bobling_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.BOBLING, 0x312f35, 0xa55f85, new Item.Properties()));

    public static final RegistryObject<Item> CAULORFLOWER_SEEDS = ITEMS.register("caulorflower_seeds", () -> new ItemNameBlockItem(ModBlocks.CAULORFLOWER.get(), new Item.Properties()));
    
    public static final RegistryObject<Item> CREATIVE_TAB_ICON = ITEMS.register("creative_tab_icon", () -> new CreativeTabItem(new Item.Properties()));
    public static final RegistryObject<Item> WAND_OF_CUBING = ITEMS.register("wand_of_cubing", () -> new WandOfCubingItem(new Item.Properties()));

}