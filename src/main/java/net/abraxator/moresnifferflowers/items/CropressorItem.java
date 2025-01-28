package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.components.CropressorAnimation;
import net.abraxator.moresnifferflowers.init.ModDataComponents;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class CropressorItem extends ItemNameBlockItem {
    public CropressorItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    
    @Override
    public void onCraftedPostProcess(ItemStack pStack, Level pLevel) {
        pStack.set(ModDataComponents.CROPRESSOR_ANIMATE, new CropressorAnimation(true, 0));
    }
}
