package net.abraxator.moresnifferflowers.lootmodifers.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModLoot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record BoblingTypeCondition(boolean cured) implements LootItemCondition {

    public static final MapCodec<BoblingTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(o -> o.cured)).apply(instance, BoblingTypeCondition::new));

    @Override
    public LootItemConditionType getType() {
        return ModLoot.BOBLING_TYPE.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof BoblingEntity bobling && bobling.isCured() == cured;
    }


    public static Builder builder(boolean cured) {
        return () -> new BoblingTypeCondition(cured);
    }
}
