package net.abraxator.moresnifferflowers.entities.goals;

import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ScheduledTick;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class BoblingGiantCropGoal extends Goal {
    private final BoblingEntity bobling;
    private final int searchArea;
    private final double speed;
    private Vec3 wantedPos;
    private int searchCooldown;
    private int randomInterval;
    private long lastFarmSearchTime;

    public BoblingGiantCropGoal(BoblingEntity bobling, int searchArea, double speed) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.bobling = bobling;
        this.searchArea = searchArea;
        this.speed = speed;
        this.randomInterval = reducedTickDelay(10);

        if(this.bobling.getWantedPos() != null && this.bobling.level().getBlockState(this.bobling.getWantedPos().below()).is(Blocks.FARMLAND)) {
            this.wantedPos = this.bobling.getWantedPos().getCenter();
        }
    }

    @Override
    public boolean canUse() {
        return checkFarm(findFarm());
    }

    private boolean gonnaPlant() {
        return this.wantedPos != null && this.wantedPos.closerThan(this.bobling.position(), 1.5);
    }

    private boolean checkFarm(Optional<BlockPos> pos) {
        if(randomInterval > 0 && this.bobling.getRandom().nextInt(randomInterval + (this.bobling.getWantedPos() != null ? 5 : 0)) != 0) {
            long currentTime = this.bobling.level().getGameTime();
            if (this.searchCooldown <= 0 && (lastFarmSearchTime == 0 || currentTime - lastFarmSearchTime > 100)) {
                pos = findFarm();
                this.searchCooldown = 100;
                lastFarmSearchTime = currentTime;
            }

            pos.ifPresent(blockPos -> {
                this.wantedPos = blockPos.getCenter();
                this.bobling.setWantedPos(Optional.of(BlockPos.containing(this.wantedPos)));
                this.bobling.getNavigation().moveTo(this.wantedPos.x, this.wantedPos.y, this.wantedPos.z, this.speed);
            });
        }

        return wantedPos != null;
    }

    private Optional<BlockPos> findFarm() {
        var level = this.bobling.level();
        var entityAABB = new AABB(BlockPos.containing(bobling.position())).inflate(searchArea, 3, searchArea);
        List<BlockPos> nearbyFarmlandsPos = BlockPos.betweenClosedStream(entityAABB).filter(blockPos -> {
            BlockState blockState = level.getBlockState(blockPos);
            return blockState.is(Blocks.FARMLAND);
        }).toList();

        if(nearbyFarmlandsPos.isEmpty()) {
            return Optional.empty();
        }

        Optional<BlockPos> randomFarmlandPos = nearbyFarmlandsPos.stream().findAny();
        var aabb = AABB.ofSize(randomFarmlandPos.get().getCenter(), 2, 0, 2).expandTowards(0, 3, 0);
        var flag = BlockPos.betweenClosedStream(aabb).allMatch(blockPos1 -> {
            if(blockPos1.getY() == randomFarmlandPos.get().getY()) {
                return level.getBlockState(blockPos1).is(Blocks.FARMLAND);
            } else {
                return level.getBlockState(blockPos1).is(Blocks.AIR);
            }
        });

        return flag ? randomFarmlandPos : Optional.empty();
    }

    @Override
    public boolean canContinueToUse() {
        var flag = true;
        if(this.bobling.level().getGameTime() % 50 == 0) {
            flag = checkFarm(Optional.empty());
        }
        return !gonnaPlant() && flag;
    }

    @Override
    public void stop() {
        if(gonnaPlant()) {
            var giantBlock = Util.getRandom(List.of(ModBlocks.GIANT_CARROT, ModBlocks.GIANT_BEETROOT, ModBlocks.GIANT_NETHERWART, ModBlocks.GIANT_POTATO, ModBlocks.GIANT_WHEAT), this.bobling.getRandom());
            var aabb = AABB.ofSize(wantedPos.add(0, 2, 0), 1, 1, 1);
            var level = this.bobling.level();

            BlockPos.betweenClosedStream(aabb).forEach(pos -> {
                level.setBlockAndUpdate(pos, giantBlock.get().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(BlockPos.containing(this.wantedPos.add(0, 1, 0)))));
                if (level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                    entity.pos1 = BlockPos.containing(this.wantedPos.add(0, 1, 0)).mutable().move(1, 2, 1);
                    entity.pos2 = BlockPos.containing(this.wantedPos.add(0, 1, 0)).mutable().move(-1, 0, -1);
                    entity.state = 2;
                }

                var tick = new ScheduledTick<>(level.getBlockState(pos).getBlock(), pos, level.getGameTime() + 50, level.nextSubTickCount());
                level.getBlockTicks().schedule(tick);
                this.bobling.remove(Entity.RemovalReason.DISCARDED);
            });
        }
    }

    @Override
    public void tick() {
        if(this.searchCooldown >= 100) {
            this.searchCooldown--;
        }
    }
}