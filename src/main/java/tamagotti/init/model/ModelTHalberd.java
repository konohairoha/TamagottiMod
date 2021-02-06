package tamagotti.init.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelTHalberd extends ModelBase {

	public ModelRenderer stick;
	public ModelRenderer front1;
	public ModelRenderer front2;
	public ModelRenderer front3;
	public ModelRenderer front4;

	public ModelTHalberd() {
		textureWidth = 64;
		textureHeight = 32;
		front4 = new ModelRenderer(this, 0, 6);
		front4.setRotationPoint(0.0F, 0.0F, 0.0F);
		front4.addBox(-1.3F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		setRotateAngle(front4, 0.7853981633974483F, 3.141592653589793F, 0.0F);
		front3 = new ModelRenderer(this, 0, 6);
		front3.setRotationPoint(0.0F, 0.0F, 0.0F);
		front3.addBox(-1.3F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		setRotateAngle(front3, 0.7853981633974483F, 3.141592653589793F, 0.0F);
		front2 = new ModelRenderer(this, 0, 6);
		front2.setRotationPoint(0.0F, 0.0F, 0.0F);
		front2.addBox(-1.3F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		setRotateAngle(front2, 0.7853981633974483F, 3.141592653589793F, 0.0F);
		front1 = new ModelRenderer(this, 0, 6);
		front1.setRotationPoint(0.0F, 0.0F, 0.0F);
		front1.addBox(-1.3F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		setRotateAngle(front1, 0.7853981633974483F, 3.141592653589793F, 0.0F);
		stick = new ModelRenderer(this, 0, 0);
		stick.setRotationPoint(0.0F, 0.0F, 0.0F);
		stick.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		setRotateAngle(stick, 0.7853981633974483F, 3.141592653589793F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(front4.offsetX, front4.offsetY, front4.offsetZ);
		GlStateManager.translate(front4.rotationPointX * f5, front4.rotationPointY * f5, front4.rotationPointZ * f5);
		GlStateManager.scale(10.0D, 1.0D, 1.0D);
		GlStateManager.translate(-front4.offsetX - 0.025, -front4.offsetY + 0.125, -front4.offsetZ + 0.01);
		GlStateManager.translate(-front4.rotationPointX * f5, -front4.rotationPointY * f5, -front4.rotationPointZ * f5);
		front4.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(front3.offsetX, front3.offsetY, front3.offsetZ);
		GlStateManager.translate(front3.rotationPointX * f5, front3.rotationPointY * f5, front3.rotationPointZ * f5);
		GlStateManager.scale(10.0D, 1.0D, 1.0D);
		GlStateManager.translate(-front3.offsetX - 0.025, -front3.offsetY - 0.125, -front3.offsetZ - 0.01);
		GlStateManager.translate(-front3.rotationPointX * f5, -front3.rotationPointY * f5, -front3.rotationPointZ * f5);
		front3.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(front2.offsetX, front2.offsetY, front2.offsetZ);
		GlStateManager.translate(front2.rotationPointX * f5, front2.rotationPointY * f5, front2.rotationPointZ * f5);
		GlStateManager.scale(12.0D, 1.0D, 1.0D);
		GlStateManager.translate(-front2.offsetX - 0.01425, -front2.offsetY, -front2.offsetZ);
		GlStateManager.translate(-front2.rotationPointX * f5, -front2.rotationPointY * f5, -front2.rotationPointZ * f5);
		front2.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(front1.offsetX, front1.offsetY, front1.offsetZ);
		GlStateManager.translate(front1.rotationPointX * f5, front1.rotationPointY * f5, front1.rotationPointZ * f5);
		GlStateManager.scale(1.0D, 6.0D, 6.0D);
		GlStateManager.translate(-front1.offsetX - 0.15F, -front1.offsetY, -front1.offsetZ + 0.03);
		GlStateManager.translate(-front1.rotationPointX * f5, -front1.rotationPointY * f5, -front1.rotationPointZ * f5);
		front1.render(f5);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.translate(stick.offsetX, stick.offsetY, stick.offsetZ);
		GlStateManager.translate(stick.rotationPointX * f5, stick.rotationPointY * f5, stick.rotationPointZ * f5);
		GlStateManager.scale(30.0D, 1.0D, 1.0D);
		GlStateManager.translate(-stick.offsetX, -stick.offsetY, -stick.offsetZ);
		GlStateManager.translate(-stick.rotationPointX * f5, -stick.rotationPointY * f5, -stick.rotationPointZ * f5);
		stick.render(f5);
		GlStateManager.popMatrix();
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
