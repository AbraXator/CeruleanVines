package net.abraxator.moresnifferflowers.blocks.cropressor;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CropressorBlockBase extends HorizontalDirectionalBlock {
    public final Part PART;
    protected BlockPos ENTITY_POS;
    protected static final VoxelShape OUT_EAST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_SOUTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_WEST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape OUT_NORTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_EAST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_SOUTH = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_WEST = Block.box(0, 0, 0, 16, 11, 16);
    protected static final VoxelShape CENTER_NORTH = Block.box(0, 0, 0, 16, 11, 16);
    public static final MapCodec<CropressorBlockBase> CODEC = simpleCodec(properties1 -> new CropressorBlockBase(properties1, Part.CENTER));
    
    public CropressorBlockBase(Properties pProperties, Part part) {
        super(pProperties);
        PART = part;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = getConnectedDirection(pState);
        if(pState.getBlock() instanceof CropressorBlockOut) {
            return switch (direction) {
                case EAST -> OUT_EAST;
                case SOUTH -> OUT_SOUTH;
                case WEST -> OUT_WEST;
                default -> OUT_NORTH;
            };
        } else {
            return switch (direction) {
                case EAST -> CENTER_EAST;
                case SOUTH -> CENTER_SOUTH;
                case WEST -> CENTER_WEST;
                default -> CENTER_NORTH;
            };
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    private Direction getNeighbourDirection(Part part, Direction direction) {
        return part == Part.OUT ? direction : direction.getOpposite();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if(pDirection == getNeighbourDirection(PART, pState.getValue(FACING))) {
            var b = pNeighborState.getBlock() instanceof CropressorBlockBase;
            var b1 = getPartFromState(pNeighborState) != PART;
            if(b && b1) {
                return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        }
        
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
            
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockPos = pContext.getClickedPos();
        BlockPos blockPos1 = blockPos.relative(direction);
        Level level = pContext.getLevel();

        return level.getBlockState(blockPos1).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(blockPos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    public static Direction getConnectedDirection(BlockState pState) {
        Direction direction = pState.getValue(FACING);
        return getPartFromState(pState) == Part.CENTER ? direction.getOpposite() : direction;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if(!pLevel.isClientSide) {
            BlockPos blockPos = pPos.relative(pState.getValue(FACING));
            pLevel.setBlock(blockPos, ModBlocks.CROPRESSOR_CENTER.get().defaultBlockState().setValue(FACING, pState.getValue(FACING)), 3);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pPos, 3);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        ENTITY_POS = PART == Part.OUT ? pPos : getEntityPos(pLevel, pPos);
        if(pLevel.getBlockEntity(ENTITY_POS) instanceof CropressorBlockEntity entity) {
            boolean a = entity.canInteract();
            if (!pLevel.isClientSide && a && pPlayer.getMainHandItem().is(ModTags.ModItemTags.CROPRESSABLE_CROPS)) {
                entity.addItem(pPlayer.getMainHandItem(), pLevel);

                return ItemInteractionResult.SUCCESS;
            }
        }
        
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }
    
    private BlockPos getEntityPos(Level level, BlockPos blockPos) {
        if(PART == Part.OUT) {
            return blockPos;
        }

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if(level.getBlockEntity(blockPos.relative(direction)) instanceof CropressorBlockEntity) {
                return blockPos.relative(direction);
            }
        }

        return null;
    }

    public static Part getPartFromState(BlockState blockState) {
        return blockState.getBlock() instanceof CropressorBlockBase baseCropressorBlock ? baseCropressorBlock.PART : null;
    }

    public static enum Part implements StringRepresentable {
        CENTER("center"),
        OUT("out");

        public static final StringRepresentable.EnumCodec<DyeColor> CODEC = StringRepresentable.fromEnum(DyeColor::values);
        private String name;

        Part(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
