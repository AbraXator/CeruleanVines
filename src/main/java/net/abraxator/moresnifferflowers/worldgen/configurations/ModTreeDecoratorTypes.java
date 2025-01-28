package net.abraxator.moresnifferflowers.worldgen.configurations;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.worldgen.configurations.tree.corrupted.CorruptedSludgeDecorator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> DECORATORS =
            DeferredRegister.create(BuiltInRegistries.TREE_DECORATOR_TYPE, MoreSnifferFlowers.MOD_ID);
    
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<CorruptedSludgeDecorator>> CORRUPTED_SLUDGE = DECORATORS.register("corrupted_sludge", () -> new TreeDecoratorType<CorruptedSludgeDecorator>(CorruptedSludgeDecorator.CODEC));
}
