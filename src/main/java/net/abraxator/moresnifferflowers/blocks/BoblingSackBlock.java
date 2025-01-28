package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BoblingSackBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoblingSackBlock extends Block implements ModEntityBlock {
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);
    
    public BoblingSackBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static void spawnSack(ServerLevel serverLevel, BlockPos pos, List<ItemStack> drops) {
        serverLevel.setBlockAndUpdate(pos, ModBlocks.BOBLING_SACK.get().defaultBlockState());
        if(serverLevel.getBlockEntity(pos) instanceof BoblingSackBlockEntity boblingEntity) {

            
            boblingEntity.inventory = NonNullList.withSize(drops.size(), ItemStack.EMPTY);
            for(int i = 0; i < drops.size(); i++) {
                boblingEntity.inventory.set(i, drops.get(i));
            }
        }
    }
    
    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pLevel.getBlockEntity(pPos) instanceof BoblingSackBlockEntity entity) {
            Containers.dropContents(pLevel, pPos, entity.inventory);
        }
        
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BoblingSackBlockEntity(pPos, pState);
    }
}
