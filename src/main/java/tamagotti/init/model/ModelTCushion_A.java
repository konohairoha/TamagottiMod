package tamagotti.init.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import tamagotti.init.entity.projectile.EntityTCushion_A;

public class ModelTCushion_A extends ModelBase {

	ModelRenderer cution;
	private final boolean isBaked;

	public ModelTCushion_A(boolean baked) {
		isBaked = baked;
		textureWidth = 48;
		textureHeight = 16;

		cution = new ModelRenderer(this, 0, 0);
		cution.addBox(-8F, -1.125F, -8F, 12, 1, 12);
		cution.setRotationPoint(0F, 0F, 0F);
		cution.setTextureSize(48, 16);
	}

	public void render(float scale, EntityTCushion_A entity) {
		render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float HeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, HeadYaw, headPitch, scale, entity);
		cution.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}

	public boolean isBaked() {
		return isBaked;
	}
}