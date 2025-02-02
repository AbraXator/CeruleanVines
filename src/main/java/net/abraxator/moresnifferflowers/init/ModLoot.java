package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.lootmodifers.conditions.BoblingTypeCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLoot {
    public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> BOBLING_TYPE = CONDITIONS.register("bobling_type", () -> new LootItemConditionType(BoblingTypeCondition.CODEC));
}
