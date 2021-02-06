package tamagotti.init.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTClystal;

@SideOnly(Side.CLIENT)
public class RenderTCrystal extends Render<EntityTClystal> {

    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("tamagottimod:textures/entity/tcrystal.png");
    private final ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);
    private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);

	public RenderTCrystal(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityTClystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float f = entity.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		this.bindTexture(ENDER_CRYSTAL_TEXTURES);
		float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
		f1 = f1 * f1 + f1;

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

			this.modelEnderCrystalNoBase.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTClystal entity) {
		return ENDER_CRYSTAL_TEXTURES;
	}

	@Override
	public boolean shouldRender(EntityTClystal livingEntity, ICamera camera, double camX, double camY,
			double camZ) {
		return super.shouldRender(livingEntity, camera, camX, camY, camZ);
	}
}
