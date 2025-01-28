package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.Comparator;
import java.util.List;

public class DragonflyProjectile extends ThrowableItemProjectile {
    public DragonflyProjectile(EntityType<? extends DragonflyProjectile> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    public DragonflyProjectile(Level pLevel, Player player) {
        super(ModEntityTypes.DRAGONFLY.get(), player, pLevel);
        this.setOwner(player);
    }
    
    public DragonflyProjectile(Level level) {
        super(ModEntityTypes.DRAGONFLY.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        /*double range = 15;
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, AABB.ofSize(this.position(), range, range, range), livingEntity1 -> !(livingEntity1 instanceof Player));
        if(!entities.isEmpty()) {
            LivingEntity livingEntity = entities.getFirst();
            double distance = this.distanceTo(livingEntity);
            Vec3 dir = new Vec3(livingEntity.getX() - this.getX(), livingEntity.getY() - this.getY(), livingEntity.getZ() - this.getZ());
            if (distance > 0) {
                dir.normalize();
                Vec3 deltaMov = new Vec3(dir.x / distance, dir.y / distance, dir.z / distance);
                this.addDeltaMovement(deltaMov.normalize());
            }
        }*/
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRAGONFLY.get();
    }

    @Override
    protected void onHit(HitResult pResult) {
        if(level() instanceof ServerLevel serverLevel) {
            var particle = new ItemParticleOption(ParticleTypes.ITEM, ModItems.DRAGONFLY.get().getDefaultInstance());
            serverLevel.sendParticles(particle, getX(), getY(), getZ(), 10, Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), Mth.nextDouble(random, 0, 0.3), 0);
        }
        super.onHit(pResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if(pResult.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 140, 2));
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.3F);
        }
        
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        discard();
    }
}
