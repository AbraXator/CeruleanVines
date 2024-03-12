package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class GiantCropBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	private final Map<Block, ModelPart> modelPartMap = new HashMap<>();
	private final ModelPart carrot;
	private final ModelPart potato;
	private final ModelPart netherwart;
	private final ModelPart beetroot;
	private final ModelPart wheat;

	public GiantCropBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
		ModelPart carrotModelPart = pContext.bakeLayer(ModModelLayerLocations.GIANT_CARROT);
		this.carrot = carrotModelPart.getChild("root.json");
		this.modelPartMap.put(ModBlocks.GIANT_CARROT.get(), this.carrot);
		ModelPart potatoModelPart = pContext.bakeLayer(ModModelLayerLocations.GIANT_POTATO);
		this.potato = potatoModelPart.getChild("root.json");
		this.modelPartMap.put(ModBlocks.GIANT_POTATO.get(), this.potato);
		ModelPart netherwartModelPart = pContext.bakeLayer(ModModelLayerLocations.GIANT_NETHERWART);
		this.netherwart = netherwartModelPart.getChild("root.json");
		this.modelPartMap.put(ModBlocks.GIANT_NETHERWART.get(), this.netherwart);
		ModelPart beetrootModelPart = pContext.bakeLayer(ModModelLayerLocations.GIANT_BEETROOT);
		this.beetroot = beetrootModelPart.getChild("root.json");
		this.modelPartMap.put(ModBlocks.GIANT_BEETROOT.get(), this.beetroot);
		ModelPart wheatModelPart = pContext.bakeLayer(ModModelLayerLocations.GIANT_WHEAT);
		this.wheat = wheatModelPart.getChild("root.json");
		this.modelPartMap.put(ModBlocks.GIANT_WHEAT.get(), this.wheat);
	}

	@Override
	public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		BlockState blockState = pBlockEntity.getBlockState();
		String path = blockState.getBlock().getDescriptionId().replace("block." + MoreSnifferFlowers.MOD_ID + ".", "");
		Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/" + path));
		VertexConsumer vertexConsumer = TEXTURE.buffer(pBufferSource, RenderType::entityCutout);

		if(blockState.is(ModTags.ModBlockTags.GIANT_CROPS) && blockState.getValue(GiantCropBlock.MODEL_POSITION) != GiantCropBlock.ModelPos.NONE) {
			var modelPos = blockState.getValue(GiantCropBlock.MODEL_POSITION);
			pPoseStack.pushPose();
			pPoseStack.translate(modelPos.x, modelPos.y, modelPos.z);
			pPoseStack.mulPose(new Quaternionf().rotateX((float) (Math.PI)));
			modelPartMap.get(blockState.getBlock()).render(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay);
			pPoseStack.popPose();
		}
	}
}