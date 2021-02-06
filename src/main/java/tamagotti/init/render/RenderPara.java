package tamagotti.init.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;

public class RenderPara {

	public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer, double x, double y, double z, float partialTicks, boolean firstPerson) {

		Random random = new Random();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		random.setSeed(entity.getEntityId() * entity.getEntityId() * 3121 + entity.getEntityId() * 45238971);

		int numCubes = (int) (entity.height / 0.5F);

		for (int i = 0; i < numCubes; i++) {
			GlStateManager.pushMatrix();
			float dx = (float) (x + random.nextGaussian() * 0.2F * entity.width);
			float dy = (float) (y + random.nextGaussian() * 0.2F * entity.height) + entity.height / 2F;
			float dz = (float) (z + random.nextGaussian() * 0.2F * entity.width);
			GlStateManager.translate(dx, dy, dz);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.rotate(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.WEB.getDefaultState(), 1);
			GlStateManager.popMatrix();
		}
		GlStateManager.disableBlend();
	}
}
