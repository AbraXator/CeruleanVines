package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public interface Corruptable {
    default Optional<Block> getCorruptedBlock(Block block, Level level) {
        return CorruptionRecipe.getCorruptedBlock(block, level);
    }
    
    default void onCorrupt(Level level, BlockPos pos, BlockState oldState, Block corruptedBlock) {
        var corruptedState = corruptedBlock.withPropertiesOf(oldState);
        level.setBlockAndUpdate(pos, corruptedState);
    }
    
    default void onCorruptByEntity(Entity entity, BlockPos blockPos, BlockState blockState, Block block, Level level) {
        if(entity instanceof CorruptedProjectile corruptedProjectile && CorruptionRecipe.canBeCorrupted(block, level)) {
            onCorrupt(level, blockPos, blockState, getCorruptedBlock(block, level).get());
            corruptedProjectile.discard();
            
            if(level.isClientSide) {
                level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0x36283D).toVector3f(), 1.0F), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}
