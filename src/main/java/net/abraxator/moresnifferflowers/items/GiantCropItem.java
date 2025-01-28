package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GiantCropItem extends BlockItem {
    public GiantCropItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        var level = pContext.getLevel();
        var aabb = AABB.ofSize(pContext.getClickedPos().above(1).getCenter(), 2, 2, 2);
        BlockPos.betweenClosedStream(aabb).forEach(pos -> {
            level.setBlockAndUpdate(pos, this.getBlock().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(pContext.getClickedPos().above())));
            if(level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                entity.pos1 = pContext.getClickedPos().mutable().move(1, 2, 1);
                entity.pos2 = pContext.getClickedPos().mutable().move(-1, 0, -1);
            }
        });
        
        return true;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) { 
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var aabb = AABB.ofSize(pContext.getClickedPos().above(2).getCenter(), 2, 2, 2);
        var ret = BlockPos.betweenClosedStream(aabb)
                .allMatch(blockPos -> level.getBlockState(blockPos).isEmpty());
        
        return ret;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        
        pTooltipComponents.add(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    }
}
