package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType CORRUPTED = WoodType.register(new WoodType(MoreSnifferFlowers.MOD_ID + ":corrupted", BlockSetType.WARPED));
    public static final WoodType VIVICUS = WoodType.register(new WoodType(MoreSnifferFlowers.MOD_ID + ":vivicus", BlockSetType.CHERRY));
}
