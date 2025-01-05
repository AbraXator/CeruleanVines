package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    protected ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MoreSnifferFlowers.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.CROPRESSOR_BELT, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.CROPRESSOR_BELT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_1")))
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_2")))
        );
        add(ModSoundEvents.DYESPRIA_PAINT, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.DYESPRIA_PAINT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("dyespria_paint"))));
    }
}
