package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
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
        
        if(blockState.is(ModBlocks.VIVICUS_SAPLING) && !blockState.getValue(ModStateProperties.VIVICUS_CURED)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.VIVICUS_CURED, true));

            var particle = new DustParticleOptions(Vec3.fromRGB24(7118872).toVector3f(), 1);
            for(int i = 0; i <= 10; i++) {
                level.addParticle(particle, blockPos.getX() + random.nextDouble(), blockPos.getY() + random.nextDouble(), blockPos.getZ() + random.nextDouble(), 0, -0.3, 0);
            }
            
            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancementCritters.USED_CURE.trigger(serverPlayer);
            }
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        if(blockState.is(ModBlocks.CORRUPTED_SLUDGE)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(ModStateProperties.CURED, true));
        }
        
        return super.useOn(pContext);
    }
}
