package net.abraxator.moresnifferflowers.client.model.block;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BondripiaModel {
	private final ModelPart root;

	public BondripiaModel(ModelPart root) {
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 18.5F, 16.0F));

		PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-16.6066F, -4.2F, 10.6066F, 12.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(32, 88).addBox(2.6066F, -7.2F, -10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-15.8995F, -4.2F, -11.3137F, 12.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(32, 88).addBox(3.3137F, -7.2F, 9.8995F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(40, 77).addBox(-10.2929F, -9.2F, -0.7071F, 22.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, 0.7854F, -3.1416F));

		PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 24).addBox(4.6066F, -4.2F, 10.6066F, 12.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(32, 88).addBox(-18.6066F, -7.2F, -10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-17.3137F, -4.2F, 9.8995F, 12.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(32, 88).addBox(1.8995F, -7.2F, -11.3137F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(40, 77).addBox(-11.7071F, -9.2F, -0.7071F, 22.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, -0.7854F, -3.1416F));

		PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(59, 12).addBox(8.4922F, 4.7698F, -7.4645F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, -0.2921F, 0.7401F, 2.7222F));

		PartDefinition cube_r4 = root.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(59, 0).addBox(-21.1434F, 4.3445F, -5.9497F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.2921F, 0.7401F, -2.7222F));

		PartDefinition cube_r5 = root.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(42, 65).addBox(7.1434F, 4.3445F, -5.9497F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.2921F, -0.7401F, 2.7222F));

		PartDefinition cube_r6 = root.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 66).addBox(-22.4922F, 4.7698F, -7.4645F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, -0.2921F, -0.7401F, -2.7222F));

		PartDefinition cube_r7 = root.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(51, 34).addBox(-10.0F, 1.725F, -11.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(-44, 99).addBox(-22.0F, 3.7F, -23.0F, 44.0F, 0.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}
}