package net.abraxator.moresnifferflowers.data.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, MoreSnifferFlowers.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(Tags.Items.SEEDS).add(ModItems.DAWNBERRY_VINE_SEEDS.get(), ModItems.AMBUSH_SEEDS.get(), ModItems.BONMEELIA_SEEDS.get(), ModItems.DYESPRIA_SEEDS.get());

        this.tag(ItemTags.TRIM_MATERIALS).add(ModItems.AMBER_SHARD.get(), ModItems.GARNET_SHARD.get(), ModItems.CROPRESSED_BEETROOT.get(), ModItems.CROPRESSED_POTATO.get(), ModItems.CROPRESSED_NETHERWART.get(), ModItems.CROPRESSED_CARROT.get(), ModItems.CROPRESSED_WHEAT.get());
        this.tag(ItemTags.TRIM_TEMPLATES).add(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        this.tag(ModTags.ModItemTags.AROMA_TRIM_TEMPLATE_INGREDIENT).add(ModItems.AMBER_SHARD.get(), ModBlocks.AMBER_BLOCK.get().asItem());
        this.tag(ModTags.ModItemTags.CROPRESSABLE_CROPS).add(Items.POTATO, Items.CARROT, Items.BEETROOT, Items.NETHER_WART, Items.WHEAT);
        this.tag(ModTags.ModItemTags.CROPRESSED_CROPS).add(ModItems.CROPRESSED_CARROT.get(), ModItems.CROPRESSED_POTATO.get(), ModItems.CROPRESSED_WHEAT.get(), ModItems.CROPRESSED_BEETROOT.get(), ModItems.CROPRESSED_NETHERWART.get());
        this.tag(ModTags.ModItemTags.CROP_SMITHING_TEMPLATES).add(ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        this.tag(ModTags.ModItemTags.CROPRESSOR_PIECES).add(ModItems.SCRAP_PIECE.get(), ModItems.BELT_PIECE.get(), ModItems.ENGINE_PIECE.get(), ModItems.TUBE_PIECE.get(), ModItems.PRESS_PIECE.get());

        this.tag(ModTags.ModItemTags.REBREWING_STAND_INGREDIENTS).add(Items.REDSTONE, Items.GLOWSTONE_DUST, Items.GUNPOWDER, Items.DRAGON_BREATH);
        this.tag(ModTags.ModItemTags.REBREWED_POTIONS).add(ModItems.REBREWED_POTION.get(), ModItems.REBREWED_SPLASH_POTION.get(), ModItems.REBREWED_LINGERING_POTION.get());

        this.tag(ItemTags.WOODEN_BUTTONS).add(ModBlocks.CORRUPTED_BUTTON.get().asItem(), ModBlocks.VIVICUS_BUTTON.get().asItem());
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.CORRUPTED_PRESSURE_PLATE.get().asItem(), ModBlocks.VIVICUS_PRESSURE_PLATE.get().asItem());
        this.tag(ItemTags.WOODEN_DOORS).add(ModBlocks.CORRUPTED_DOOR.get().asItem(), ModBlocks.VIVICUS_DOOR.get().asItem());
        this.tag(ItemTags.WOODEN_SLABS).add(ModBlocks.CORRUPTED_SLAB.get().asItem(), ModBlocks.VIVICUS_SLAB.get().asItem());
        this.tag(ItemTags.WOODEN_STAIRS).add(ModBlocks.CORRUPTED_STAIRS.get().asItem(), ModBlocks.VIVICUS_STAIRS.get().asItem());
        this.tag(ItemTags.WOODEN_FENCES).add(ModBlocks.CORRUPTED_FENCE.get().asItem(), ModBlocks.VIVICUS_FENCE.get().asItem());
        this.tag(ItemTags.FENCE_GATES).add(ModBlocks.CORRUPTED_FENCE_GATE.get().asItem(), ModBlocks.VIVICUS_FENCE_GATE.get().asItem());
        this.tag(ItemTags.WOODEN_TRAPDOORS).add(ModBlocks.CORRUPTED_TRAPDOOR.get().asItem(), ModBlocks.VIVICUS_TRAPDOOR.get().asItem());
        this.tag(ItemTags.PLANKS).add(ModBlocks.CORRUPTED_PLANKS.get().asItem(), ModBlocks.VIVICUS_PLANKS.get().asItem());
        this.tag(ItemTags.SAPLINGS).add(ModBlocks.CORRUPTED_SAPLING.get().asItem(), ModBlocks.VIVICUS_SAPLING.get().asItem());
    }

    private Item item(DeferredBlock<Block> object) {
        return object.get().asItem();
    }
}