package net.abraxator.moresnifferflowers.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

public record Dye(DyeColor color, int amount) {
    public static final Codec<Dye> CODEC = RecordCodecBuilder.create(
            dyeInstance -> dyeInstance.group(
                    Codec.INT.fieldOf("color").forGetter(Dye::colorId),
                    Codec.INT.fieldOf("amount").forGetter(Dye::amount)
            ).apply(dyeInstance, (colorId, amount) -> new Dye(colorFromId(colorId), amount))
    );
    
    public static final Dye EMPTY = new Dye(DyeColor.WHITE, 0);
    
    public boolean isEmpty() {
        return Dye.this.amount <= 0;
    }

    public static Dye getDyeFromDyeStack(ItemStack dyeStack) {
        return new Dye(((DyeItem) dyeStack.getItem()).getDyeColor(), dyeStack.getCount());
    }
    
    public static Dye getDyeFromDyespria(ItemStack dyespria) {
        CompoundTag tag = dyespria.getOrCreateTag();
        int count = tag.getInt("amount");
        DyeColor color = DyeColor.byId(tag.getInt("color"));
        
        return new Dye(color, count);
    }
    
    public static ItemStack stackFromDye(Dye dye) {
        return dye.isEmpty() ? ItemStack.EMPTY : new ItemStack(DyeItem.byColor(dye.color), dye.amount);
    }    
    
    public static boolean dyeCheck(Dye dye, ItemStack dyeToInsert) {
        DyeItem dyeToInsertItem = ((DyeItem) dyeToInsert.getItem());

        return dye.color.equals(dyeToInsertItem.getDyeColor());
    }
    
    public static int colorForDye(Colorable colorable, DyeColor dyeColor) {
        return colorable.colorValues().getOrDefault(dyeColor, -1);
    }

    public static void setDyeToDyeHolderStack(ItemStack dyespria, ItemStack dyeToInsert, int amount) {
        setDyeToDyeHolderStack(dyespria, dyeToInsert, amount, 4);
    }
    
    public static void setDyeToDyeHolderStack(ItemStack dyespria, ItemStack dyeToInsert, int amount, int uses) {
        var dyeColor = dyeToInsert.getItem() instanceof DyeItem ? ((DyeItem) dyeToInsert.getItem()).getDyeColor() : DyeColor.WHITE;
        CompoundTag tag = dyespria.getOrCreateTag();
        tag.putInt("amount", amount);
        tag.putInt("color", dyeColor.getId());
        tag.putInt("uses", uses);
        dyespria.setTag(tag);
    }
    
    public static void setDyeColorToStack(ItemStack stack, DyeColor color, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("amount", amount);
        tag.putInt("color", color.getId());
        stack.setTag(tag);
    }
    
    public static DyeColor colorFromId(int id) {
        return DyeColor.byId(id);
    }
    
    public int colorId() {
        return color.getId();
    }
}