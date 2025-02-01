package net.abraxator.moresnifferflowers.compat.quark;

import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.api.event.SimpleHarvestEvent;

public class OtherModEvents {
    public static void onSimpleHarvest(SimpleHarvestEvent event) {
        if(event.entity instanceof Player player &&
                !event.level.isClientSide &&
                player.getItemInHand(event.hand == null ? InteractionHand.MAIN_HAND : event.hand).getItem() instanceof JarOfBonmeelItem &&
                event.blockState.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            event.level.setBlock(event.pos, event.blockState, 3);
        }
    }
}