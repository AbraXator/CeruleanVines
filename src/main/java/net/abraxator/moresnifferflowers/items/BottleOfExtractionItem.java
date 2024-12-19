package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BottleOfExtractionItem extends Item {
    public BottleOfExtractionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        var ret = pStack;
        
        if (!(pLivingEntity instanceof Player player) && pLevel.isClientSide) {
            return pStack;
        }
        
        if (pLivingEntity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }
        
        ret = initPotion(((Player) pLivingEntity));
        pLivingEntity.removeAllEffects();
        return ret;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!canExtract(pLevel, pPlayer)) {
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        } else {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }
    }

    private ItemStack initPotion(Player player) {
        var stack = ModItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
        var effects = player.getActiveEffects();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(PotionContents.getColor(effects)), new ArrayList<>(effects)));
        return stack;
    }
    
    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    private boolean canExtract(Level level, Player player) {
        return !level.isClientSide && player.getActiveEffects() != null && !player.getActiveEffects().isEmpty() && !player.hasEffect(ModMobEffects.EXTRACTED);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatableWithFallback("bottle_of_extraction.tooltip.usage", "Drink to extract all effects into single potion").withStyle(ChatFormatting.GOLD));
    }
}
