package net.abraxator.moresnifferflowers.blocks;

import com.mojang.datafixers.util.Pair;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.StreamSupport;

public class GiantCropBlock extends Block implements ModEntityBlock, Bonmeelable {
    public static final EnumProperty<ModelPos> MODEL_POSITION = EnumProperty.create("model_pos", ModelPos.class);

    public GiantCropBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(MODEL_POSITION, ModelPos.NONE));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pLevel.getBlockEntity(pPos) instanceof GiantCropBlockEntity entity) {
            entity.canGrow = true;
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(!pState.getValue(MODEL_POSITION).equals(ModelPos.NONE)) {
            pLevel.getBlockTicks().schedule(new ScheduledTick<>(this, pPos, pLevel.getGameTime() + 7, pLevel.nextSubTickCount()));
            if(pState.getValue(MODEL_POSITION).equals(ModelPos.NED) && pLevel instanceof ServerLevel level) {
                level.sendParticles(ModParticles.GIANT_CROP.get(), pPos.getCenter().x - 1, pPos.getCenter().y - 1, pPos.getCenter().z + 1, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(MODEL_POSITION);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GiantCropBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return tickerHelper(pLevel);
    }

    @Override
    public void performBonmeel(BlockPos blockPos, BlockState blockState, Level level, @Nullable Player player) {
        Iterable<BlockPos> blockPosList = BlockPos.betweenClosed(
                blockPos.getX() - 1,
                blockPos.getY() - 0,
                blockPos.getZ() - 1,
                blockPos.getX() + 1,
                blockPos.getY() + 2,
                blockPos.getZ() + 1
        );
        
        blockPosList.forEach(pos -> {
            pos = pos.immutable();
            level.destroyBlock(pos, false);
            level.setBlockAndUpdate(pos, cropMap().get(blockState.getBlock()).getFirst().defaultBlockState().setValue(GiantCropBlock.MODEL_POSITION, evaulateModelPos(pos, blockPos)));
            if(level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
                entity.pos1 = blockPos.mutable().move(1, 2, 1);
                entity.pos2 = blockPos.mutable().move(-1, 0, -1);
            }
        });

        if(!player.getAbilities().instabuild) {
            player.getMainHandItem().shrink(1);
        }
        
        if(player instanceof ServerPlayer serverPlayer) {
            ModAdvancementCritters.USED_BONMEEL.trigger(serverPlayer);
        }
        
        level.playLocalSound(blockPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @Override
    public boolean canBonmeel(BlockPos blockPos, BlockState bonmeeledState, Level level) {
        Iterable<BlockPos> blockPosList = BlockPos.betweenClosed(
                blockPos.getX() - 1,
                blockPos.getY() - 0,
                blockPos.getZ() - 1,
                blockPos.getX() + 1,
                blockPos.getY() + 2,
                blockPos.getZ() + 1
        );
        return StreamSupport.stream(blockPosList.spliterator(), false).allMatch(pos -> {
            BlockState blockState = level.getBlockState(pos);
            int cropY = blockPos.getY();
            var PROPERTY = cropMap().get(bonmeeledState.getBlock()).getSecond().getFirst();
            int MAX_AGE = cropMap().get(bonmeeledState.getBlock()).getSecond().getSecond();

            if(pos.getY() == cropY) {
                return blockState.is(bonmeeledState.getBlock()) && blockState.is(ModTags.ModBlockTags.BONMEELABLE) && blockState.getValue(PROPERTY) == MAX_AGE;
            } else {
                return blockState.canBeReplaced();
            }
        });
    }

    public static Map<Block, Pair<Block, Pair<IntegerProperty, Integer>>> cropMap() {
        return Map.of(
                Blocks.CARROTS, new Pair<>(ModBlocks.GIANT_CARROT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.POTATOES, new Pair<>(ModBlocks.GIANT_POTATO.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.NETHER_WART, new Pair<>(ModBlocks.GIANT_NETHERWART.get(), new Pair<>(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE)),
                Blocks.BEETROOTS, new Pair<>(ModBlocks.GIANT_BEETROOT.get(), new Pair<>(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE)),
                Blocks.WHEAT, new Pair<>(ModBlocks.GIANT_WHEAT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE))
        );
    }
    
    public static GiantCropBlock.ModelPos evaulateModelPos(BlockPos pos, BlockPos posToCompare) {
        var value = GiantCropBlock.ModelPos.NONE;

        posToCompare = posToCompare.above();
        pos = pos.above();

        if(pos.equals(posToCompare.north().east())) {
            value = GiantCropBlock.ModelPos.NEU;
        }
        if(pos.equals(posToCompare.north().west())) {
            value = GiantCropBlock.ModelPos.NWU;
        }
        if(pos.equals(posToCompare.south().east())) {
            value = GiantCropBlock.ModelPos.SEU;
        }
        if(pos.equals(posToCompare.south().west())) {
            value = GiantCropBlock.ModelPos.SWU;
        }

        posToCompare.below(2);
        pos = pos.below(2);

        if(pos.equals(posToCompare.north().east())) {
            value = GiantCropBlock.ModelPos.NED;
        }
        if(pos.equals(posToCompare.north().west())) {
            value = GiantCropBlock.ModelPos.NWD;
        }
        if(pos.equals(posToCompare.south().east())) {
            value = GiantCropBlock.ModelPos.SED;
        }
        if(pos.equals(posToCompare.south().west())) {
            value = GiantCropBlock.ModelPos.SWD;
        }

        return value;
    }

    public static enum ModelPos implements StringRepresentable {
        NED("ned",-0.4992, -0.4992, 1.4992 ),
        NEU("neu",-0.4994, 1.4994, 1.4994 ),
        NWD("nwd",1.4996, -0.4996, 1.4996 ),
        NWU("nwu",1.4998, 1.4998, 1.4998 ),
        SED("sed",-0.5, -0.5, -0.5 ),
        SEU("seu",-0.4990, 1.4990, -0.4990 ),
        SWD("swd",1.4988, -0.4988, -0.4988 ),
        SWU("swu",1.4986, 1.4986, -0.4986 ),
        NONE("none");

        public final String name;
        public final double x;
        public final double y;
        public final double z;

        ModelPos(String name, double x, double y, double z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        ModelPos(String name) {
            this(name, 0, 0, 0);
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}