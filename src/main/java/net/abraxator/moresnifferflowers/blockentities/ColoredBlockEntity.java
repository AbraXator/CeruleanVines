package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ColoredBlockEntity extends ModBlockEntity implements Colorable {
    public Dye dye = Dye.EMPTY;

    public ColoredBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        return null;
    }

    public Dye removeDye() {
        var ret = dye;
        dye = Dye.EMPTY;
        setChanged();
        return ret;
    }

    @Override
    public void onAddDye(@Nullable ItemStack destinationStack, ItemStack dyeStack, int amount) {
        this.dye = Dye.getDyeFromDyeStack(dyeStack);
        setChanged();
    }

    @Override
    public void setChanged() {
        BlockState blockState = getBlockState().setValue(ModStateProperties.COLOR, dye.color());

        if(dye.isEmpty()) {
            blockState.setValue(ModStateProperties.COLOR, DyeColor.WHITE);
        }

        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        level.setBlockAndUpdate(getBlockPos(), blockState);

        super.setChanged();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        dye = Dye.EMPTY;
        this.dye = new Dye(DyeColor.byId(pTag.getInt("dyeId")), pTag.getInt("amount"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("dyeId", dye.color().getId());
        pTag.putInt("amount", dye.amount());
    }

    @Override
    public CompoundTag getUpdateTag() {
        super.getUpdateTag();
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}