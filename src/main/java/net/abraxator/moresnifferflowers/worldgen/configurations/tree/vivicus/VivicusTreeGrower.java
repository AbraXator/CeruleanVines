package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class VivicusTreeGrower {
    private final String name;
    private final ResourceKey<ConfiguredFeature<?, ?>> cured_tree;
    private final ResourceKey<ConfiguredFeature<?, ?>> corrupted_tree;

    public VivicusTreeGrower(String name, ResourceKey<ConfiguredFeature<?, ?>> cured_tree, ResourceKey<ConfiguredFeature<?, ?>> corrupted_tree) {
        this.name = name;
        this.cured_tree = cured_tree;
        this.corrupted_tree = corrupted_tree;
    }

    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, BlockState saplingState) {
        return saplingState.getValue(ModStateProperties.VIVICUS_TYPE) == BoblingEntity.Type.CORRUPTED ? corrupted_tree : cured_tree;
    }

    public boolean growTree(ServerLevel pLevel, ChunkGenerator pChunkGenerator, BlockPos pPos, BlockState saplingState, RandomSource pRandom) {
        ResourceKey<ConfiguredFeature<?, ?>> resourcekey1 = this.getConfiguredFeature(pRandom, saplingState);
        if (resourcekey1 == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> holder1 = pLevel.registryAccess()
                    .registryOrThrow(Registries.CONFIGURED_FEATURE)
                    .getHolder(resourcekey1)
                    .orElse(null);
            var event = net.neoforged.neoforge.event.EventHooks.fireBlockGrowFeature(pLevel, pRandom, pPos, holder1);
            holder1 = event.getFeature();
            if (event.isCanceled()) return false;
            if (holder1 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> configuredfeature1 = holder1.value();
                BlockState blockstate1 = pLevel.getFluidState(pPos).createLegacyBlock();
                //pLevel.setBlock(pPos, blockstate1, 4);
                if (configuredfeature1.place(pLevel, pChunkGenerator, pRandom, pPos)) {
                    if (pLevel.getBlockState(pPos) == blockstate1) {
                        pLevel.sendBlockUpdated(pPos, saplingState, blockstate1, 2);
                    }

                    return true;
                } else {
                    pLevel.setBlock(pPos, saplingState, 4);
                    return false;
                }
            }
        }
    }
}
