package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class VivicusAntidoteItem extends Item {
    public VivicusAntidoteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var blockPos = pContext.getClickedPos();
        var blockState = level.getBlockState(blockPos);
        var random = level.getRandom();
        var player = pContext.getPlayer();
        
        if(blockState.is(ModBlocks.VIVICUS_SAPLING.get()) && blockState.getValue(ModStateProperties.VIVICUS_TYPE) != BoblingEntity.Type.CURED) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.VIVICUS_TYPE, BoblingEntity.Type.CURED));

            var particle = new DustParticleOptions(Vec3.fromRGB24(7118872).toVector3f(), 1);
            for(int i = 0; i <= 10; i++) {
                level.addParticle(particle, blockPos.getX() + random.nextDouble(), blockPos.getY() + random.nextDouble(), blockPos.getZ() + random.nextDouble(), 0, -0.3, 0);
            }
            
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, pContext.getItemInHand());
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        if(blockState.is(ModBlocks.CORRUPTED_SLUDGE.get())) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.CURED, true));
        }
        
        return super.useOn(pContext);
    }
}
