package net.abraxator.moresnifferflowers.data.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
    public ModBlockTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.BLOCK, pLookupProvider, block -> block.builtInRegistryHolder().key(), MoreSnifferFlowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.FLOWERS).add(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.AMBUSH_TOP.get(), ModBlocks.CAULORFLOWER.get(), ModBlocks.DYESPRIA_PLANT.get(), ModBlocks.BONMEELIA.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.AMBUSH_TOP.get(), ModBlocks.CAULORFLOWER.get());

        this.tag(BlockTags.SWORD_EFFICIENT).add(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.AMBUSH_TOP.get(), ModBlocks.CAULORFLOWER.get(), ModBlocks.BONMEELIA.get(),
                ModBlocks.GIANT_CARROT.get(), ModBlocks.GIANT_POTATO.get(), ModBlocks.GIANT_NETHERWART.get(), ModBlocks.GIANT_BEETROOT.get(), ModBlocks.GIANT_WHEAT.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.AMBUSH_BOTTOM.get(), ModBlocks.AMBUSH_TOP.get(), ModBlocks.BONMEELIA.get(), ModBlocks.GIANT_CARROT.get(), ModBlocks.GIANT_POTATO.get(), ModBlocks.GIANT_NETHERWART.get(), ModBlocks.GIANT_BEETROOT.get(), ModBlocks.GIANT_WHEAT.get(), ModBlocks.CAULORFLOWER.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add( ModBlocks.GIANT_CARROT.get(), ModBlocks.GIANT_POTATO.get(), ModBlocks.GIANT_NETHERWART.get(), ModBlocks.GIANT_BEETROOT.get(), ModBlocks.GIANT_WHEAT.get(), ModBlocks.CAULORFLOWER.get());
        
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.AMBER.get(), ModBlocks.CROPRESSOR_OUT.get(), ModBlocks.CROPRESSOR_CENTER.get(), ModBlocks.REBREWING_STAND_BOTTOM.get(), ModBlocks.REBREWING_STAND_TOP.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.AMBER.get(), ModBlocks.CROPRESSOR_OUT.get(), ModBlocks.CROPRESSOR_CENTER.get(), ModBlocks.REBREWING_STAND_BOTTOM.get(), ModBlocks.REBREWING_STAND_TOP.get());

        this.tag(ModTags.ModBlockTags.BONMEELABLE).add(Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.NETHER_WART);
        this.tag(ModTags.ModBlockTags.GIANT_CROPS).add(ModBlocks.GIANT_CARROT.get(), ModBlocks.GIANT_POTATO.get(), ModBlocks.GIANT_NETHERWART.get(), ModBlocks.GIANT_BEETROOT.get(), ModBlocks.GIANT_WHEAT.get());
        this.tag(ModTags.ModBlockTags.GIANT_CROPS).add(ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.SPROUTING_VIVICUS_LEAVES.get());
        
        this.tag(BlockTags.LOGS).add(ModBlocks.CORRUPTED_LOG.get(), ModBlocks.VIVICUS_LOG.get(), ModBlocks.STRIPPED_CORRUPTED_LOG.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get());

        this.tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.CORRUPTED_BUTTON.get(), ModBlocks.VIVICUS_BUTTON.get());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.CORRUPTED_PRESSURE_PLATE.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get());
        this.tag(BlockTags.WOODEN_DOORS).add(ModBlocks.CORRUPTED_DOOR.get(), ModBlocks.VIVICUS_DOOR.get());
        this.tag(BlockTags.WOODEN_SLABS).add(ModBlocks.CORRUPTED_SLAB.get(), ModBlocks.VIVICUS_SLAB.get());
        this.tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.CORRUPTED_STAIRS.get(), ModBlocks.VIVICUS_STAIRS.get());
        this.tag(BlockTags.WOODEN_FENCES).add(ModBlocks.CORRUPTED_FENCE.get(), ModBlocks.VIVICUS_FENCE.get());
        this.tag(BlockTags.FENCE_GATES).add(ModBlocks.CORRUPTED_FENCE_GATE.get(), ModBlocks.VIVICUS_FENCE_GATE.get());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.CORRUPTED_TRAPDOOR.get(), ModBlocks.VIVICUS_TRAPDOOR.get());
        this.tag(BlockTags.PLANKS).add(ModBlocks.CORRUPTED_PLANKS.get(), ModBlocks.VIVICUS_PLANKS.get());
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.CORRUPTED_SAPLING.get(), ModBlocks.VIVICUS_SAPLING.get());

        this.tag(ModTags.ModBlockTags.VIVICUS_BLOCKS).add(
            ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(), ModBlocks.VIVICUS_BUTTON.get(), 
            ModBlocks.VIVICUS_DOOR.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(), 
            ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_PLANKS.get(),
            ModBlocks.VIVICUS_PRESSURE_PLATE.get(), ModBlocks.VIVICUS_SAPLING.get(), ModBlocks.VIVICUS_STAIRS.get(),
            ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_WOOD.get(),
            ModBlocks.SPROUTING_VIVICUS_LEAVES.get());
    
        this.tag(BlockTags.FLOWER_POTS).add(ModBlocks.POTTED_DYESPRIA.get(), ModBlocks.POTTED_CORRUPTED_SAPLING.get(), ModBlocks.POTTED_VIVICUS_SAPLING.get());
    }
}
