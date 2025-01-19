package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.gui.screen.RebrewingStandScreen;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.block.BondripiaModel;
import net.abraxator.moresnifferflowers.client.model.block.CropressorModel;
import net.abraxator.moresnifferflowers.client.model.block.GiantCropModels;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.client.particle.AmbushParticle;
import net.abraxator.moresnifferflowers.client.particle.CarrotParticle;
import net.abraxator.moresnifferflowers.client.particle.FlyParticle;
import net.abraxator.moresnifferflowers.client.particle.GiantCropParticle;
import net.abraxator.moresnifferflowers.client.renderer.block.*;
import net.abraxator.moresnifferflowers.client.renderer.entity.BoblingRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.CorruptedProjectileRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.DragonflyRenderer;
import net.abraxator.moresnifferflowers.client.renderer.entity.ModBoatRenderer;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistration {
    public static void init(FMLCommonSetupEvent event) {
        Sheets.addWoodType(ModWoodTypes.CORRUPTED);
        Sheets.addWoodType(ModWoodTypes.VIVICUS);
        ModItemProperties.register();
        MenuScreens.register(ModMenuTypes.REBREWING_STAND.get(), RebrewingStandScreen::new);
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
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BOBLING.get(), BoblingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CORRUPTED_SLIME_BALL.get(), CorruptedProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MOD_CORRUPTED_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
        event.registerEntityRenderer(ModEntityTypes.MOD_CORRUPTED_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));
        event.registerEntityRenderer(ModEntityTypes.MOD_VIVICUS_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
        event.registerEntityRenderer(ModEntityTypes.MOD_VIVICUS_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));
        event.registerEntityRenderer(ModEntityTypes.JAR_OF_ACID.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.XBUSH.get(), AmbushBlockEntityRenderer::new);
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
        event.registerSpriteSet(ModParticles.GARBUSH.get(), AmbushParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GIANT_CROP.get(), GiantCropParticle.Provider::new);
    }
    
    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo iModFileInfo = ModList.get().getModFileById(MoreSnifferFlowers.MOD_ID);
            if(iModFileInfo == null) {
                MoreSnifferFlowers.LOGGER.error("Could not find More Sniffer Flowers mod file info; built-in resource packs will be missing!");
            }

            IModFile modFile = iModFileInfo.getFile();
            event.addRepositorySource(pOnLoad -> {
                Pack rtx = Pack.readMetaAndCreate(
                        MoreSnifferFlowers.loc("more_sniffer_flowers_rtx").toString(),
                        Component.literal("RTX More Sniffer Flowers"),
                        false,
                        pId -> new PathPackResources(pId, modFile.findResource("resourcepacks/more_sniffer_flowers_rtx"), true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN);
                if(rtx != null) {
                    pOnLoad.accept(rtx);
                }
            });

            event.addRepositorySource(pOnLoad -> {

                Pack customStyleGUI = Pack.readMetaAndCreate(
                        MoreSnifferFlowers.loc("more_sniffer_flowers_boring").toString(),
                        Component.literal("Boring More Sniffer Flowers"),
                        false,
                        pId -> new PathPackResources(pId, modFile.findResource("resourcepacks/more_sniffer_flowers_boring"), true),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN);
                if(customStyleGUI != null) {
                    pOnLoad.accept(customStyleGUI);
                }
            });
        }
    }
}
