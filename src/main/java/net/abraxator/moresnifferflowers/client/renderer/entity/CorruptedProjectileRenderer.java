package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CorruptedProjectileRenderer extends EntityRenderer<CorruptedProjectile> {
    public static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/entity/corrupted_projectile.png");
    private final CorruptedProjectileModel model;
    
    public CorruptedProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CorruptedProjectileModel(pContext.bakeLayer(ModModelLayerLocations.CORRUPTED_PROJECTILE));
    }

    @Override
    public void render(CorruptedProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        if(pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25)) {
            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 180F));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
            pPoseStack.translate(0, -0.5, 0);
            pPoseStack.scale(0.6F, 0.6F, 0.6F);
            this.model.renderToBuffer(
                    pPoseStack,
                    pBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(pEntity))),
                    pPackedLight,
                    OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBufferSource, pPackedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CorruptedProjectile pEntity) {
        return TEXTURE;
    }
}
