package net.abraxator.moresnifferflowers.blockentities;

import com.google.common.collect.Lists;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.client.gui.menu.RebrewingStandMenu;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RebrewingStandBlockEntity extends BaseContainerBlockEntity {
    public static final double MAX_FUEL = 16;
    public static final int DATA_PROGRESS = 0;
    public static final int DATA_FUEL = 1;
    public static final int MAX_PROGRESS = 100;
    private NonNullList<ItemStack> inv = NonNullList.withSize(6, ItemStack.EMPTY);
    int brewProgress;
    int fuel;
    int cost;
    private boolean[] lastPotionCount;
    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> RebrewingStandBlockEntity.this.brewProgress;
                case 1 -> RebrewingStandBlockEntity.this.fuel;
                case 2 -> RebrewingStandBlockEntity.this.cost;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    RebrewingStandBlockEntity.this.brewProgress = pValue;
                    break;
                case 1:
                    RebrewingStandBlockEntity.this.fuel = pValue;
                    break;
                case 2:
                    RebrewingStandBlockEntity.this.cost = pValue;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public RebrewingStandBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.REBREWING_STAND.get(), pPos, pBlockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("");
    }

    @Override
    public boolean isEmpty() {
        return inv.stream().allMatch(Predicate.not(ItemStack::isEmpty));
    }

    public void tick(Level level) {
        //FUEL 0; OG-POTION 1; INGREDIENT 2; POTION 3 - 5;
        var fuelStack = inv.get(0);
        var ogPotionStack = inv.get(1);
        var ingredientStack = inv.get(2);
        var potionBits = getPotionBits();
        this.cost = 0;

        if(fuel < MAX_FUEL && fuelStack.is(ModItems.CROPRESSED_NETHERWART.get())) {
            fuel++;
            fuelStack.shrink(1);
            setChanged();
        }

        if(!ogPotionStack.isEmpty()) {
            var potionContent = getEffect(ogPotionStack, ingredientStack);
            this.cost = potionContent != null ? 4 + (potionContent.size() - 2) * 2 : 17;

            if(canBrew()) {
                brewProgress++;
                if(brewProgress >= MAX_PROGRESS) {
                    brew(level, ogPotionStack, ingredientStack);
                }
            }
        }

        if(!canBrew()) {
            brewProgress = 0;
        }

        if(!Arrays.equals(potionBits, lastPotionCount)) {
            bottleStateLogic(potionBits);
        }
    }

    private void brew(Level level, ItemStack ogPotionStack, ItemStack ingredientStack) {
        var effects = getEffect(ogPotionStack, ingredientStack);
        if(effects != null) {
            List<Integer> index = Util.make(Lists.newArrayList(), integers -> integers.addAll(Arrays.asList(3, 4, 5)));
            for (int i : index) {
                ItemStack itemStack = inv.get(i);
                if (!itemStack.is(ItemStack.EMPTY.getItem())) {
                    ItemStack outputPotion = ModItems.REBREWED_POTION.get().getDefaultInstance();

                    if(ingredientStack.is(Items.GUNPOWDER)) {
                        outputPotion = ModItems.REBREWED_SPLASH_POTION.get().getDefaultInstance();
                    } else if (ingredientStack.is(Items.DRAGON_BREATH)) {
                        outputPotion = ModItems.REBREWED_LINGERING_POTION.get().getDefaultInstance();
                    }

                    PotionUtils.setCustomEffects(outputPotion, effects);
                    inv.set(i, outputPotion);
                }
            }

            ingredientStack.shrink(1);
            inv.set(1, Items.GLASS_BOTTLE.getDefaultInstance());
            fuel -= this.cost;
            level.playSound(null, getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        brewProgress = 0;
    }

    private void bottleStateLogic(boolean[] potionBits) {
        lastPotionCount = potionBits;
        BlockState blockstate = level.getBlockState(getBlockPos().below());
        if (!(blockstate.getBlock() instanceof RebrewingStandBlockBase)) {
            return;
        }

        for(int i = 0; i < RebrewingStandBlockBase.HAS_BOTTLE.length; ++i) {
            blockstate = blockstate.setValue(RebrewingStandBlockBase.HAS_BOTTLE[i], potionBits[i]);
        }

        level.setBlock(getBlockPos().below(), blockstate, 2);
    }

    private boolean canBrew() {
        boolean ret = false;
        boolean correctInvContent = !inv.get(2).isEmpty() && inv.get(1).is(ModItems.EXTRACTED_BOTTLE.get());
        boolean hasFuel = fuel >= 1 && this.fuel >= this.cost;
        boolean correctCost = this.cost <= 16;


        for(int i = 3; i <= 5; i++) {
            if(!inv.get(i).isEmpty() && !inv.get(i).is(ModItems.REBREWED_POTION.get())) {
                ret = true;
            }
        }

        return ret && correctInvContent && hasFuel && correctCost;
    }

    private boolean[] getPotionBits() {
        boolean[] ret = new boolean[3];

        for(int i = 3; i <= 5; ++i) {
            if (!this.inv.get(i).isEmpty()) {
                ret[i - 3] = true;
            }
        }

        return ret;
    }

    private List<MobEffectInstance> getEffect(ItemStack inputPotion, ItemStack ingredient) {
        List<MobEffectInstance> ret = new ArrayList<>();
        List<Integer> durList = new ArrayList<>();
        ListTag listTag = ((ListTag) inputPotion.getOrCreateTag().get("CustomPotionEffects"));
        int defaultAmp = 1;
        int defaultDur = 6000;

        if (listTag == null) {
            return null;
        }

        for (int i = 0; i < listTag.size(); i++) {
            var potion = listTag.getCompound(i);
            var id = potion.getString("forge:id");
            var amp = potion.getByte("Amplifier") + (ingredient.is(Items.GLOWSTONE_DUST) ? 2 : defaultAmp);
            var dur = potion.getInt("Duration") + (ingredient.is(Items.REDSTONE) ? 12000 : defaultDur);
            var splitId = id.split(":");
            var instance = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(splitId[0], splitId[1])), dur, amp);

            durList.add(dur);
            ret.add(instance);
        }

        int maxInt = Collections.max(durList);
        ret.add(new MobEffectInstance(ModMobEffects.EXTRACTED.get(), maxInt));

        return ret;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new RebrewingStandMenu(pContainerId, pInventory, this, this.containerData);
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return pSlot >= 0 && pSlot < this.inv.size() ? this.inv.get(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(this.inv, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(inv, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot < this.inv.size()) {
            this.inv.set(pSlot, pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.inv.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, inv);
        pTag.putByte("progress", ((byte) brewProgress));
        pTag.putByte("fuel", ((byte) fuel));
        pTag.putByte("cost", ((byte) this.cost));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inv = NonNullList.withSize(6, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, inv);
        fuel = pTag.getByte("fuel");
        brewProgress = pTag.getByte("progress");
        this.cost = pTag.getByte("cost");
    }
}