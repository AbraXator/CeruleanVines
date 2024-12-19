package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.data.datamaps.Corruptable;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Optional;

public class CorruptedProjectile extends ThrowableItemProjectile {
    public CorruptedProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CorruptedProjectile(Level pLevel, LivingEntity pShooter) {
        super(ModEntityTypes.CORRUPTED_SLIME_BALL.get(), pShooter, pLevel);
    }
    
    public CorruptedProjectile(Level level) {
        super(ModEntityTypes.CORRUPTED_SLIME_BALL.get(), level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CORRUPTED_SLIME_BALL.get();
    }

    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        for (int i = 0; i < 16; i++) {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }
        this.discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 3) {
            this.level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0x36283D).toVector3f(), 1.0F), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if(entity instanceof LivingEntity livingEntity) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if(checkState(this.level().getBlockState(pResult.getBlockPos()))) {
            var pos = pResult.getBlockPos();
            var state = this.level().getBlockState(pos);
            var layer = state.getValue(ModStateProperties.LAYER);
            this.level().setBlockAndUpdate(
                    pos,
                    ModBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(ModStateProperties.LAYER, layer + 1));
        } else if (checkState(this.level().getBlockState(pResult.getBlockPos().above()))) {
            var pos = pResult.getBlockPos().above();
            var state = this.level().getBlockState(pos);
            var layer = state.getValue(ModStateProperties.LAYER);
            this.level().setBlockAndUpdate(
                    pos,
                    ModBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(ModStateProperties.LAYER, layer + 1));
        } else {
            if(!transformBlock(this.level(), pResult.getBlockPos().above())) {
                transformBlock(this.level(), pResult.getBlockPos());
                this.level().setBlockAndUpdate(
                        pResult.getBlockPos().relative(pResult.getDirection()),
                        ModBlocks.CORRUPTED_SLIME_LAYER.get().defaultBlockState().setValue(ModStateProperties.LAYER, 1));
            }
        }
    }
    
    private boolean transformBlock(Level level, BlockPos blockPos) {
        var pos = BlockPos.findClosestMatch(blockPos, 1, 1, blockPos1 -> Corruptable.canBeCorrupted(level.getBlockState(blockPos1).getBlock(), random));
        var state = level.getBlockState(blockPos);
        
        if(Corruptable.canBeCorrupted(state.getBlock(), random)) {
            Optional<Block> optional = Corruptable.getCorruptedBlock(state.getBlock(), this.random);
            optional.ifPresent(block -> {
                if (level.getBlockState(blockPos).getBlock() instanceof net.abraxator.moresnifferflowers.blocks.corrupted.Corruptable corruptable && level instanceof ServerLevel serverLevel) {
                    corruptable.onCorrupt(serverLevel, blockPos, level.getBlockState(blockPos), block);
                } else {
                    level.setBlockAndUpdate(blockPos, block.withPropertiesOf(state));
                }
                level.addParticle(
                        new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F),
                        blockPos.getX() + level.random.nextDouble(), blockPos.getY() + level.random.nextDouble(), blockPos.getZ() + level.random.nextDouble(),
                        0.0D, 0.0D, 0.0D);
            });

            return true;
        }
        
        return false;
    }
    
    private static boolean checkState(BlockState state) {
        return state.is(ModBlocks.CORRUPTED_SLIME_LAYER) && state.getValue(ModStateProperties.LAYER) != 8;
    }
}
