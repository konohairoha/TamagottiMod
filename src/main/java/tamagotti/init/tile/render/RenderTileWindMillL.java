package tamagotti.init.tile.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.tile.TileWindMillL;
import tamagotti.init.tile.model.ModelWindMillL;

public class RenderTileWindMillL extends TileEntitySpecialRenderer<TileWindMillL> {

	float angle = 0;
	private final static  ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/entity/windmill_l_brass.png");

	@Override
	public void render(TileWindMillL te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ModelWindMillL model = this.getModel(te);
		if (model == null) { return; }
		float speed = 0 * partialTicks;
		float rot = 0 * partialTicks;

		this.bindTexture(TEX);

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);

		this.render(te, model, rot, speed, partialTicks);

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private static final ModelWindMillL MODEL = new ModelWindMillL();

	protected ModelWindMillL getModel(TileWindMillL te) {
		return MODEL;
	}

	public void render(TileWindMillL te, ModelWindMillL model, float rot, float speed, float tick) {

		float x = 0F;
		float y = 0F;
		float z = 90F;
		EnumFacing face = te.getFace();

		if (face == EnumFacing.NORTH) {
			y = -90F;
		} else if (face == EnumFacing.SOUTH) {
			y = 90F;
		} else if (face == EnumFacing.EAST) {
			y = 0F;
		} else if (face == EnumFacing.WEST) {
			y = 180F;
		} else if (face == EnumFacing.UP) {
			x = -90F;
		} else if (face == EnumFacing.DOWN) {
			x = 90F;
		}

		angle = te.acceleration(tick);
		if (!te.getActive()) {
			angle = 0;
		}

		GlStateManager.rotate(x, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(y, 0F, 1.0F, 0.0F);
		GlStateManager.rotate(z, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);

		model.render(rot, speed, tick);
	}
}
