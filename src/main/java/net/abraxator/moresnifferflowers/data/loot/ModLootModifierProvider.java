package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.lootmodifers.AddItemsModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootModifierProvider extends GlobalLootModifierProvider {
    public ModLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void start() {
        add("seeds_from_sniffing", new AddItemsModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.parse("gameplay/sniffer_digging")).build()
        }, List.of(ModItems.DAWNBERRY_VINE_SEEDS.get(), ModItems.DYESPRIA_SEEDS.get(), ModItems.AMBUSH_SEEDS.get(), ModItems.CAULORFLOWER_SEEDS.get(),
                ModItems.BONMEELIA_SEEDS.get(), ModItems.BONDRIPIA_SEEDS.get(), ModBlocks.VIVICUS_SAPLING.get().asItem())));
    }

}
