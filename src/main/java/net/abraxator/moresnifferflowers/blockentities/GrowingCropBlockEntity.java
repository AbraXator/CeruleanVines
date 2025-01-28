package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class GrowingCropBlockEntity extends ModBlockEntity {
    public float growProgress;
    public boolean hasGrown;
    public final float growRate;
    
    public GrowingCropBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, float growRate) {
        super(pType, pPos, pBlockState);
        this.growRate = growRate;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void tick(Level level) {
        if(canGrow(this.growProgress, this.hasGrown)) {
            this.growProgress += growRate;
            this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            if(this.growProgress >= 1) {
                this.onGrow(getBlockPos(), getBlockState(), getLevel());
            }
        }
    }

    public boolean canGrow(float growProgress, boolean hasGrown) {
        return !(growProgress >= 1) && !hasGrown;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    public void onGrow(BlockPos blockPos, BlockState state, Level level) {
        this.hasGrown = true;;
        this.level.sendBlockUpdated(blockPos, state, state, Block.UPDATE_CLIENTS);
    }

    public void reset() {
        this.growProgress = 0;
        this.hasGrown = false;
        //level.setBlock(blockPos, state.setValue(AmbushBlock.AGE, 7), 3);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.hasGrown = pTag.getBoolean("hasGrown");
        this.growProgress = pTag.getFloat("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("hasGrown", this.hasGrown);
        pTag.putFloat("progress", this.growProgress);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
