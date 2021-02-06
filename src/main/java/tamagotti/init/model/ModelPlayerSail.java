package tamagotti.init.model;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class ModelPlayerSail extends ModelPlayer {

	public ModelPlayerSail(float modelSize, boolean smallArmsIn) {
		super(modelSize, smallArmsIn);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.bipedLeftArmwear.rotateAngleY = this.bipedLeftArm.rotateAngleY = (float)Math.PI;
		this.bipedLeftArmwear.rotateAngleX = this.bipedLeftArm.rotateAngleX = 0;
		this.bipedLeftArmwear.rotateAngleZ = this.bipedLeftArm.rotateAngleZ = -2.9F;
		this.bipedLeftArmwear.rotationPointY = this.bipedLeftArm.rotationPointY = 1;
		this.bipedLeftArmwear.rotationPointX = this.bipedLeftArm.rotationPointX = 4;

		this.bipedRightArmwear.rotateAngleY = this.bipedRightArm.rotateAngleY = (float)Math.PI;
		this.bipedRightArmwear.rotateAngleX = this.bipedRightArm.rotateAngleX = 0;
		this.bipedRightArmwear.rotateAngleZ = this.bipedRightArm.rotateAngleZ = 2.9F;
		this.bipedRightArmwear.rotationPointY = this.bipedRightArm.rotationPointY = 1;
		this.bipedRightArmwear.rotationPointX = this.bipedRightArm.rotationPointX = -4;
	}
}
