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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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
        BlockPos.betweenClosedStream(aabb).forEach(pos -> {
            level.setBlockAndUpdate(pos, this.getBlock().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(pContext.getClickedPos().above())));
            if (level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                entity.center = pContext.getClickedPos().above();
            }
        });

        return true;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var aabb = AABB.ofSize(pContext.getClickedPos().getCenter(), 2, 0, 2).setMaxY(3);
        var ret = BlockPos.betweenClosedStream(aabb)
                .allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());

        return ret;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    }
}