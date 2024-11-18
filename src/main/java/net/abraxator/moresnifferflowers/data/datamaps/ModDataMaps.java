package net.abraxator.moresnifferflowers.data.datamaps;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class ModDataMaps {
    public static final DataMapType<Block, Corruptable> CORRUPTABLE = DataMapType.builder(
            MoreSnifferFlowers.loc("corruptable"), Registries.BLOCK, Corruptable.CODEC).build();
}
