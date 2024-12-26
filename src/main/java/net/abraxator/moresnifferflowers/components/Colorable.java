package net.abraxator.moresnifferflowers.components;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.Map;

import static net.abraxator.moresnifferflowers.init.ModStateProperties.COLOR;
import static net.abraxator.moresnifferflowers.init.ModStateProperties.EMPTY;

public interface Colorable {
    Map<DyeColor, Integer> colorValues();

    default TagKey<Block> matchTag() {
        return null;
    }
    
    default Pair<EnumProperty<DyeColor>, BooleanProperty> getColorAndEmptyProperties() {
        return new Pair<>(COLOR, EMPTY);
    }
        
    default boolean canBeColored(BlockState blockState, Dye dye) {
        return !getDyeFromBlock(blockState).color().equals(dye.color());
    }
    
    default Dye getDyeFromBlock(BlockState blockState) {
        DyeColor dyeColor = DyeColor.WHITE;
        boolean empty = true;
        
        if (blockState.hasProperty(getColorAndEmptyProperties().getA())) {
            dyeColor = blockState.getValue(getColorAndEmptyProperties().getA());
        }
        if (blockState.hasProperty(getColorAndEmptyProperties().getB())) {
            empty = blockState.getValue(getColorAndEmptyProperties().getB());
        }
        
        return new Dye(dyeColor, !empty ? 1 : 0);
    }
    
    default void colorBlock(Level level, BlockPos blockPos, BlockState blockState, Dye dye) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(getColorAndEmptyProperties().getA(), dye.color()));
    } 
    
    default boolean isColorEmpty(BlockState blockState) {
        return blockState.getValue(getColorAndEmptyProperties().getB());
    }
    
    default ItemStack add(@Nullable ItemStack dyespria, Dye dyeInside, ItemStack dyeToInsert) {
        if(dyeToInsert.getItem() instanceof DyeItem) {
            if(!dyeInside.isEmpty()) {
                int amountInside = dyeInside.amount();
                int freeSpace = 64 - amountInside;
                int totalDye = Math.min(amountInside + dyeToInsert.getCount(), 64); //AMOUNT TO INSERT INTO DYESPRIA
                if (!Dye.dyeCheck(dyeInside, dyeToInsert)) {
                    //DYESPRIA HAS DIFFERENT DYE AND YOU INSERT ANOTHER 😝
                    onAddDye(dyespria, dyeToInsert, dyeToInsert.getCount());
                    dyeToInsert.shrink(totalDye);
                    return Dye.stackFromDye(dyeInside);
                } else if(freeSpace <= 0) {
                    //DYESPRIA HAS NO SPACE 😇
                    return dyeToInsert;
                }

                //DYESPRIA HAS DYE AND YOU INSERT SAME DYE 🥳
                onAddDye(dyespria, dyeToInsert, totalDye);
                dyeToInsert.shrink(totalDye);
                return dyeToInsert;
            } else {
                //DYESPRIA HAS NO DYE 🥸
                onAddDye(dyespria, dyeToInsert, dyeToInsert.getCount());
                return ItemStack.EMPTY;
            }
        }
        return dyeToInsert;
    }
    
    default void onAddDye(@Nullable ItemStack destinationStack, ItemStack dye, int amount) {}

    default void particles(RandomSource randomSource, Level level, Dye dye, BlockPos blockPos) {
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.addParticle(
                    new DustParticleOptions(dye.isEmpty() ? Vec3.fromRGB24(14013909).toVector3f() : Vec3.fromRGB24(Dye.colorForDye(this, dye.color())).toVector3f(), 1.0F),
                    blockPos.getX() + randomSource.nextDouble(),
                    blockPos.getY() + randomSource.nextDouble(),
                    blockPos.getZ() + randomSource.nextDouble(),
                    0, 0, 0);
        }
    }
    
}
