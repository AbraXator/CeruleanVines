package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModRecipeTypes;
import net.abraxator.moresnifferflowers.init.ModSoundEvents;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;

public class CropressorBlockEntity extends ModBlockEntity {
    public int[] cropCount = new int[5];
    public ItemStack currentCrop = ItemStack.EMPTY;
    public ItemStack result = ItemStack.EMPTY;
    public int progress = 0;
    public final int MAX_PROGRESS = 100;
    private static final int INV_SIZE = 16;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CropressingRecipe> quickCheck = RecipeManager.createCheck(ModRecipeTypes.CROPRESSING.get());
    private boolean sound = true;

    public CropressorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CROPRESSOR.get(), pPos, pBlockState);
    }

    @Override
    public void tick(Level level) {
        var recipeInput = new SingleRecipeInput(currentCrop);
        var cropressingRecipeOptional = quickCheck.getRecipeFor(recipeInput, level);
        
        if(currentCrop.getCount() >= INV_SIZE && cropressingRecipeOptional.isPresent()) {
            result = cropressingRecipeOptional.get().value().result();
            progress++;

            if(sound) {
                level.playSound(null, worldPosition, ModSoundEvents.CROPRESSOR_BELT.get(), SoundSource.BLOCKS, 1.0F, (float) (1.0F + (level.getRandom().nextFloat() * 0.2)));
                sound = false;
            }
            
            if(progress % 10 == 0) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
            }

            if(progress >= MAX_PROGRESS) {
                Vec3 blockPos = getBlockPos().relative(getBlockState().getValue(CropressorBlockBase.FACING).getOpposite()).getCenter();
                ItemEntity entity = new ItemEntity(level, blockPos.x, blockPos.y + 0.5, blockPos.z, result);
                Crop crop = Crop.fromItem(currentCrop.getItem());
                level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                level.addFreshEntity(entity);
                cropCount[crop.ordinal()] = 0;
                currentCrop = ItemStack.EMPTY;
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
                setChanged();
                progress = 0;
            }
        }
    }

    public boolean canInteract() {
        return 0 >= progress || currentCrop.getCount() >= INV_SIZE;
    }

    public ItemStack addItem(ItemStack pStack, Level level) {
        Crop crop = Crop.fromItem(pStack.getItem());
        if(Crop.canAddCrop(cropCount, crop)) {
            int index = crop.ordinal();
            this.cropCount[index]++;
            this.currentCrop = new ItemStack(crop.item, this.cropCount[index]);
            pStack.shrink(1);
            int fullness = Mth.ceil((float) this.cropCount[index] / 2);
            var pos = getBlockPos().relative(getBlockState().getValue(HorizontalDirectionalBlock.FACING));
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(ModStateProperties.FULLNESS, fullness));
            level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ModStateProperties.FULLNESS, fullness));
            return pStack;
        }
        
        return pStack;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putIntArray("crop_count", cropCount);
        pTag.put("content", currentCrop.saveOptional(pRegistries));
        pTag.putInt("progress", progress);
        pTag.put("result", currentCrop.saveOptional(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        cropCount = pTag.getIntArray("crop:count");
        currentCrop = ItemStack.parseOptional(pRegistries, pTag.getCompound("content"));
        progress = pTag.getInt("progress");
        result = ItemStack.parseOptional(pRegistries, pTag.getCompound("result"));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.put("result", result.saveOptional(pRegistries));
        compoundtag.putInt("progress", progress);
        return compoundtag;
    }
    
    public static enum Crop {
        CARROT(Items.CARROT),
        POTATO(Items.POTATO),
        WHEAT(Items.WHEAT),
        NETHERWART(Items.NETHER_WART),
        BEETROOT(Items.BEETROOT);
        
        public Item item;
        
        Crop(Item item) {
            this.item = item;
        }
        
        public static boolean canAddCrop(int[] cropCount, Crop crop) {
            return crop != null && getCount(cropCount, crop) < 16;
        }
        
        public static int getCount(int[] cropCount, Crop crop) {
            return cropCount[crop.ordinal()];
        }
        
        @Nullable
        public static Crop fromItem(Item item) {
            return Arrays.stream(values())
                    .filter(crops -> crops.item == item)
                    .findFirst()
                    .orElse(null);
        }
    }
}
