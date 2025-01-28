package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.ModChestBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ModBoatRenderer extends BoatRenderer {
    private final Map<ModBoatEntity.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ModBoatRenderer(EntityRendererProvider.Context pContext, boolean pChestBoat) {
        super(pContext, pChestBoat);
        this.boatResources = Stream.of(ModBoatEntity.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type,
               type -> Pair.of(ResourceLocation.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, getTextureLocation(type, pChestBoat)), this.createBoatModel(pContext, type, pChestBoat))));    
    }

    @Override
    public void render(Boat pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 0.375F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pEntityYaw));
        float f = (float)pEntity.getHurtTime() - pPartialTicks;
        float f1 = pEntity.getDamage() - pPartialTicks;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f > 0.0F) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)pEntity.getHurtDir()));
        }

        float f2 = pEntity.getBubbleAngle(pPartialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            pPoseStack.mulPose(new Quaternionf().setAngleAxis(pEntity.getBubbleAngle(pPartialTicks) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        Pair<ResourceLocation, ListModel<Boat>> pair = getModelWithLocation(pEntity);
        ResourceLocation resourcelocation = pair.getFirst();
        ListModel<Boat> listmodel = pair.getSecond();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, boatColor(pEntity));
        if (!pEntity.isUnderWater()) {
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.waterMask());
            if (listmodel instanceof WaterPatchModel waterpatchmodel) {
                waterpatchmodel.waterPatch().render(pPoseStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        pPoseStack.popPose();
    }

    private static String getTextureLocation(ModBoatEntity.Type pType, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/chest_boat/" + pType.getName() + ".png" : "textures/entity/boat/" + pType.getName() + ".png";
    }

    private ListModel<Boat> createBoatModel(EntityRendererProvider.Context pContext, ModBoatEntity.Type pType, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? ModBoatRenderer.createChestBoatModelName(pType) : ModBoatRenderer.createBoatModelName(pType);
        ModelPart modelpart = pContext.bakeLayer(modellayerlocation);
        return pChestBoat ? new ChestBoatModel(modelpart) : new BoatModel(modelpart);
    }

    public static ModelLayerLocation createBoatModelName(ModBoatEntity.Type pType) {
        return createLocation("boat/" + pType.getName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(ModBoatEntity.Type pType) {
        return createLocation("chest_boat/" + pType.getName(), "main");
    }

    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, pPath), pModel);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return this.boatResources.get(modBoat.getModVariant());
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return this.boatResources.get(modChestBoatEntity.getModVariant());
        } else {
            return null;
        }
    }
    
    private ModBoatEntity.Type getType(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return modBoat.getModVariant();
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return modChestBoatEntity.getModVariant();
        } else {
            return null;
        }
    }
    
    private int boatColor(Boat boat) {
        if(this.getType(boat).equals(ModBoatEntity.Type.VIVICUS)) {
            if(boat instanceof VivicusBoatEntity vivicusBoat) {
                return vivicusBoat.colorValues().get(vivicusBoat.getColor());
            } else if(boat instanceof VivicusChestBoatEntity vivicusChestBoat) {
                return vivicusChestBoat.colorValues().get(vivicusChestBoat.getColor());
            }
        }
        
        return -1;
    }
}
