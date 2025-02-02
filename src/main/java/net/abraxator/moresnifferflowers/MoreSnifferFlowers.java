package net.abraxator.moresnifferflowers;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.abraxator.moresnifferflowers.compat.quark.OtherModEvents;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTreeDecoratorTypes;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModTrunkPlacerTypes;
import net.abraxator.moresnifferflowers.worldgen.feature.ModFeatures;
import net.abraxator.moresnifferflowers.worldgen.structures.ModStructureTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MoreSnifferFlowers.MOD_ID)
public class MoreSnifferFlowers {
    public static final String MOD_ID = "moresnifferflowers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MoreSnifferFlowers() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModLoot.CONDITIONS.register(modEventBus);
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
        ModStructureTypes.STRUCTURE_PIECE.register(modEventBus);
        ModBannerPatterns.BANNER_PATTERNS.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        if(ModList.get().isLoaded("quark")) {
            MinecraftForge.EVENT_BUS.addListener(OtherModEvents::onSimpleHarvest);
        }
        
        ModPacketHandler.init();
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        ModAdvancementCritters.init();
        
        event.enqueueWork(() -> {
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
            fireBlock.setFlammable(ModBlocks.VIVICUS_LEAVES_SPROUT.get(), 5, 20);

            ComposterBlock.add(0.3F, ModItems.DAWNBERRY_VINE_SEEDS.get());
            ComposterBlock.add(0.3F, ModItems.DAWNBERRY.get());
            ComposterBlock.add(0.3F, ModItems.AMBUSH_SEEDS.get());
            ComposterBlock.add(0.4F, ModItems.CAULORFLOWER_SEEDS.get());
            ComposterBlock.add(0.4F, ModItems.DYESPRIA_SEEDS.get());
            ComposterBlock.add(0.5F, ModItems.BONMEELIA_SEEDS.get());
            ComposterBlock.add(1.0F, ModItems.CROPRESSED_BEETROOT.get());
            ComposterBlock.add(1.0F, ModItems.CROPRESSED_NETHERWART.get());
            ComposterBlock.add(1.0F, ModItems.CROPRESSED_WHEAT.get());
            ComposterBlock.add(1.0F, ModItems.CROPRESSED_POTATO.get());
            ComposterBlock.add(1.0F, ModItems.CROPRESSED_CARROT.get());
            ComposterBlock.add(1.0F, ModBlocks.CORRUPTED_SAPLING.get());
            ComposterBlock.add(1.0F, ModBlocks.VIVICUS_SAPLING.get());
            ComposterBlock.add(1.0F, ModBlocks.CORRUPTED_LEAVES.get());
            ComposterBlock.add(1.0F, ModBlocks.VIVICUS_LEAVES.get());
            ModCauldronInteractions.bootstrap();
        });
    }
    
    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String sLoc(String path) {
        return loc(path).toString();
    }
}
