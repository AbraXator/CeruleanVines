package net.abraxator.moresnifferflowers.init;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class ModCauldronInteractions {
    public static final CauldronInteraction.InteractionMap BONMEEL = CauldronInteraction.newInteractionMap("bonmeel");
    public static final CauldronInteraction.InteractionMap ACID = CauldronInteraction.newInteractionMap("acid");
    public static final CauldronInteraction JAR_OF_BONMEEL = (pState, pLevel, pPos, pPlayer, pHand, pStack) -> 
        CauldronInteraction.emptyBucket(pLevel, pPos,  pPlayer, pHand, ModItems.JAR_OF_BONMEEL.toStack(), pState, SoundEvents.BOTTLE_EMPTY);
    public static final CauldronInteraction JAR_OF_ACID = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            CauldronInteraction.emptyBucket(pLevel, pPos,  pPlayer, pHand, ModItems.JAR_OF_ACID.toStack(), pState, SoundEvents.BOTTLE_EMPTY);

    public static void bootstrap() {
        Map<Item, CauldronInteraction> bonmeel = BONMEEL.map();
        bonmeel.put(
                Items.GLASS_BOTTLE,
                (pState, pLevel, pPos, pPlayer, pHand, pStack) -> 
                        CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, Items.GLASS_BOTTLE.getDefaultInstance(), ModItems.JAR_OF_BONMEEL.toStack(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL)
        );
        bonmeel.put(ModItems.JAR_OF_BONMEEL.asItem(), JAR_OF_BONMEEL);

        Map<Item, CauldronInteraction> acid = ACID.map();
        acid.put(Items.GLASS_BOTTLE,
                (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                        CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, Items.GLASS_BOTTLE.getDefaultInstance(), ModItems.JAR_OF_ACID.toStack(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL)
        );
        acid.put(ModItems.JAR_OF_ACID.get(), JAR_OF_ACID);
    }
}
