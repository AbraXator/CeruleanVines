package net.abraxator.moresnifferflowers;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.abraxator.moresnifferflowers.client.ClientEvents;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTreeDecoratorTypes;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.abraxator.moresnifferflowers.worldgen.feature.ModFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.slf4j.Logger;

@Mod(MoreSnifferFlowers.MOD_ID)
public class MoreSnifferFlowers {
    public static final String MOD_ID = "moresnifferflowers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreSnifferFlowers(IEventBus modEventBus, Dist dist) {
        if(dist.isClient()) modEventBus.addListener(ClientEvents::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener((RegisterEvent e) -> ModAdvancementCritters.init());

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModMobEffects.EFFECTS.register(modEventBus);
        ModSoundEvents.SOUNDS.register(modEventBus);
        ModPaintings.PAINTINGS.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModEntityTypes.ENTITIES.register(modEventBus);
        ModTrunkPlacerTypes.TRUNKS.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModTreeDecoratorTypes.DECORATORS.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModEntityDataSerializers.SERIALIZERS.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ModStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
        AxeItem.STRIPPABLES.put(ModBlocks.CORRUPTED_LOG.get(), ModBlocks.STRIPPED_CORRUPTED_LOG.get());
        AxeItem.STRIPPABLES.put(ModBlocks.VIVICUS_LOG.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get());

        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        pot.addPlant(ModBlocks.DYESPRIA_PLANT.getId(), ModBlocks.POTTED_DYESPRIA);
        pot.addPlant(ModBlocks.CORRUPTED_SAPLING.getId(), ModBlocks.POTTED_CORRUPTED_SAPLING);
        pot.addPlant(ModBlocks.VIVICUS_SAPLING.getId(), ModBlocks.POTTED_VIVICUS_SAPLING);

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.setFlammable(ModBlocks.CORRUPTED_LOG.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_WOOD.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.STRIPPED_CORRUPTED_LOG.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.STRIPPED_CORRUPTED_WOOD.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_PLANKS.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_STAIRS.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_SLAB.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_FENCE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_FENCE_GATE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_DOOR.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_TRAPDOOR.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_PRESSURE_PLATE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_BUTTON.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_LEAVES.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.CORRUPTED_SAPLING.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_LOG.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_WOOD.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.STRIPPED_VIVICUS_LOG.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.STRIPPED_VIVICUS_WOOD.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_PLANKS.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_STAIRS.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_SLAB.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_FENCE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_FENCE_GATE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_DOOR.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_TRAPDOOR.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_PRESSURE_PLATE.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_BUTTON.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_LEAVES.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.VIVICUS_SAPLING.get(), 5, 20);
        fireBlock.setFlammable(ModBlocks.SPROUTING_VIVICUS_LEAVES.get(), 5, 20);
    }

        public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String sLoc(String path) {
        return loc(path).toString();
    }
}
