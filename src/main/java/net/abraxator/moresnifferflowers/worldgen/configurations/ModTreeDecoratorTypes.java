package net.abraxator.moresnifferflowers.worldgen.configurations;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedSludgeDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, MoreSnifferFlowers.MOD_ID);
    
    public static final RegistryObject<TreeDecoratorType<CorruptedSludgeDecorator>> CORRUPTED_SLUDGE = DECORATORS.register("corrupted_sludge", () -> new TreeDecoratorType<CorruptedSludgeDecorator>(CorruptedSludgeDecorator.CODEC));
}
