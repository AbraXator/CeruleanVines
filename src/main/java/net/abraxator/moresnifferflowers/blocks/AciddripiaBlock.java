package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class AciddripiaBlock extends BondripiaBlock {
    public AciddripiaBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!isMaxAge(pState)) {
            grow(pLevel, pPos);
        } else if (pRandom.nextDouble() <= 0.33D && pLevel.getBlockEntity(pPos) instanceof BondripiaBlockEntity entity) {
            var aabb = new AABB(entity.center.below()).inflate(1.5D, 0, 1.5D).setMaxY(10.0D);
            pLevel.getEntities((Entity) null, aabb, entity1 -> entity1.getType() == EntityType.PLAYER)
                    .stream().map(entity1 -> ((Player) entity1))
                    .forEach(entity1 ->{
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
                    if(state.is(BlockTags.LEAVES)) {
                        pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                    } else if (pLevel.getBlockState(blockPos).getBlock() instanceof AbstractCauldronBlock) {
                        fillCauldron(pLevel, blockPos, pLevel.getBlockState(blockPos));
                    }
                    
                    blockPos = blockPos.below();
                }
            }
        }
    }

    private Optional<Property<?>> getProperty(BlockPos pos, Level level) {
        return level.getBlockState(pos).getProperties().stream()
                .filter(property -> property.getName().contains("age") && property instanceof IntegerProperty)
                .findAny();
    }
}
