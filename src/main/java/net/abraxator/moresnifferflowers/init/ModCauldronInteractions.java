package net.abraxator.moresnifferflowers.init;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import java.util.Map;

public class ModCauldronInteractions {
    public static final Map<Item, CauldronInteraction> BONMEEL = CauldronInteraction.newInteractionMap();
    public static final Map<Item, CauldronInteraction> ACID = CauldronInteraction.newInteractionMap();
    public static final CauldronInteraction FILL_JAR_OF_BONMEEL = (pState, pLevel, pPos, pPlayer, pHand, pStack) -> 
        CauldronInteraction.emptyBucket(pLevel, pPos,  pPlayer, pHand, ModItems.JAR_OF_BONMEEL.get().getDefaultInstance(), ModBlocks.BONMEEL_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BOTTLE_EMPTY);
    public static final CauldronInteraction JAR_OF_ACID = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            CauldronInteraction.emptyBucket(pLevel, pPos,  pPlayer, pHand, ModItems.JAR_OF_ACID.get().getDefaultInstance(), ModBlocks.ACID_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BOTTLE_EMPTY);

    public static void bootstrap() {
        BONMEEL.put(
                Items.GLASS_BOTTLE,
                (pState, pLevel, pPos, pPlayer, pHand, pStack) -> 
                        CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                                ModItems.JAR_OF_BONMEEL.get().getDefaultInstance(), 
                                blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, 
                                SoundEvents.BOTTLE_FILL)
        );
        BONMEEL.put(ModItems.JAR_OF_BONMEEL.get(), FILL_JAR_OF_BONMEEL);
        
        ACID.put(Items.GLASS_BOTTLE,
                (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                        CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                                ModItems.JAR_OF_ACID.get().getDefaultInstance(), 
                                blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, 
                                SoundEvents.BOTTLE_FILL)
        );
        ACID.put(ModItems.JAR_OF_ACID.get(), JAR_OF_ACID);
    }
}
