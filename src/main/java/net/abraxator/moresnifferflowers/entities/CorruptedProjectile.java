package net.abraxator.moresnifferflowers.entities;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class CorruptedProjectile extends ThrowableItemProjectile {
    public static final Map<Block, Block> TRANSFORMED_BLOCKS = Util.make(Maps.newHashMap(), map -> {
        map.put(Blocks.DIRT, Blocks.COARSE_DIRT);
        map.put(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT);
        map.put(Blocks.SAND, Blocks.RED_SAND);
        map.put(Blocks.STONE, Blocks.NETHERRACK);
        map.put(ModBlocks.DYESPRIA_PLANT.get(), ModBlocks.DYESCRAPIA_PLANT.get());
        map.put(ModBlocks.DAWNBERRY_VINE.get(), ModBlocks.GLOOMBERRY_VINE.get());
    });
    
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
        for (int i = 0; i < 8; i++) {
            this.level().addParticle(this.getParticle(), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
        this.discard();
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
        var pos = BlockPos.findClosestMatch(blockPos, 1, 1, blockPos1 -> canBeTransformed(level.getBlockState(blockPos1)));
        var state = level.getBlockState(blockPos);
        
        if(canBeTransformed(state)) {
            var transformedBlock = TRANSFORMED_BLOCKS.getOrDefault(state.getBlock(),
                    state.is(BlockTags.LOGS) ? ModBlocks.DECAYED_LOG.get() : state.getBlock());

            level.setBlock(blockPos, transformedBlock.withPropertiesOf(state), 3);
            
            return true;
        }
        
        return false;
    }
    
    private static boolean canBeTransformed(BlockState state) {
        return TRANSFORMED_BLOCKS.containsKey(state.getBlock()) || state.is(BlockTags.LOGS);
    }
    
    private static boolean checkState(BlockState state) {
        return state.is(ModBlocks.CORRUPTED_SLIME_LAYER) && state.getValue(ModStateProperties.LAYER) != 8;
    }
}
