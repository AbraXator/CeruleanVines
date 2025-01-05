package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addSound(ModSoundEvents.CROPRESSOR_BELT.get(), "Cropressor Belt");
    }
    
    private void addSound(SoundEvent event, String translation) {
        add("sound.moresnifferflowers." + event.getLocation().getPath(), translation);
    }
}
