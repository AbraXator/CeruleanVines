package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.CaulorflowerBlock;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.block.BondripiaModel;
import net.abraxator.moresnifferflowers.client.model.block.CropressorModel;
import net.abraxator.moresnifferflowers.client.model.block.GiantCropModels;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.client.particle.*;
import net.abraxator.moresnifferflowers.client.renderer.block.*;
import net.abraxator.moresnifferflowers.client.renderer.entity.BoblingRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.CorruptedProjectileRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.DragonflyRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.ModBoatRenderer;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.awt.*;
import java.util.Optional;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        Sheets.addWoodType(ModWoodTypes.CORRUPTED);
        Sheets.addWoodType(ModWoodTypes.VIVICUS);
        ModItemProperties.register();
    }
    
    @SubscribeEvent
    public static void onRegisterMenuScreenEvent(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.REBREWING_STAND.get(), RebrewingStandScreen::new);
    }
    
    @SubscribeEvent
    public static void onEntityRenderersRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //ENTITY
        event.registerLayerDefinition(ModModelLayerLocations.BOBLING, BoblingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayerLocations.DRAGONFLY, DragonflyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayerLocations.CORRUPTED_PROJECTILE, CorruptedProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayerLocations.CORRUPTED_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayerLocations.CORRUPTED_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayerLocations.VIVICUS_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayerLocations.VIVICUS_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);

        //BLOCK
        event.registerLayerDefinition(ModModelLayerLocations.GIANT_CARROT, GiantCropModels::createGiantCarrotLayer);
        event.registerLayerDefinition(ModModelLayerLocations.GIANT_POTATO, GiantCropModels::createGiantPotatoLayer);
        event.registerLayerDefinition(ModModelLayerLocations.GIANT_NETHERWART, GiantCropModels::createNetherwartLayer);
        event.registerLayerDefinition(ModModelLayerLocations.GIANT_BEETROOT, GiantCropModels::createBeetrootLayer);
        event.registerLayerDefinition(ModModelLayerLocations.GIANT_WHEAT, GiantCropModels::createWheatLayer);
        event.registerLayerDefinition(ModModelLayerLocations.CROPRESSOR, CropressorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayerLocations.BONDRIPIA, BondripiaModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BOBLING.get(), BoblingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CORRUPTED_SLIME_BALL.get(), CorruptedProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MOD_CORRUPTED_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
        event.registerEntityRenderer(ModEntityTypes.MOD_CORRUPTED_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));
        event.registerEntityRenderer(ModEntityTypes.MOD_VIVICUS_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
        event.registerEntityRenderer(ModEntityTypes.MOD_VIVICUS_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));
    }

    @SubscribeEvent
    public static void blockRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.AMBUSH.get(), AmbushBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GIANT_CROP.get(), GiantCropBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CROPRESSOR.get(), CropressorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DYESPRIA_PLANT.get(), DyespriaPlantBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.VIVICUS_SIGN.get(), VivicusSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.VIVICUS_HANGING_SIGN.get(), VivicusHangingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BONDRIPIA.get(), BondripiaBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.FLY.get(), FlyParticle.Provider::new);
        event.registerSpriteSet(ModParticles.CARROT.get(), CarrotParticle.Provider::new);
        event.registerSpriteSet(ModParticles.AMBUSH.get(), AmbushParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GIANT_CROP.get(), GiantCropParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GIANT_CROP.get(), GiantCropParticle.Provider::new);
        event.registerSpriteSet(ModParticles.BONDRIPIA.get(), BondripiaParticle.BondripiaParticleProvider::new);
    }

    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> {
            Colorable colorable = ((Colorable) pState.getBlock());
            Dye dye = colorable.getDyeFromBlock(pState);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                if (pTintIndex == 0) {
                    int startRed = (color >> 16) & 0xFF;
                    int startGreen = (color >> 8) & 0xFF;
                    int startBlue = color & 0xFF;
                    float[] colorHSB = Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                    return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
                }
                if (pTintIndex == 1) {
                    return color;
                }
            }
            return -1;
        }, ModBlocks.CAULORFLOWER.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
            var colorable = ((ColorableVivicusBlock) pState.getBlock());
            if(pTintIndex == 0) {
                var dyedValue = Dye.colorForDye(colorable, pState.getValue(colorable.getColorProperty()));
                var color = colorable.getDyeFromBlock(pState).color();
                
                if(pState.is(ModBlocks.VIVICUS_LEAVES) || pState.is(ModBlocks.VIVICUS_LEAVES_SPROUT) || pState.is(ModBlocks.VIVICUS_SAPLING)) {
                    int startRed = (dyedValue >> 16) & 0xFF;
                    int startGreen = (dyedValue >> 8) & 0xFF;
                    int startBlue = dyedValue & 0xFF;
                    float[] colorHSB =  Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                    assert pPos != null;
                    float hue = (float) (colorHSB[0] + 1 + Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ()) / 15);

                    if (colorHSB[1] < 0.3 && colorHSB[2] < 0.8){
                        colorHSB[2] = (float) (colorHSB[2] - 1 + Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 15;
                    }

                    if (colorHSB[1] < 0.3){
                        colorHSB[1] = (float) (colorHSB[1] + 1 + Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 12;
                    }


                    return Color.HSBtoRGB(hue, colorHSB[1], colorHSB[2]);
                }
                
                return dyedValue;
            }
            
            return -1;
        }, ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(), 
           ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.VIVICUS_PLANKS.get(), ModBlocks.VIVICUS_STAIRS.get(), 
           ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(), 
           ModBlocks.VIVICUS_DOOR.get(), ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get(), 
           ModBlocks.VIVICUS_BUTTON.get(), ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.VIVICUS_SAPLING.get(), 
           ModBlocks.VIVICUS_LEAVES_SPROUT.get(), ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_HANGING_SIGN.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> {
            Dye dye = Dye.getDyeFromStack(pStack);
            if(pTintIndex != 0 || dye.isEmpty()) {
                return -1;
            } else {
                return Dye.colorForDye(((DyespriaItem) pStack.getItem()), dye.color());
            }
        }, ModItems.DYESPRIA.get());
        event.register((pStack, pTintIndex) -> {
            return pTintIndex > 0 ? -1 : pStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
        }, ModItems.EXTRACTED_BOTTLE.get(), ModItems.REBREWED_POTION.get(), ModItems.REBREWED_SPLASH_POTION.get(), ModItems.REBREWED_LINGERING_POTION.get());
    }


    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFile iModFileInfo = ModList.get().getModFileById(MoreSnifferFlowers.MOD_ID).getFile();
            event.addRepositorySource(pOnLoad -> {
                String name = "more_sniffer_flowers_rtx";
                var pack = Pack.readMetaAndCreate(
                        new PackLocationInfo(name, Component.literal("More Sniffer Flowers RTX"), PackSource.BUILT_IN, Optional.empty()),
                        new Pack.ResourcesSupplier() {
                            @Override
                            public PackResources openPrimary(PackLocationInfo pLocation) {
                                return new PathPackResources(pLocation, iModFileInfo.findResource("resourcepacks/" + name));
                            }

                            @Override
                            public PackResources openFull(PackLocationInfo pLocation, Pack.Metadata pMetadata) {
                                return openPrimary(pLocation);
                            }
                        },
                        PackType.CLIENT_RESOURCES,
                        new PackSelectionConfig(false, Pack.Position.TOP, false));
                if(pack != null) {
                    pOnLoad.accept(pack);
                }
            });

            event.addRepositorySource(pOnLoad -> {
                String name = "more_sniffer_flowers_boring";
                    var pack = Pack.readMetaAndCreate(
                            new PackLocationInfo(name, Component.literal("More Sniffer Flowers Boring"),  PackSource.BUILT_IN, Optional.empty()),
                            new Pack.ResourcesSupplier() {
                                @Override
                                public PackResources openPrimary(PackLocationInfo packLocationInfo) {
                                    return new PathPackResources(packLocationInfo, iModFileInfo.findResource("resourcepacks/" + name));
                                }

                                @Override
                                public PackResources openFull(PackLocationInfo packLocationInfo, Pack.Metadata metadata) {
                                    return openPrimary(packLocationInfo);
                                }
                            },
                            PackType.CLIENT_RESOURCES,
                            new PackSelectionConfig(false, Pack.Position.TOP, false));
                    if(pack != null) {
                        pOnLoad.accept(pack);
                    }
            });
        }
    }
}
