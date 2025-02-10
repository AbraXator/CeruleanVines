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

		PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 50).addBox(2.6066F, -7.2F, 10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(4, 50).addBox(-18.6066F, -7.2F, -10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -16.0F, 0.0F, -0.7854F, -3.1416F));

		PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 50).addBox(-18.6066F, -7.2F, 10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(4, 50).addBox(2.6066F, -7.2F, -10.6066F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -16.0F, 0.0F, 0.7854F, -3.1416F));

		PartDefinition cube_r3 = root.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 50).mirror().addBox(-19.3137F, -7.2F, 9.8995F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -6.3F, -15.0F, 0.0F, -0.7854F, 3.1416F));

		PartDefinition cube_r4 = root.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 50).mirror().addBox(-17.8995F, -7.2F, -11.3137F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -6.3F, -15.0F, 0.0F, 0.7854F, 3.1416F));

		PartDefinition cube_r5 = root.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(4, 50).addBox(3.3137F, -7.2F, 9.8995F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.3F, -15.0F, 0.0F, 0.7854F, -3.1416F));

		PartDefinition cube_r6 = root.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(4, 50).addBox(1.8995F, -7.2F, -11.3137F, 16.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.3F, -15.0F, 0.0F, -0.7854F, -3.1416F));

		PartDefinition cube_r7 = root.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 39).addBox(-10.2929F, -9.2F, -0.7071F, 22.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, 0.7854F, -3.1416F));

		PartDefinition cube_r8 = root.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(12, 39).addBox(-11.7071F, -9.2F, -0.7071F, 22.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, -0.7854F, -3.1416F));

		PartDefinition cube_r9 = root.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 26).addBox(8.4922F, 4.7698F, -6.2145F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, -0.2921F, 0.7401F, 2.7222F));

		PartDefinition cube_r10 = root.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(59, 0).addBox(-7.0F, 0.0F, -6.0F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.4263F, -6.1904F, -4.5026F, 0.2717F, 0.6563F, -2.7538F));

		PartDefinition cube_r11 = root.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(14, 27).addBox(-7.0F, 0.0F, -6.0F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.8607F, -6.2296F, -5.1127F, 0.2717F, -0.6563F, 2.7538F));

		PartDefinition cube_r12 = root.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(14, 25).addBox(-7.0F, 0.0F, -6.0F, 14.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.4399F, -6.2101F, -26.3957F, -0.2717F, -0.6563F, -2.7538F));

		PartDefinition cube_r13 = root.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 1.725F, -11.0F, 20.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.3F, -15.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r14 = root.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(-44, 68).addBox(-22.0F, 0.0F, -22.0F, 44.0F, 0.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -16.0F, 0.0009F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}