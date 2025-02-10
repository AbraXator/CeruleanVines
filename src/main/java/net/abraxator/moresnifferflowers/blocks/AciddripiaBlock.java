package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AciddripiaBlock extends BondripiaBlock {
    public AciddripiaBlock(Properties p_49795_) {
        super(p_49795_);
    }
    
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof BondripiaBlockEntity entity) {
            if (!isMaxAge(pState)) {
                grow(pLevel, pPos);
            } else if (pRandom.nextDouble() <= 0.33D) {
                var aabb = new AABB(entity.center.below()).inflate(1.5D, 0, 1.5D).setMaxY(10.0D);
                pLevel.getEntities((Entity) null, aabb, entity1 -> entity1.getType() == EntityType.PLAYER)
                        .stream().map(entity1 -> ((Player) entity1))
                        .forEach(entity1 -> {
                            entity1.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1));
                        });
                for (BlockPos currentPos : BlockPos.betweenClosed(entity.center.below().north().east(), entity.center.below().south().west())) {
                    BlockPos blockPos = currentPos;

                    for (int i = 0; i < 10; i++) {
                        if (getProperty(blockPos, pLevel).isPresent()) {
                            BlockState state = pLevel.getBlockState(blockPos);
                            state = state.setValue((IntegerProperty) getProperty(blockPos, pLevel).get(), 0);
                            pLevel.setBlock(blockPos, state, 2);
                        }

                        BlockState state = pLevel.getBlockState(blockPos);
                        if (state.is(BlockTags.LEAVES)) {
                            pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                        } else if (pLevel.getBlockState(blockPos).getBlock() instanceof AbstractCauldronBlock) {
                            fillCauldron(pLevel, blockPos, this.defaultBlockState());
                        } else if (state.is(BlockTags.DIRT) && !state.is(Blocks.DIRT)) {
                            pLevel.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 2);
                        }

                        blockPos = blockPos.below();
                    }
                }
            }
        }
    }

    public static void afterCorruption(BlockPos centrePos, Level level, BlockPos pos){
        level.setBlockAndUpdate(pos, ModBlocks.ACIDRIPIA.get().withPropertiesOf(level.getBlockState(pos)));
        if(level.getBlockEntity(pos) instanceof BondripiaBlockEntity entity){
            entity.center = centrePos;
        }
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(state.getValue(ModStateProperties.CENTER) && isMaxAge(state) && level.getBlockEntity(pos) instanceof BondripiaBlockEntity entity && random.nextFloat() < 0.4) {
            BlockPos.withinManhattanStream(entity.center, 1, 0, 1).forEach(blockPos -> {

                var vec3 = blockPos.getCenter();
                var difference = blockPos.subtract(entity.center);
                boolean isCorner = (Math.abs(difference.getX()) + Math.abs(difference.getZ())) == 2;
                boolean isMid = blockPos.equals(entity.center);
                BlockPos pos2 = blockPos.below(random.nextInt(8));

                if (random.nextFloat() < 0.2) {
                    if (isMid) {
                        level.addParticle(ModParticles.ACIDRIPIA_DRIP.get(), vec3.x + random.nextIntBetweenInclusive(-1, 1) * 0.15, vec3.y - 0.25, vec3.z + random.nextIntBetweenInclusive(-1, 1) * 0.15, 0, 1, 0);

                    } else if (isCorner) {
                        level.addParticle(ModParticles.ACIDRIPIA_FALL.get(), vec3.x - difference.getX() * 0.05, vec3.y, vec3.z - difference.getZ() * 0.05, 0, 1, 0);

                    } else {
                        level.addParticle(ModParticles.ACIDRIPIA_DRIP.get(), vec3.x - difference.getX() * 0.1, vec3.y, vec3.z - difference.getZ() * 0.1, 0, 1, 0);
                    }
                }
                if (level.getBlockState(pos2).isSolid() && level.getBlockState(pos2.below()).isAir()){
                    ParticleUtils.spawnParticleBelow(level, pos2, random, ModParticles.ACIDRIPIA_DRIP.get());
                }
            });
        }
    }

    private Optional<Property<?>> getProperty(BlockPos pos, Level level) {
        return level.getBlockState(pos).getProperties().stream()
                .filter(property -> property.getName().contains("age") && property instanceof IntegerProperty)
                .findAny();
    }
}
