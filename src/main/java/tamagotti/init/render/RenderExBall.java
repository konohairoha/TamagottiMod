package tamagotti.init.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderExBall<T extends Entity> extends Render<T> {

	private static final ResourceLocation tex = new ResourceLocation("tamagottimod:textures/entity/ritter.png");

	public RenderExBall(RenderManager render) {
		super(render);
	}

	public void renderBullet(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.bindEntityTexture(entity);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90F, 0F, 1F, 0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0F, 0F, 1F);
        Tessellator tesla = Tessellator.getInstance();
		BufferBuilder buf = tesla.getBuffer();
		int i = 0;
		float f = 0F;
		float f1 = 0.5F;
		float f2 = (0 + i * 10) / 32F;
		float f3 = (5 + i * 10) / 32F;
		float f4 = 0F;
		float f5 = 0.15625F;
		float f6 = (5 + i * 10) / 32F;
		float f7 = (10 + i * 10) / 32F;
		float f8 = 0.15625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.rotate(45F, 1F, 0F, 0F);
		GlStateManager.scale(f8, f8, f8);
		GlStateManager.translate(-4F, 0F, 0F);
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}
		GlStateManager.glNormal3f(f8, 0F, 0F);
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-7D, -2D, -2D).tex(f4, f6).endVertex();
		buf.pos(-7D, -2D, 2D).tex(f5, f6).endVertex();
		buf.pos(-7D, 2D, 2D).tex(f5, f7).endVertex();
		buf.pos(-7D, 2D, -2D).tex(f4, f7).endVertex();
		tesla.draw();
		GlStateManager.glNormal3f(-f8, 0F, 0F);
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-7D, 2D, -2D).tex(f4, f6).endVertex();
		buf.pos(-7D, 2D, 2D).tex(f5, f6).endVertex();
		buf.pos(-7D, -2D, 2D).tex(f5, f7).endVertex();
		buf.pos(-7D, -2D, -2D).tex(f4, f7).endVertex();
		tesla.draw();
		for (int j = 0; j < 4; ++j) {
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.glNormal3f(0F, 0F, f8);
			buf.begin(7, DefaultVertexFormats.POSITION_TEX);
			buf.pos(-8D, -2D, 0D).tex(f, f2).endVertex();
			buf.pos(8D, -2D, 0D).tex(f1, f2).endVertex();
			buf.pos(8D, 2D, 0D).tex(f1, f3).endVertex();
			buf.pos(-8D, 2D, 0D).tex(f, f3).endVertex();
			tesla.draw();
		}
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderBullet(var1, var2, var4, var6, var8, var9);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}
}
