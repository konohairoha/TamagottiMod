package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTHalberd;
import tamagotti.init.model.ModelTHalberd;

@SideOnly(Side.CLIENT)
public class RenderTHalberd extends ReforgedRender<EntityTHalberd> {

    public static final ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/entity/thalberd.png");

	public RenderTHalberd(RenderManager renderManager) {
		super(renderManager, new ModelTHalberd(), 90);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTHalberd entity) {
		return TEX;
	}
}