package net.abraxator.moresnifferflowers.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public enum DyespriaMode implements StringRepresentable {
    SINGLE("single", DyespriaSelector::single, ChatFormatting.WHITE),
    COLUMN("column", DyespriaSelector::column, ChatFormatting.BLUE),
    ROW("row", DyespriaSelector::row, ChatFormatting.GREEN),
    SHAPE("shape", DyespriaSelector::shape, ChatFormatting.RED);

    public static final Codec<DyespriaMode> CODEC = StringRepresentable.fromValues(DyespriaMode::values);
    public static final IntFunction<DyespriaMode> BY_ID = ByIdMap.continuous(DyespriaMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, DyespriaMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal); 

    private final String name;
    private final Function<DyespriaSelector, Set<BlockPos>> selector;
    private final ChatFormatting textColor;

    DyespriaMode(String name, Function<DyespriaSelector, Set<BlockPos>> selector, ChatFormatting textColor) {
        this.name = name;
        this.selector = selector;
        this.textColor = textColor;
    }
    
    public static DyespriaMode byIndex(int index) {
        return BY_ID.apply(index);
    }

    public static DyespriaMode shift(DyespriaMode current, int amount) {
        int size = values().length;
        int currentIndex = current.ordinal();
        int newIndex = (currentIndex + amount) % size;

        return byIndex(newIndex);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Function<DyespriaSelector, Set<BlockPos>> getSelector() {
        return selector;
    }

    public ChatFormatting getTextColor() {
        return textColor;
    }

    public static record DyespriaSelector(BlockPos originalPos, BlockState blockState, @Nullable TagKey<Block> tag, Level level, Direction clickedDir) {
        public Set<BlockPos> single() {
            return Set.of(originalPos());
        }
        
        public Set<BlockPos> column() {
            Set<BlockPos> ret = new HashSet<>();
            ret.add(originalPos);
            var posUp = originalPos.above().mutable();
            var posDown = originalPos.below().mutable();
            while (matchBlock(posUp)) {
                ret.add(posUp.immutable());
                posUp.move(Direction.UP);
            }
            
            while(matchBlock(posDown)) {
                ret.add(posDown.immutable());
                posDown.move(Direction.DOWN);
            }
            
            return ret;
        }

        public Set<BlockPos> row() {
            Set<BlockPos> ret = new HashSet<>();
            ret.add(originalPos);
            
            if (clickedDir == Direction.DOWN || clickedDir == Direction.UP) {
                return ret;
            }
            
            var rightDir = clickedDir.getCounterClockWise();
            var leftDir = clickedDir.getClockWise();
            var posRight = originalPos.relative(rightDir).mutable();
            var posDown = originalPos.relative(leftDir).mutable();
            while (matchBlock(posRight)) {
                ret.add(posRight.immutable());
                posRight.move(rightDir);
            }

            while(matchBlock(posDown)) {
                ret.add(posDown.immutable());
                posDown.move(leftDir);
            }

            return ret;
        }

        public Set<BlockPos> shape() {
            return BlockPos.withinManhattanStream(originalPos, 4, 4, 4)
                    .map(BlockPos::immutable)
                    .filter(this::matchBlock)
                    .collect(Collectors.toSet());
        }
        
        private boolean matchBlock(BlockPos pos) {
            var state = level.getBlockState(pos);
            boolean isVanilla = DyespriaItem.checkDyedBlock(state);
            boolean isColorableAndColored = state.is(blockState.getBlock()) 
                    && state.hasProperty(ModStateProperties.COLOR) 
                    && state.getValue(ModStateProperties.COLOR).equals(blockState.getValue(ModStateProperties.COLOR));
            return isVanilla || isColorableAndColored || (tag != null && level.getBlockState(pos).is(tag));
        }
    }
}
