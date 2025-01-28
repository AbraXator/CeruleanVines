package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.accesstransformer.generated.AtParser;

public class BoblingRenderer extends MobRenderer<BoblingEntity, BoblingModel<BoblingEntity>> {
    public static final ResourceLocation CORRUPTED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/corrupted_bobling.png");
    public static final ResourceLocation CURED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bobling.png");
    public static final ResourceLocation BONMEELED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bonmeeled_bobling.png");
    
    public BoblingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BoblingModel<>(pContext.bakeLayer(ModModelLayerLocations.BOBLING)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(BoblingEntity pEntity) {
        if (pEntity.getBoblingType() == BoblingEntity.Type.CORRUPTED) {
            return CORRUPTED_TEXTURE;
        } else {
            return CURED_TEXTURE;
        }
    }
}
