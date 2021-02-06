package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.entity.monster.EntityBurningSkullHead;
import tamagotti.init.model.ModelBurningSkullHead;

public class RenderBurningSkullHead extends RenderLiving<EntityBurningSkullHead> {

    private static final ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/entity/burningskullhead.png");

	public RenderBurningSkullHead(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBurningSkullHead(), 0.3F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBurningSkullHead entity) {
		return TEX;
	}
}
