package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.CropressorAnimation;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.networking.DyespriaModePacket;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = 
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final Supplier<DataComponentType<Dye>> DYE = DATA_COMPONENTS.register("dye", () -> DataComponentType.<Dye>builder().persistent(Dye.CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<DyespriaMode>> DYESPRIA_MODE = DATA_COMPONENTS.register("dyespria_mode", () -> DataComponentType.<DyespriaMode>builder().persistent(DyespriaMode.CODEC).networkSynchronized(DyespriaMode.STREAM_CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<CropressorAnimation>> CROPRESSOR_ANIMATE = DATA_COMPONENTS.register("cropressor_animate", () -> DataComponentType.<CropressorAnimation>builder().persistent(CropressorAnimation.CODEC).cacheEncoding().build());
    public static final Supplier<DataComponentType<Integer>> DYESPRIA_USES = DATA_COMPONENTS.register("dyespria_uses", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
    
}
