package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.init.ModBuiltinLoottables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLoottableProvider extends LootTableProvider {
    public ModLoottableProvider(PackOutput pOutput) {
        super(pOutput, ModBuiltinLoottables.MOD_LOOT_TABLES, List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLoottableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModArcheologyLoot::new, LootContextParamSets.ARCHAEOLOGY),
                new LootTableProvider.SubProviderEntry(ModChestLoot::new, LootContextParamSets.CHEST)
        ));
    }
}
