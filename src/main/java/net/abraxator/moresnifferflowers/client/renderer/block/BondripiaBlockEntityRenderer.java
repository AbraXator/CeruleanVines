package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BondripiaBlockEntityRenderer<T extends BondripiaBlockEntity> implements BlockEntityRenderer<T> {
    private ModelPart model;
    private static final Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/bondripia"));

    public BondripiaBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.model = pContext.bakeLayer(ModModelLayerLocations.BONDRIPIA);
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(pBlockEntity.getBlockState().getValue(ModStateProperties.CENTER) && pBlockEntity.getBlockState().getValue(ModStateProperties.AGE_2) >= 2) {
            pPoseStack.translate(0.5, 1.5, 0.5);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(180));
            this.model.render(pPoseStack, TEXTURE.buffer(pBufferSource, RenderType::entityCutout), pPackedLight, pPackedOverlay);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return AABB.INFINITE;
    }
}
