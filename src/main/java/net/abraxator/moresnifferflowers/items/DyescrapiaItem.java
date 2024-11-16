package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class DyescrapiaItem extends BlockItem {
    public DyescrapiaItem(Properties pProperties) {
        super(ModBlocks.DYESCRAPIA_PLANT.get(), pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var pos = pContext.getClickedPos();
        var state = pContext.getLevel().getBlockState(pos);
        var player = pContext.getPlayer();
        var level = pContext.getLevel();
        
        if(state.getBlock() instanceof Colorable colorable) {
            var dye = new Dye(colorable.getDyeFromBlock(state).color(), 1);
            if(!dye.color().equals(DyeColor.WHITE)) {
                colorable.colorBlock(level, pos, state, new Dye(DyeColor.WHITE, 1));
                player.addItem(Dye.stackFromDye(dye));
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }            
        }
        
        return handlePlacement(pos, level, player, pContext.getHand(), pContext.getItemInHand());
    }

    private InteractionResult handlePlacement(BlockPos blockPos, Level level, Player player, InteractionHand hand, ItemStack stack) {
        var posForDyespria = blockPos.above();
        var blockHitResult = new BlockHitResult(posForDyespria.below().getCenter(), Direction.UP, posForDyespria.below(), false);
        var useOnCtx = new UseOnContext(level, player, hand, stack, blockHitResult);
        var result = super.useOn(useOnCtx);

        if (level.getBlockEntity(blockPos.above()) instanceof DyespriaPlantBlockEntity entity) {
            entity.dye = Dye.getDyeFromStack(stack);
            entity.setChanged();
        }

        return result;
    }
}
