package net.abraxator.moresnifferflowers.items;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.blocks.CaulorflowerBlock;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class DyespriaItem extends Item {
    public static final Map<DyeColor, Integer> COLORS = Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
        dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFD9D9D9);
        dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFF696969);
        dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFF4C4C4C);
        dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF1E1E1E);
        dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFF724727);
        dyeColorHexFormatMap.put(DyeColor.RED, 0xFFD0021B);
        dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFFF8300);
        dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFFFE100);
        dyeColorHexFormatMap.put(DyeColor.LIME, 0xFF41CD34);
        dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFF287C23);
        dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF00AACC);
        dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFF4F3FE0);
        dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFF2E388D);
        dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFF963EC9);
        dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFD73EDA);
        dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFF38BAA);
    });

    public DyespriaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack stack = pContext.getItemInHand();

        if (pContext.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if(blockState.is(ModBlocks.CAULORFLOWER.get()) && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
            if (!player.isShiftKeyDown()) {
                colorOne(stack, serverLevel, blockPos, blockState);
            } else {
                colorColumn(stack, serverLevel, blockPos);
            }
            level.playSound(player, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS);
            ModAdvancementCritters.USED_DYESPRIA.get().trigger(serverPlayer);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public void colorOne(ItemStack stack, ServerLevel level, BlockPos blockPos, BlockState blockState) {
        Dye dye = getDye(stack);
        RandomSource randomSource = level.random;

        if(blockState.getValue(CaulorflowerBlock.COLOR).equals(dye.color)) {
            return;
        }

        if(!dye.isEmpty()) {
            level.setBlock(blockPos, blockState.setValue(CaulorflowerBlock.COLOR, dye.color).setValue(CaulorflowerBlock.HAS_COLOR, true), 3);
            setDye(stack, stackFromDye(new Dye(dye.color, dye.amount - 1)));
        } else {
            level.setBlock(blockPos, blockState.setValue(CaulorflowerBlock.HAS_COLOR, false), 3);
        }

        particles(randomSource, level, dye, blockPos);

        level.sendBlockUpdated(blockPos, blockState, blockState, 1);
    }

    private void colorColumn(ItemStack stack, ServerLevel level, BlockPos blockPos) {
        BlockPos posUp = blockPos.above().mutable();
        BlockPos posDown = blockPos.mutable();
        while (level.getBlockState(posUp).is(ModBlocks.CAULORFLOWER.get())) {
            colorOne(stack, level, posUp, level.getBlockState(posUp));
            posUp = posUp.above();
        }

        while (level.getBlockState(posDown).is(ModBlocks.CAULORFLOWER.get())) {
            colorOne(stack, level, posDown, level.getBlockState(posDown));
            posDown = posDown.below();
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if(pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if(pOther.isEmpty()) {
                pAccess.set(remove(pStack));
                playRemoveOneSound(pPlayer);
            } else {
                ItemStack itemStack = add(pStack, pOther);
                pAccess.set(itemStack);
                if(itemStack.isEmpty()) {
                    this.playInsertSound(pPlayer);
                }
            }
            return true;
        }
        return false;
    }

    private void particles(RandomSource randomSource, ServerLevel level, Dye dye, BlockPos blockPos) {
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.sendParticles(
                    new DustParticleOptions(dye.isEmpty() ? Vec3.fromRGB24(14013909).toVector3f() : Vec3.fromRGB24(colorForDye(dye.color)).toVector3f(), 1.0F),
                    blockPos.getX() + randomSource.nextDouble(),
                    blockPos.getY() + randomSource.nextDouble(),
                    blockPos.getZ() + randomSource.nextDouble(),
                    1, 0, 0, 0, 0.3D);
        }
    }

    private ItemStack add(ItemStack dyespria, ItemStack dyeToInsert) {
        Dye dye = getDye(dyespria);
        if(dyeToInsert.getItem() instanceof DyeItem) {
            if(!dye.isEmpty()) {
                int amountInside = dye.amount;
                int freeSpace = 64 - amountInside;
                int totalDye = Math.min(amountInside + dyeToInsert.getCount(), 64); //AMOUNT TO INSERT INTO DYESPRIA
                if (!dyeCheck(dye, dyeToInsert)) {
                    //DYESPRIA HAS DIFFERENT DYE AND YOU INSERT ANOTHER 😝
                    setDye(dyespria, dyeToInsert);
                    dyeToInsert.shrink(totalDye);
                    return stackFromDye(dye);
                } else if(freeSpace <= 0) {
                    //DYESPRIA HAS NO SPACE 😇
                    return dyeToInsert;
                }

                //DYESPRIA HAS DYE AND YOU INSERT SAME DYE 🥳
                setDye(dyespria, dye.color, totalDye);
                dyeToInsert.shrink(totalDye);
                return dyeToInsert;
            } else {
                //DYESPRIA HAS NO DYE 🥸
                setDye(dyespria, dyeToInsert);
                return ItemStack.EMPTY;
            }
        }
        return dyeToInsert;
    }

    private boolean dyeCheck(Dye dye, ItemStack dyeToInsert) {
        DyeItem dyeToInsertItem = ((DyeItem) dyeToInsert.getItem());

        return dye.color.equals(dyeToInsertItem.getDyeColor());
    }

    private ItemStack remove(ItemStack pStack) {
        Dye dye = getDye(pStack);
        if(!dye.isEmpty()) {
            setDye(pStack, DyeColor.WHITE, 0);
            return stackFromDye(dye);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        Dye dye = getDye(pStack);
        Component usage = Component.translatableWithFallback("tooltip.dyespria.usage", "Right click with dye to insert \nRight click caulorflower to repaint \nSneak to apply to the whole column \n").withStyle(ChatFormatting.GOLD);

        if(!dye.isEmpty()) {
            Component name = Component
                    .literal(dye.amount + " - " + WordUtils.capitalizeFully(dye.color
                                    .getName()
                                    .toLowerCase()
                                    .replaceAll("[^a-z_]", "")
                                    .replaceAll("_", " ")))
                    .withStyle(Style.EMPTY
                            .withColor(colorForDye(dye.color)));

            pTooltipComponents.add(usage);
            pTooltipComponents.add(name);
        } else {
            pTooltipComponents.add(usage);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.dyespria.empty", "Empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public static void setDye(ItemStack stack, ItemStack dye) {
        setDye(stack, ((DyeItem) dye.getItem()).getDyeColor(), dye.getCount());
    }

    public static void setDye(ItemStack stack, DyeColor dyeColor, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("color", dyeColor.getId());
        tag.putInt("amount", amount);
        stack.setTag(tag);
    }

    public static Dye getDye(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        int colorId = tag.getInt("color");
        int amount = tag.getInt("amount");

        return new Dye(DyeColor.byId(colorId), amount);
    }

    public static ItemStack stackFromDye(Dye dye) {
        return new ItemStack(DyeItem.byColor(dye.color), dye.amount);
    }

    public static int colorForDye(DyeColor dyeColor) {
        return COLORS.getOrDefault(dyeColor, -1);
    }

    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    public record Dye(DyeColor color, int amount) {
        public boolean isEmpty() {
            return Dye.this.amount <= 0;
        }
    }
}
