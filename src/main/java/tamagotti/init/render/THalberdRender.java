package tamagotti.init.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.model.ModelTHalberd;

@SideOnly(Side.CLIENT)
public abstract class THalberdRender <T extends Entity> extends Render<T> {

	ModelTHalberd model;
	float scale = 1;
	int modifier = 0;

	protected THalberdRender(RenderManager renderManager, ModelTHalberd model, float scale, int rotationModifier) {
		this(renderManager, model, rotationModifier);
		this.scale = scale;
	}

	protected THalberdRender(RenderManager renderManager, ModelTHalberd model, int rotationModifier) {
		super(renderManager);
		this.model = model;
		modifier = rotationModifier;
	}

	@Override
	public void doRender(T bullet, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(bullet, x, y, z, yaw, partialTicks);
		renderEntityModel(bullet, x, y, z, yaw, partialTicks);
	}

	@Override
	protected abstract ResourceLocation getEntityTexture(T entity);

	public void renderEntityModel(T entity, double x, double y, double z, float yaw, float tick) {
		GL11.glPushMatrix();
		bindTexture(getEntityTexture(entity));
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotated(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * tick - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotated(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * tick, 0.0F, 0.0F, 1.0F);
		model.render(entity, (float) x, (float) y, (float) z, yaw + modifier, tick, 0.0475F);
		GL11.glPopMatrix();
	}
}
