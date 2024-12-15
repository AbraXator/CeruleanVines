package net.abraxator.moresnifferflowers.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DawnberryVineBlock extends MultifaceBlock implements BonemealableBlock, ModCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final BooleanProperty IS_SHEARED = BooleanProperty.create("is_sheared");
    public static final MapCodec<DawnberryVineBlock> CODEC = RecordCodecBuilder.mapCodec(p_304392_ -> 
            p_304392_.group(propertiesCodec(), Codec.BOOL.fieldOf("evil").forGetter(DawnberryVineBlock::isEvil))
                    .apply(p_304392_, DawnberryVineBlock::new));

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);
    private final boolean evil;

    public DawnberryVineBlock(Properties pProperties, boolean evil) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(IS_SHEARED, Boolean.FALSE));

        this.evil = evil;
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(AGE, IS_SHEARED);
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getAge(BlockState pState) {
        return pState.getValue(this.getAgeProperty());
    }

    public int getMaxAge() {
        return AGE.getPossibleValues().stream().toList().get(AGE.getPossibleValues().size() - 1);
    }

    public final boolean isMaxAge(BlockState pState) {
        return this.getAge(pState) >= this.getMaxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return !this.isMaxAge(pState) && !pState.getValue(IS_SHEARED);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pStack.is(Items.SHEARS) && !(getAge(pState) >= 4) && !pState.getValue(ModStateProperties.SHEARED)) {
            return shearAction(pState, pLevel, pPos, pPlayer, pHand, pStack);
        } else if(pStack.is(Items.BONE_MEAL) && (getAge(pState) < 4)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (this.isMaxAge(pState)) {
            return dropMaxAgeLoot(pState, pLevel, pPos, pPlayer);
        } else if (pState.getValue(AGE) == 3) {
            return dropAgeThreeLoot(pState, pLevel, pPos, pPlayer);
        }

        return InteractionResult.PASS;
    }

    private ItemInteractionResult shearAction(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        shear(player, level, pos, blockState, hand);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private InteractionResult dropMaxAgeLoot(BlockState blockState, Level level, BlockPos pos, Player player) {
        RandomSource randomSource = level.getRandom();
        final ItemStack DAWNBERRY = new ItemStack(evil ? ModItems.GLOOMBERRY.get() : ModItems.DAWNBERRY.get(), randomSource.nextIntBetweenInclusive(1, 2));
        final ItemStack SEEDS = new ItemStack(evil ? ModItems.GLOOMBERRY_VINE_SEEDS.get() : ModItems.DAWNBERRY_VINE_SEEDS.get(), randomSource.nextIntBetweenInclusive(0, 1));

        popResource(level, pos, DAWNBERRY);
        popResource(level, pos, SEEDS);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        BlockState state = blockState.setValue(AGE, 2);
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected InteractionResult dropAgeThreeLoot(BlockState blockState, Level level, BlockPos pos, Player player) {
        final ItemStack DAWNBERRY = new ItemStack(ModItems.DAWNBERRY.get());
        popResource(level, pos, DAWNBERRY);
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        BlockState state = blockState.setValue(AGE, 2);
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            makeGrowOnTick(this, pState, pLevel, pPos);
        }
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return !isMaxAge(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        //grow(pState, pLevel, pPos, pRandom);
        int age = getAge(pState);
        pLevel.setBlock(pPos, pState.setValue(AGE, age >= 4 ? age : age + 1), 2);
        boolean canSpread = Direction.stream().anyMatch((p_153316_) -> this.spreader.canSpreadInAnyDirection(pState, pLevel, pPos, p_153316_.getOpposite()));
        if(pRandom.nextFloat() >= 0.3F && pRandom.nextFloat() >= 0.3F && canSpread) {
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    public boolean isEvil() {
        return evil;
    }
}
