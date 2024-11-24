package net.abraxator.moresnifferflowers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
  
public class CorruptedSlimeLayerBlock extends SnowLayerBlock {
    public CorruptedSlimeLayerBlock(Properties p_56585_) {
        super(p_56585_);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0, 0, 0, 16, Math.max((pState.getValue(LAYERS) - 3) * 2, 0), 16);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        pEntity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        showParticles(pEntity, 10);

        if (pEntity.causeFallDamage(pFallDistance, 0.2F, pLevel.damageSources().fall())) {
            pEntity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        double d0 = Math.abs(pEntity.getDeltaMovement().y);
        if (d0 < 0.1 && !pEntity.isSteppingCarefully()) {
            double d1 = (double) 1 / pState.getValue(LAYERS) + d0 * 0.2;
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }
    
    private void showParticles(Entity pEntity, int pParticleCount) {
        if (pEntity.level().isClientSide) {
            for (int i = 0; i < pParticleCount; i++) {
                pEntity.level()
                        .addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.defaultBlockState()), pEntity.getX(), pEntity.getY(), pEntity.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

}
