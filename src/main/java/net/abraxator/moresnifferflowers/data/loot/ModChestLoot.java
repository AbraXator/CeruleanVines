package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.init.ModBuiltinLoottables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record ModChestLoot() implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> pOutput) {
        pOutput.accept(
                ModBuiltinLoottables.SWAMP_SNIFFER_TEMPLE_CHEST,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(UniformGenerator.between(2.0F, 8.0F))
                                        .add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                        .add(LootItem.lootTableItem(Items.COAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                        .add(LootItem.lootTableItem(Items.STONE_AXE).setWeight(2))
                                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.EMERALD))
                                        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                        )
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GOLDEN_APPLE)))
        );
    }
}
