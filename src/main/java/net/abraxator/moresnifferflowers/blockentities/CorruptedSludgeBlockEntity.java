package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.networking.CorruptedSludgePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CorruptedSludgeBlockEntity extends ModBlockEntity implements GameEventListener.Holder<CorruptedSludgeBlockEntity.CorruptedSludgeListener> {
    public CorruptedSludgeListener corruptedSludgeListener;
    public int usesLeft;
    public int stateChange;
    public GameEventListener listener;
    
    public CorruptedSludgeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CORRUPTED_SLUDGE.get(), pPos, pBlockState);
        this.corruptedSludgeListener = new CorruptedSludgeListener(new BlockPositionSource(pPos));
        this.usesLeft = Minecraft.getInstance().level.random.nextIntBetweenInclusive(16, 32);
        this.stateChange = usesLeft / 4;
        this.listener = new CorruptedSludgeListener(new BlockPositionSource(pPos));
    }

    public void updateUses() {
        this.usesLeft--;
         
        if(this.usesLeft <= 0) {
            CorruptedSludgeListener.shootProjectiles(this.getBlockPos().getCenter(), this.level.random.nextIntBetweenInclusive(8, 16), this.level);
            this.level.destroyBlock(this.getBlockPos(), true);
            
            return;
        }
        
        if(this.usesLeft % stateChange == 0 && this.getBlockState().getValue(ModStateProperties.USES_4) - 1 != -1) {
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ModStateProperties.USES_4, this.getBlockState().getValue(ModStateProperties.USES_4) - 1));
        }
    }
    
    @Override
    public CorruptedSludgeListener getListener() {
        return corruptedSludgeListener;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("uses", usesLeft);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        usesLeft = tag.getInt("uses");
    }

    public static class CorruptedSludgeListener implements GameEventListener {
        private PositionSource positionSource;

        public CorruptedSludgeListener(PositionSource positionSource) {
            this.positionSource = positionSource;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return GameEvent.BLOCK_DESTROY.getNotificationRadius();
        }

        @Override
        public boolean handleGameEvent(ServerLevel pLevel, GameEvent pGameEvent, GameEvent.Context pContext, Vec3 pPos) {
            CorruptedSludgeBlockEntity entity;

            if(pLevel.getBlockEntity(BlockPos.containing(this.positionSource.getPosition(pLevel).get())) instanceof CorruptedSludgeBlockEntity entity1) {
                entity = entity1;
            } else return false;

            if(entity.usesLeft <= 0 || entity.getBlockState().getValue(ModStateProperties.CURED)) {
                return false;
            }

            if(pGameEvent == GameEvent.BLOCK_PLACE && CorruptionRecipe.canBeCorrupted(pContext.affectedState().getBlock(), pLevel)) {
                Vec3 startPos = this.getListenerSource().getPosition(pLevel).get();
                Vec3 dirNormal = new Vec3(pPos.x - startPos.x, pPos.y - startPos.y, pPos.z - startPos.z).normalize();
                Optional<Block> corrupted = CorruptionRecipe.getCorruptedBlock(pContext.affectedState().getBlock(), pLevel);
                BlockPos blockPos = BlockPos.containing(pPos);
                corrupted.ifPresent(block -> {
                    ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new CorruptedSludgePacket(startPos.toVector3f(), pPos.toVector3f(), dirNormal.toVector3f()));
                    if(pLevel.getBlockState(BlockPos.containing(pPos)).getBlock() instanceof net.abraxator.moresnifferflowers.blocks.Corruptable corruptable) {
                        corruptable.onCorrupt(pLevel, blockPos, pLevel.getBlockState(BlockPos.containing(pPos)), block);
                    } else {
                        pLevel.setBlockAndUpdate(BlockPos.containing(pPos), block.withPropertiesOf(pContext.affectedState()));
                    }
                    pLevel.sendParticles(
                            new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F),
                            blockPos.getX() + pLevel.random.nextDouble(), blockPos.getY() + pLevel.random.nextDouble(), blockPos.getZ() + pLevel.random.nextDouble(),
                            10,
                            0.0D, 0.0D, 0.0D,
                            0.0D
                    );

                    entity.updateUses();
                });

                return corrupted.isPresent();
            }

            if(pGameEvent == GameEvent.BLOCK_DESTROY && pContext.affectedState().is(ModTags.ModBlockTags.CORRUPTED_SLUDGE) && !pPos.equals(this.positionSource.getPosition(pLevel).get()) && pContext.sourceEntity() instanceof Player player) {
                var projectileNumber = pContext.affectedState().is(ModBlocks.CORRUPTED_LEAVES.get()) || pContext.affectedState().is(ModBlocks.CORRUPTED_LEAVES_BUSH.get())  ? pLevel.random.nextInt(1) + 1 : pLevel.random.nextInt(5) + 1;
                shootProjectiles(this.positionSource.getPosition(pLevel).get(), projectileNumber, pLevel);
                entity.updateUses();
                return true;
            }

            return false;
        }
        
        public static void shootProjectiles(Vec3 center, int projectileNumber, Level level) {
            var radius = 2.5;
            Set<Vec3> placed = new HashSet<>();

            for(int i = 0; i < projectileNumber; i++) {
                generatePoint(placed, center, radius, level);
            }
        }

        private static void generatePoint(Set<Vec3> placed, Vec3 center, double radius, Level pLevel) {
            var random = pLevel.random;

            double theta = 2 * Mth.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xg = center.x + radius * Mth.sin((float) phi) * Mth.cos((float) theta);
            double yg = center.y + radius * Mth.sin((float) phi) * Mth.sin((float) theta);
            double zg = center.z + radius * Mth.cos((float) phi);
            var vec3 = new Vec3(xg, yg, zg);
            
            if (placed.stream().noneMatch(vec31 -> AABB.ofSize(vec3, 1, 1, 1).contains(vec31)) && pLevel.getBlockState(BlockPos.containing(vec3)).canBeReplaced()) {
                var pos = center;
                var x = random.nextDouble() * 0.5;
                var y = random.nextDouble() * 0.5;
                var z = random.nextDouble() * 0.5;
                CorruptedProjectile projectile = new CorruptedProjectile(pLevel);
                projectile.setPos(vec3);
                Vec3 dir = new Vec3(projectile.getX() - pos.x, projectile.getY() - pos.y, projectile.getZ() - pos.z).normalize().multiply(x, y, z);
                projectile.setDeltaMovement(dir);
                pLevel.addFreshEntity(projectile);

                //level.sendParticles(ModParticles.CARROT.get(), vec3.x, vec3.y, vec3.z, 1, 0D, 0D, 0D, 0D);
                placed.add(vec3);
            }
        }
    }
}
