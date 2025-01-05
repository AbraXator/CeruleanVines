package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<SoundEvent> CROPRESSOR_BELT = variableRange("block.cropressor.belt");
    public static final RegistryObject<SoundEvent> DYESPRIA_PAINT = variableRange("item.dyespria.paint");

    private static RegistryObject<SoundEvent> variableRange(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(MoreSnifferFlowers.loc(name)));
    }
}
