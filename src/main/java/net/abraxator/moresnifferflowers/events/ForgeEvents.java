package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public void onVanillaGame(VanillaGameEvent event) {
    }


    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        Vec3 loc = livingEntity.position();
        BlockPos blockPos = BlockPos.containing(loc);
        
        if(level.getBlockState(blockPos).is(ModBlocks.CORRUPTED_SLIME_LAYER.get()) || level.getBlockState(blockPos.below()).is(ModBlocks.CORRUPTED_SLIME_LAYER.get())) {
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().multiply(1, 0.3, 1));
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        var itemStack = event.getEntity().getItemInHand(event.getHand()).getItem().getDefaultInstance();
        var block = event.getLevel().getBlockState(event.getPos());

        if(itemStack.getItem() instanceof JarOfBonmeelItem item && block.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            item.useOn(new UseOnContext(event.getEntity(), event.getHand(), event.getHitVec()));
        } else if((itemStack.is(ModItems.REBREWED_POTION.get()) || itemStack.is(ModItems.EXTRACTED_BOTTLE.get())) && block.is(Blocks.DIRT)) {
            event.setCanceled(true);
        } else if(itemStack.is(ItemTags.AXES) && (block.is(ModBlocks.VIVICUS_LOG.get()) || block.is(ModBlocks.VIVICUS_WOOD.get()))) {
            var strippedBlock = AxeItem.STRIPPABLES.get(block.getBlock());
            var state = strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, block.getValue(RotatedPillarBlock.AXIS))
                    .setValue(ModStateProperties.COLOR, block.getValue(ModStateProperties.COLOR));

            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, event.getPos(), itemStack);
            }

            event.getLevel().setBlock(event.getPos(), state, 3);
            event.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(event.getEntity(), state));
            itemStack.hurtAndBreak(1, event.getEntity(), player -> {
                player.broadcastBreakEvent(player.getUsedItemHand());
            });

            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
        }
    }
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getState().is(ModBlocks.AMBER_BLOCK.get()) && event.getLevel() instanceof ServerLevel serverLevel) {
            fireFlyLogic(event.getState(), serverLevel, event.getPos(), event.getPlayer(), event);
        }

        if(event.getLevel().getBlockEntity(event.getPos()) instanceof GiantCropBlockEntity entity) {
            BlockPos.withinManhattanStream(entity.center, 1, 1, 1).forEach(blockPos -> {
                event.getLevel().destroyBlock(blockPos, true);
            });
        }
        
        if (event.getLevel().getBlockEntity(event.getPos()) instanceof BondripiaBlockEntity entity) {
            Direction.Plane.HORIZONTAL.forEach(direction -> {
                BlockPos blockPos = entity.center.relative(direction);

                event.getLevel().destroyBlock(blockPos, true);
            });
            event.getLevel().destroyBlock(entity.center, true);
        }
    }

    @SubscribeEvent
    public static void onGetAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if(event.getAdvancement().getId().equals(ResourceLocation.tryParse("husbandry/obtain_sniffer_egg")) && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ModAdvancementCritters.EARN_SNIFFER_ADVANCEMENT.trigger(serverPlayer);
        }
    }

    private static void fireFlyLogic(BlockState state, ServerLevel serverLevel, BlockPos pos, Player player, Event event) {
        List<ItemStack> list = Block.getDrops(state, serverLevel, pos, serverLevel.getBlockEntity(pos), player, player.getMainHandItem());
        RandomSource randomSource = serverLevel.getRandom();
        if ((randomSource.nextInt(list.size())) == list.size()) {
            event.setCanceled(true);
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                   10, 0, 0, 0, 0);
            Vec3 vecPos = pos.getCenter();
            serverLevel.sendParticles(
                    ModParticles.FLY.get(),
                    vecPos.x() + (randomSource.nextInt(10) - 5) / 10,
                    vecPos.y() + (randomSource.nextInt(10) - 5) / 10,
                    vecPos.z() + (randomSource.nextInt(10) - 5) / 10,
                    0, 0, 0, 0, 0);
        }
    }
}
