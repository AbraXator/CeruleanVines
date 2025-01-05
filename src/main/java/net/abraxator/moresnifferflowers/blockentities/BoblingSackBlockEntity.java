package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BoblingSackBlockEntity extends BlockEntity {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    
    public BoblingSackBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BOBLING_SACK.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
/*        pTag.putInt("inv_size", this.inventory.size());
        ContainerHelper.saveAllItems(pTag, this.inventory, pRegistries);*/
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
/*        this.inventory = NonNullList.withSize(pTag.getInt("inv_size"), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.inventory, pRegistries);*/
    }
}
