package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.lootmodifers.conditions.BoblingTypeCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModLoot {
    public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<LootItemConditionType> BOBLING_TYPE = CONDITIONS.register("bobling_type", () -> new LootItemConditionType(new BoblingTypeCondition.ConditionSerializer()));
}
