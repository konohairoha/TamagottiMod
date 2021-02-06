package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTKArrow;

@SideOnly(Side.CLIENT)
public class RenderTKArrow extends RenderArrow<EntityTKArrow>{
    public static final ResourceLocation TArrow = new ResourceLocation("tamagottimod:textures/entity/bambooarrow.png");

    public  RenderTKArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityTKArrow entity) {
		return TArrow;
	}
}