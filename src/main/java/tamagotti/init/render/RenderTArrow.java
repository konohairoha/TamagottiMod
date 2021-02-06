package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTArrow;

@SideOnly(Side.CLIENT)
public class RenderTArrow extends RenderArrow<EntityTArrow> {
    public static final ResourceLocation TArrow = new ResourceLocation("tamagottimod:textures/entity/tarrow.png");

    public  RenderTArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityTArrow entity) {
		return TArrow;
	}
}