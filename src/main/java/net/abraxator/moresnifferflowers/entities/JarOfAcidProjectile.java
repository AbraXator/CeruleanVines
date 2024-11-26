package net.abraxator.moresnifferflowers.entities;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockAgeProcessor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JarOfAcidProjectile extends ThrowableItemProjectile {
    private static final Logger log = LoggerFactory.getLogger(JarOfAcidProjectile.class);
    private List<BlockPos> blockPos;
    
    public JarOfAcidProjectile(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
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
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if(entity instanceof LivingEntity livingEntity) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        float r = 5.0F;
        for (float theta = 0; theta < 2 * Mth.PI; theta += 0.2F) {
            for (float phi = 0; phi < Mth.PI; phi += 0.2F) {
                float x = (float) (this.getX() + r * Mth.cos(theta) * Mth.sin(phi));
                float y = (float) (this.getY() + r * Mth.sin(theta) * Mth.sin(phi));
                float z = (float) (this.getZ() + r * Mth.cos(theta));
                BlockPos blockPos1 = BlockPos.containing(x, y, z);
                BlockState blockState = level().getBlockState(blockPos1);
                if(!blockState.is(Blocks.AIR)) {
                    Vec3 vec3 = new Vec3(blockPos1.getX() + random.nextDouble(), blockPos1.getY() + random.nextDouble(), blockPos1.getZ() + random.nextDouble())
                    this.level().addParticle(new DustParticleOptions(Vec3.fromRGB24(0xa1e820).toVector3f(), 1), vec3.x, vec3.y, vec3.z, 0, 0, 0);
                    FallingBlockEntity block = new FallingBlockEntity(EntityType.FALLING_BLOCK, level());
                    block.state
                }
            }
        };
    }
}
