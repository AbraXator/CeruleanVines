package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GiantCropItem extends BlockItem {
    public GiantCropItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        var level = pContext.getLevel();
        var aabb = AABB.ofSize(pContext.getClickedPos().above(1).getCenter(), 2, 2, 2);
        var clickedPos = pContext.getClickedPos();
        
        BlockPos.betweenClosedStream(aabb).forEach(pos -> {
            pos = pos.immutable();
            level.destroyBlock(pos, false);
            level.setBlockAndUpdate(
                    pos, 
                    GiantCropBlock.cropMap().get(this.getBlock()).getFirst().defaultBlockState().setValue(GiantCropBlock.MODEL_POSITION, GiantCropBlock.evaulateModelPos(pos, clickedPos)));
            if(level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                entity.pos1 = clickedPos.mutable().move(1, 2, 1);
                entity.pos2 = clickedPos.mutable().move(-1, 0, -1);
            }
            
            if(!pState.getValue(GiantCropBlock.MODEL_POSITION).equals(GiantCropBlock.ModelPos.NONE)) {
                level.getBlockTicks().schedule(new ScheduledTick<>(level.getBlockState(pos).getBlock(), pos, level.getGameTime() + 7, level.nextSubTickCount()));
                if(pState.getValue(GiantCropBlock.MODEL_POSITION).equals(GiantCropBlock.ModelPos.NED) && level.isClientSide) {
                    level.addParticle(ModParticles.GIANT_CROP.get(), pos.getCenter().x - 1, pos.getCenter().y - 1, pos.getCenter().z + 1, 0, 0, 0);
                }
            }
        });
        

        
        return true;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) { 
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var aabb = AABB.ofSize(pContext.getClickedPos().above(2).getCenter(), 2, 2, 2);
        
        return BlockPos.betweenClosedStream(aabb).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        
        pTooltipComponents.add(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    }
}
