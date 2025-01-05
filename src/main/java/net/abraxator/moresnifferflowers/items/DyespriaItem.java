package net.abraxator.moresnifferflowers.items;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.components.EntityDistanceComparator;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.networking.DyespriaDisplayModeChangePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DyespriaItem extends BlockItem implements Colorable {
    public DyespriaItem(Properties pProperties) {
        super(ModBlocks.DYESPRIA_PLANT.get(), pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack stack = pContext.getItemInHand();
        Dye dye = Dye.getDyeFromDyespria(stack);

        if (pContext.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (checkDyedBlock(blockState) || blockState.getBlock() instanceof Colorable && !dye.isEmpty()) {
            DyespriaMode dyespriaMode = getMode(stack);
            DyespriaMode.DyespriaSelector dyespriaSelector = new DyespriaMode.DyespriaSelector(blockPos, blockState, getMatchTag(blockState), level, pContext.getClickedFace());
            Set<BlockPos> set = dyespriaMode.getSelector().apply(dyespriaSelector);
            set.stream().sorted(new EntityDistanceComparator(blockPos)).forEach(blockPos1 -> {
                var state = level.getBlockState(blockPos1);

                if(!Dye.getDyeFromDyespria(stack).isEmpty()) {
                    colorOne(stack, level, blockPos1, state);
                }
            });

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return handlePlacement(blockPos, level, player, pContext.getHand(), stack);
    }
    
    public DyespriaMode getMode(ItemStack stack) {
        return DyespriaMode.byIndex(stack.getOrCreateTag().getByte("mode"));
    }
    
    private @Nullable TagKey<Block> getMatchTag(BlockState blockState) {
        return blockState instanceof Colorable colorable ? colorable.matchTag() : null;
    }
    
    private InteractionResult handlePlacement(BlockPos blockPos, Level level, Player player, InteractionHand hand, ItemStack stack) {
        var posForDyespria = blockPos.above();
        var blockHitResult = new BlockHitResult(posForDyespria.below().getCenter(), Direction.UP, posForDyespria.below(), false);
        var useOnCtx = new UseOnContext(level, player, hand, stack, blockHitResult);
        var result = super.useOn(useOnCtx);

        if (level.getBlockEntity(blockPos.above()) instanceof DyespriaPlantBlockEntity entity) {
            entity.dye = Dye.getDyeFromDyespria(stack);
            entity.setChanged();
        }

        return result;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        var state = super.getPlacementState(pContext);
        return state == null ? null : state.setValue(ModStateProperties.AGE_3, 3);
    }

    public boolean colorOne(ItemStack stack, Level level, BlockPos blockPos, BlockState blockState) {
        Dye dye = Dye.getDyeFromDyespria(stack);
        
        if (!canDye(blockState, dye)) {
            return false;
        }

        if(blockState.getBlock() instanceof Colorable colorable) {
            if(colorable.canBeColored(blockState, dye)) {
                colorable.colorBlock(level, blockPos, blockState, dye);
                finishColoring(dye, level, stack, blockPos);
                
                return true;
            }
        } else {
            dyeNonColorableBlock(blockState, blockPos, dye.color(), level);
            finishColoring(dye, level, stack, blockPos);

            return true;
        }
        
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !Dye.getDyeFromDyespria(stack).isEmpty();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int lowColor = 0x8c1111;
        int highColor = 0x179529;
        int input = getDyespriaUses(stack)-1;
        int maxInput= 4;

        int lowRed = (lowColor >> 16) & 0xFF;
        int lowGreen = (lowColor >> 8) & 0xFF;
        int lowBlue = lowColor & 0xFF;

        int highRed = (highColor >> 16) & 0xFF;
        int highGreen = (highColor >> 8) & 0xFF;
        int highBlue = highColor & 0xFF;

        float[] lowHSB =  Color.RGBtoHSB(lowRed, lowGreen, lowBlue, null);
        float[] highHSB =  Color.RGBtoHSB(highRed, highGreen, highBlue, null);


        float finalHue = ((lowHSB[0] * (Math.abs(input - maxInput))) + (highHSB[0] * input)) / maxInput;

        return Mth.hsvToRgb(finalHue, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getDyespriaUses(stack) * 13.0F / 4);
    }

    public void finishColoring(Dye dye, Level level, ItemStack dyespria, BlockPos blockPos) {
        int uses = getDyespriaUses(dyespria) - 1;
        int dyeCount;
        
        if(uses <= 0) {
            dyeCount = dye.amount() - 1;
            setDyespriaUses(dyespria, 4);
        } else {
            dyeCount = dye.amount();
            setDyespriaUses(dyespria, uses);
        }
        
        ItemStack itemStack = Dye.stackFromDye(new Dye(dye.color(), dyeCount));
        Dye.setDyeToDyeHolderStack(dyespria, itemStack, itemStack.getCount(), getDyespriaUses(dyespria));
        if (level.isClientSide) {
            particles(level.getRandom(), level, dye, blockPos);
        }
    }
    
    public static int getDyespriaUses(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.contains("uses") ? tag.getInt("uses") : 4;
    }
    
    public static void setDyespriaUses(ItemStack stack, int uses) {
        var tag = stack.getOrCreateTag();
        tag.putInt("uses", uses);
        stack.setTag(tag);
    }
    
    private boolean canDye(BlockState blockState, Dye dye) {
        return (blockState.hasProperty(ModStateProperties.COLOR) && !blockState.getValue(ModStateProperties.COLOR).equals(dye.color())) || !dye.isEmpty();
    }

    public static boolean checkDyedBlock(BlockState blockState) {
        return blockState.is(ModTags.ModBlockTags.DYED);
    }
    
    private void dyeNonColorableBlock(BlockState blockState, BlockPos blockPos, DyeColor newColor, Level level) {
        if(!checkDyedBlock(blockState)) {
            return;
        }
        
        String blockIdentifier = blockState.getBlock().toString().replace("Block{", "").replace("}", "");
        String[] identifiers = blockIdentifier.split(":");
        String blockId = identifiers[1]; 
        String modId = identifiers[0];   
        String[] splitBlockId = blockId.split("_");
        List<String> validColors = Arrays.stream(DyeColor.values())
                .map(DyeColor::getName)
                .toList();
        int colorIndex = validColors.contains(splitBlockId[0]) ? 0 : 1;
        splitBlockId[colorIndex] = newColor.getName();
        
        String finalBlockName = String.join("_", splitBlockId);
        Block finalBlock = BuiltInRegistries.BLOCK.get(new ResourceLocation(modId, finalBlockName));
        BlockState finalBlockState = finalBlock.defaultBlockState();
        
        level.setBlockAndUpdate(blockPos, copyAllBlockStateProperties(blockState, finalBlockState));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState copyAllBlockStateProperties(BlockState sourceState, BlockState targetState) {
        for (Property<?> property : sourceState.getProperties()) {
            if (targetState.hasProperty(property)) {
                T value = (T) sourceState.getValue(property);
                targetState = targetState.setValue((Property<T>) property, value);
            }
        }
        return targetState;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if(pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if(pOther.isEmpty()) {
                pAccess.set(remove(pStack));
                playRemoveOneSound(pPlayer);
            } else {
                ItemStack itemStack = add(pStack, Dye.getDyeFromDyespria(pStack), pOther);
                pAccess.set(itemStack);
                if(itemStack.isEmpty()) {
                    this.playInsertSound(pPlayer);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onAddDye(@Nullable ItemStack destinationStack, ItemStack dye, int amount) {
        Dye.setDyeToDyeHolderStack(destinationStack, dye, amount);
    }

    private ItemStack remove(ItemStack pStack) {
        var dye = Dye.getDyeFromDyespria(pStack);
        int uses = getDyespriaUses(pStack);
        
        if(!dye.isEmpty()) {
            Dye.setDyeColorToStack(pStack, DyeColor.WHITE, 0);
            return Dye.stackFromDye(new Dye(dye.color(), dye.amount() - (uses == 4 ? 0 : 1)));
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Dye dye = Dye.getDyeFromDyespria(pStack);
        Component usage = Component.translatableWithFallback("tooltip.dyespria.usage", "Right click with dye to insert \nRight click caulorflower to repaint \nSneak to apply to the whole column \n").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(usage.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> pTooltipComponents.add(Component.literal(s).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(getCurrentModeComponent(getMode(pStack)));
        pTooltipComponents.add(Component.empty());
        
        if(!dye.isEmpty()) {
            var name = Component
                    .literal(dye.amount() + " - " + WordUtils.capitalizeFully(dye.color()
                            .getName()
                            .toLowerCase()
                            .replaceAll("[^a-z_]", "")
                            .replaceAll("_", " ")))
                    .withStyle(Style.EMPTY
                            .withColor(Dye.colorForDye(this, dye.color())));
            pTooltipComponents.add(name);
        } else {
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.dyespria.empty", "Empty").withStyle(ChatFormatting.GRAY));
        }
    }

    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        return Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
            dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFFFFFFF);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFF9d979b);
            dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFF474f52);
            dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF1d1d21);
            dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFF835432);
            dyeColorHexFormatMap.put(DyeColor.RED, 0xFFce5849);
            dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFf89635);
            dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFffee53);
            dyeColorHexFormatMap.put(DyeColor.LIME, 0xFF80c71f);
            dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFF5e7c16);
            dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF36a98c);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFF70d9e4);
            dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFF4753ac);
            dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFFb15fc2);
            dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFd276b9);
            dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFf8b0c4);
        });
    }
    
    public static Component getCurrentModeComponent(DyespriaMode dyespriaMode) {
        var baseText = Component.translatable("message.more_sniffer_flowers.dyespria_mode").append(": ").withStyle(ChatFormatting.GOLD);
        var modeText = Component.literal(dyespriaMode.getSerializedName()).withStyle(dyespriaMode.getTextColor());
        return baseText.append(modeText);
    }
    
    public void changeMode(ServerPlayer player, ItemStack stack, int amount) {
        var currentMode = getMode(stack);
        var newMode = DyespriaMode.shift(currentMode, amount);
        var tag = stack.getOrCreateTag();
        tag.putByte("mode", (byte) newMode.ordinal());
        stack.setTag(tag);
        ModPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new DyespriaDisplayModeChangePacket(newMode.ordinal()));
    }
}