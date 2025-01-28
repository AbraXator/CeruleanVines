package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.init.ModBuiltinLoottables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLoottableProvider {
    public static LootTableProvider create(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        return new LootTableProvider(
                pOutput,
                ModBuiltinLoottables.all(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLoottableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModArcheologyLoot::new, LootContextParamSets.ARCHAEOLOGY),
                        new LootTableProvider.SubProviderEntry(ModChestLoot::new, LootContextParamSets.CHEST)
                ),
                pRegistries
        );
    }
}
