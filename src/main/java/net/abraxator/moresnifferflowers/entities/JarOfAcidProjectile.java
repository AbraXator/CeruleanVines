package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class JarOfAcidProjectile extends ThrowableItemProjectile {
    private static final Logger log = LoggerFactory.getLogger(JarOfAcidProjectile.class);
    public JarOfAcidProjectile(LivingEntity pShooter, Level pLevel) {
        super(ModEntityTypes.JAR_OF_ACID.get(), pShooter, pLevel);
    }
    
    public JarOfAcidProjectile(Level pLevel) {
        super(ModEntityTypes.JAR_OF_ACID.get(), pLevel);
    }
    
    public JarOfAcidProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.JAR_OF_ACID.get();
    }

    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
    }
    
    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.level().broadcastEntityEvent(this, (byte)3);
        degrowCrops(BlockPos.containing(pResult.getLocation()));
        makeAreaOfEffectCloud();
        
        this.discard();
    }
    
    private void degrowCrops(BlockPos blockPos) {
        BlockPos.withinManhattanStream(blockPos, 2, 1, 2).forEach(pos -> {
            
            getProperty(pos).ifPresent(property -> {
                BlockState state = level().getBlockState(pos);
                state = state.setValue((IntegerProperty) property, 0);
                level().setBlock(pos, state, 2);
            });

            BlockState state = level().getBlockState(pos);
            if(state.is(BlockTags.LEAVES)) {
                level().setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
            if(state.is(BlockTags.DIRT)) {
                level().setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            }
        });
    }

    private Optional<Property<?>> getProperty(BlockPos pos) {
        return level().getBlockState(pos).getProperties().stream()
                .filter(property -> property.getName().contains("age") && property instanceof IntegerProperty)
                .findAny();
    }
    
    private void makeAreaOfEffectCloud() {
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        if (this.getOwner() instanceof LivingEntity livingentity) {
            areaeffectcloud.setOwner(livingentity);
        }

        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setParticle(new DustParticleOptions(Vec3.fromRGB24(0xaeff5c).toVector3f(), 1));
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
        areaeffectcloud.setPotion(new Potion(new MobEffectInstance(MobEffects.POISON, 60, 2)));
        this.level().addFreshEntity(areaeffectcloud);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if(entity instanceof LivingEntity livingEntity) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 2));
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}
