package tamagotti.init.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.entity.projectile.EntityTCushion_Y;
import tamagotti.init.model.ModelTCushion_A;

public class RenderTCushion_Y extends Render<EntityTCushion_Y> {

	private static final ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/entity/tcushion_y.png");
	private static final ModelTCushion_A MODEL = new ModelTCushion_A(false);

	public RenderTCushion_Y(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.4F;
		this.shadowOpaque = 0.4F;
	}

	@Override
	public void doRender(EntityTCushion_Y entity, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.scale(-1F, -1F, 1F);
		this.bindTexture(TEX);
		float rotX = -entity.rotationPitch;
		float rotY = 180 - entity.rotationYaw;
		float rotZ = 0F;
		GlStateManager.rotate(rotY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotX, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(rotZ, 0.0F, 0.0F, 1.0F);
		MODEL.render(0.0625F, null);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, yaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTCushion_Y entity) {
		return TEX;
	}
}
