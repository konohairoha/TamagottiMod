package tamagotti.init.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.model.ModelTHalberd;

@SideOnly(Side.CLIENT)
public abstract class ReforgedRender <T extends Entity> extends Render<T> {

	ModelTHalberd model;
	float scale = 1;
	int modifier = 0;

	protected ReforgedRender(RenderManager renderManager, ModelTHalberd model, float scale, int rotationModifier) {
		this(renderManager, model, rotationModifier);
		this.scale = scale;
	}

	protected ReforgedRender(RenderManager renderManager, ModelTHalberd model, int rotationModifier) {
		super(renderManager);
		this.model = model;
		modifier = rotationModifier;
	}

	@Override
	public void doRender(T bullet, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(bullet, x, y, z, yaw, partialTicks);
		this.renderEntityModel(bullet, x, y, z, yaw, partialTicks);
	}

	public void renderEntityModel(T theEntity, double x, double y, double z, float yaw, float partialTicks) {
		GL11.glPushMatrix();
		bindTexture(getEntityTexture(theEntity));
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotated(
				theEntity.prevRotationYaw + (theEntity.rotationYaw - theEntity.prevRotationYaw) * partialTicks - 90.0F,
				0.0F, 1.0F, 0.0F);
		GL11.glRotated(
				theEntity.prevRotationPitch + (theEntity.rotationPitch - theEntity.prevRotationPitch) * partialTicks,
				0.0F, 0.0F, 1.0F);
		model.render(theEntity, (float) x, (float) y, (float) z, yaw + modifier, partialTicks, 0.0475F);
		GL11.glPopMatrix();
	}

}
