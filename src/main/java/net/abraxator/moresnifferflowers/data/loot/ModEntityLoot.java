package net.abraxator.moresnifferflowers.data.loot;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.lootmodifers.conditions.BoblingTypeCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

public class ModEntityLoot extends EntityLootSubProvider {

    protected ModEntityLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        add(ModEntityTypes.BOBLING.get(), LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.CORRUPTED_BOBLING_CORE.get())
                                .when(BoblingTypeCondition.builder(false))
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.8F, 0.05F))
                        )
                        .add(LootItem.lootTableItem(ModItems.BOBLING_CORE.get())
                                .when(BoblingTypeCondition.builder(true))
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.4F, 0.1F))
                        )
        ));
        
    }


    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entities -> ForgeRegistries.ENTITY_TYPES.getKey(entities).getNamespace().equals(MoreSnifferFlowers.MOD_ID));
    }
}
