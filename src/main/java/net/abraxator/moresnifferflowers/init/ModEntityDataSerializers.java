package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = 
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MoreSnifferFlowers.MOD_ID);
    
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<BoblingEntity.Type>> BOBLING_TYPE = SERIALIZERS.register("bobling_type", () -> EntityDataSerializer.forValueType(BoblingEntity.Type.STREAM_CODEC));
}
