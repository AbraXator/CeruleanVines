package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItemProperties;
import net.abraxator.moresnifferflowers.init.ModItemProperties.*;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MoreSnifferFlowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModItems.BOBLING_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        basicItem(ModItems.CORRUPTED_SIGN.get());
        basicItem(ModItems.CORRUPTED_HANGING_SIGN.get());
        basicItem(ModItems.VIVICUS_SIGN.get());
        basicItem(ModItems.VIVICUS_HANGING_SIGN.get());
        basicItem(ModItems.CORRUPTED_BOAT.get());
        basicItem(ModItems.CORRUPTED_CHEST_BOAT.get());
        basicItem(ModItems.VIVICUS_BOAT.get());
        basicItem(ModItems.VIVICUS_CHEST_BOAT.get());

        /*for(int i = 1; i <= ModItemProperties.COPRESSOR_ANIMATION_FRAMES; i++) {
            withExistingParent(ModItems.CROPRESSOR.getId().getPath() + "_animation_" + i, MoreSnifferFlowers.loc("item/cropressor_animation_" + i / ModItemProperties.FRAME_TIME));
            
            modelBuilder.override(i)
                    .predicate(MoreSnifferFlowers.loc("item/cropressor_animation_" + i / ModItemProperties.FRAME_TIME), (float) ((double) 1 / i))
                    .model(new ModelFile.ExistingModelFile(MoreSnifferFlowers.loc("item/cropressor_animation_" + i / ModItemProperties.FRAME_TIME), existingFileHelper));
        }*/
    }
}
