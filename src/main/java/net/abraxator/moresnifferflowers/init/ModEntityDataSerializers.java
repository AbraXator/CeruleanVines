package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = 
            DeferredRegister.create(ForgeRegistries.ENTITY_DATA_SERIALIZERS.get(), MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<EntityDataSerializer<BoblingEntity.Type>> BOBLING_TYPE = SERIALIZERS.register("bobling_type", () -> EntityDataSerializer.simpleEnum(BoblingEntity.Type.class));
}
