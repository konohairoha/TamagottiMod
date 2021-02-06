package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.monster.EntityTChicken;
import tamagotti.init.model.ModelTChicken;

@SideOnly(Side.CLIENT)
public class RenderTChicken extends RenderLiving<EntityTChicken>{

	public static final ResourceLocation TEXTURES = new ResourceLocation("textures/entity/chicken.png");

	public RenderTChicken(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelTChicken(), 0.3F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTChicken entity) {
		return TEXTURES;
	}

	@Override
	protected float handleRotationFloat(EntityTChicken livingBase, float partialTicks) {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
