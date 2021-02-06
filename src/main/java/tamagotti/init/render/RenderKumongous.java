package tamagotti.init.render;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.monster.EntityKumongous;

@SideOnly(Side.CLIENT)
public class RenderKumongous extends RenderLiving<EntityKumongous> {

	public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/kumongous.png");

	public RenderKumongous(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSpider(), 0.3F);
	}

	@Override
	protected void preRenderCallback(EntityKumongous entitylivingbaseIn, float partialTickTime) {
		float size = 1.33F;
		GlStateManager.scale(size, size, size);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityKumongous entity) {
		return TEXTURES;
	}
}
