package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VivicusHangingSignRenderer extends HangingSignRenderer {
    public VivicusHangingSignRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void renderSignWithText(SignBlockEntity pSignEntity, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, BlockState pState, SignBlock pSignBlock, WoodType pWoodType, Model pModel) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.9375, 0.5);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pSignBlock.getYRotationDegrees(pState)));
        pPoseStack.translate(0.0F, -0.3125F, 0.0F);
        renderVivicusSign(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pWoodType, pModel, pState);
        this.renderSignText(
                pSignEntity.getBlockPos(),
                pSignEntity.getFrontText(),
                pPoseStack,
                pBuffer,
                pPackedLight,
                pSignEntity.getTextLineHeight(),
                pSignEntity.getMaxTextLineWidth(),
                true
        );
        this.renderSignText(
                pSignEntity.getBlockPos(),
                pSignEntity.getBackText(),
                pPoseStack,
                pBuffer,
                pPackedLight,
                pSignEntity.getTextLineHeight(),
                pSignEntity.getMaxTextLineWidth(),
                false
        );
        pPoseStack.popPose();
    }
    
    private void renderVivicusSign(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, WoodType pWoodType, Model pModel, BlockState pState) {
        pPoseStack.pushPose();
        float f = this.getSignModelRenderScale();
        pPoseStack.scale(f, -f, -f);
        Material material = Sheets.getHangingSignMaterial(pWoodType);
        VertexConsumer vertexconsumer = material.buffer(pBuffer, pModel::renderType);
        var color = -1;
        if(pState.getBlock() instanceof ColorableVivicusBlock colorableVivicusBlock) {
            var dyeColor = pState.getValue(ModStateProperties.COLOR);
            color = colorableVivicusBlock.colorValues().get(dyeColor);
            vertexconsumer.setColor(color);
        }
        this.renderSignModel(pPoseStack, pPackedLight, pPackedOverlay, pModel, vertexconsumer, color);
        pPoseStack.popPose();
    }

    void renderSignModel(PoseStack pPoseStack, int pPackedLight, int pPackedOverlay, Model pModel, VertexConsumer pVertexConsumer, int color) {
        HangingSignRenderer.HangingSignModel hangingsignrenderer$hangingsignmodel = (HangingSignRenderer.HangingSignModel)pModel;
        hangingsignrenderer$hangingsignmodel.root.render(pPoseStack, pVertexConsumer, pPackedLight, pPackedOverlay, color);
    }
}
