package tamagotti.init.entity.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.entity.monster.EntityLCEvokerDep;
import tamagotti.init.render.RenderLCEvokerDep;

public class LayerLCEvokerDep implements LayerRenderer<EntityLCEvokerDep> {

    private static final ResourceLocation TEX_0 = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderLCEvokerDep renderer;
    private final ModelIllager model = new ModelIllager(0.0F, 0.0F, 64, 64);

	public LayerLCEvokerDep(RenderLCEvokerDep renderer) {
		this.renderer = renderer;
	}

	@Override
	public void doRenderLayer(EntityLCEvokerDep entity, float swing, float amount, float tick, float age, float yaw, float pitch, float scale) {
		boolean flag = entity.isInvisible();
		GlStateManager.depthMask(!flag);
		this.renderer.bindTexture(TEX_0);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = entity.ticksExisted + tick;
		GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		this.model.setModelAttributes(this.renderer.getMainModel());
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.model.render(entity, swing, amount, age, yaw, pitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(flag);
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}