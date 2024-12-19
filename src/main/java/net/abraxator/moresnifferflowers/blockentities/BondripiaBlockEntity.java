package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

public class BondripiaBlockEntity extends ModBlockEntity {
    public BlockPos center;
    
    public BondripiaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BONDRIPIA.get(), pPos, pBlockState);
        this.center = pPos;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("center", NbtUtils.writeBlockPos(this.center));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.center = NbtUtils.readBlockPos(pTag, "center").orElseGet(this::getBlockPos);
    }
}
