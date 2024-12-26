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
import net.minecraft.util.StringRepresentable;
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
import org.apache.logging.log4j.core.pattern.ThreadIdPatternConverter;

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
                
                if(crop == null) {
                    return;
                }
                
                level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                level.addFreshEntity(entity);
                cropCount[crop.ordinal()] = 0;
                currentCrop = ItemStack.EMPTY;
                updateFullness(0, crop);
                level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
                setChanged();
                progress = 0;
            }
        }
    }

    public boolean canInteract() {
        return progress <= 0;
    }

    public ItemStack addItem(ItemStack pStack) {
        Crop crop = Crop.fromItem(pStack.getItem());
        if (Crop.canAddCrop(cropCount, crop)) {
            int index = crop.ordinal();
            
            int currentCount = this.cropCount[index];
            int maxAddable = 16 - currentCount;
            int itemsToAdd = Math.min(pStack.getCount(), maxAddable);

            if (itemsToAdd > 0) {
                this.cropCount[index] += itemsToAdd;
                this.currentCrop = new ItemStack(crop.item, this.cropCount[index]);
                
                pStack.shrink(itemsToAdd);
                
                int fullness = Mth.ceil((float) this.cropCount[index] / 2);
                updateFullness(fullness, crop);
            }
        }

        return pStack;
    }

    private void updateFullness(int fullness, Crop crop) {
        var pos = getBlockPos().relative(getBlockState().getValue(HorizontalDirectionalBlock.FACING));
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(ModStateProperties.FULLNESS, fullness));
        level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(ModStateProperties.FULLNESS, fullness).setValue(ModStateProperties.CROP, crop));
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
        cropCount = pTag.getIntArray("crop_count");
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
    
    public static enum Crop implements StringRepresentable {
        CARROT("carrot", Items.CARROT, 0xe67022),
        POTATO("potato", Items.POTATO, 0xce933a),
        WHEAT("wheat", Items.WHEAT, 0xcdb159),
        NETHERWART("netherwart", Items.NETHER_WART, 0x730408),
        BEETROOT("beetroot", Items.BEETROOT, 0xa4272c);
        
        String name;
        public Item item;
        public int tint;
        
        Crop(String name, Item item, int tint) {
            this.name = name;
            this.item = item;
            this.tint = tint;
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

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
