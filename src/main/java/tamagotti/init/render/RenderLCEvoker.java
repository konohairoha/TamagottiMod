package tamagotti.init.render;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLCEvoker extends RenderLiving<EntityMob> {

	private static final ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/entity/tevoker.png");

	public RenderLCEvoker(RenderManager render) {
		super(render, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
		this.addLayer(new LayerHeldItem(this) {
			@Override
			public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount,
					float tick, float age, float netHead, float headPitch, float scale) {
				if (((EntitySpellcasterIllager) entity).isSpellcasting()) {
					super.doRenderLayer(entity, limbSwing, amount, tick, age, netHead, headPitch, scale);
				}
			}

			@Override
			protected void translateToHand(EnumHandSide side) {
				((ModelIllager) this.livingEntityRenderer.getMainModel()).getArm(side).postRender(0.0625F);
			}
		});
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMob entity) {
		return TEX;
	}

	@Override
	protected void preRenderCallback(EntityMob entity, float tickTime) {
		float f = 0.9375F;
		GlStateManager.scale(f, f, f);
	}
}