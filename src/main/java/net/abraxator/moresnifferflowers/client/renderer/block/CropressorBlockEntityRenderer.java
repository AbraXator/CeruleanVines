package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CropressorBlockEntityRenderer implements BlockEntityRenderer<CropressorBlockEntity> {
    private static final Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/cropressor"));

    public CropressorBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        ModelPart modelPart = pContext.bakeLayer(ModModelLayerLocations.CROPRESSOR);
    }

    @Override
    public void render(CropressorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockState blockState = pBlockEntity.getBlockState();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction direction = pBlockEntity.getBlockState().getValue(CropressorBlockBase.FACING).getOpposite();

        var progress = pBlockEntity.progress;
        if(blockState.is(ModBlocks.CROPRESSOR_OUT.get()) && progress > 0) {
            double scale = 100D;
            double d = progress / scale;
            Vec3 factor = switch (direction) {
                case NORTH -> new Vec3(0.5, 0, (1 - d));
                case EAST -> new Vec3(d, 0, 0.55);
                case SOUTH -> new Vec3(0.5, 0, d);
                default -> new Vec3((1 - d), 0, 0.55);
            };

            pPoseStack.pushPose();
            pPoseStack.translate(factor.x, 0.35, factor.z);
            pPoseStack.scale(0.4F, 0.4F, 0.4F);
            itemRenderer.renderStatic(pBlockEntity.result, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, pBlockEntity.getLevel(), ((int) pBlockEntity.getBlockPos().asLong()));
            pPoseStack.popPose();
        }
    }
}
