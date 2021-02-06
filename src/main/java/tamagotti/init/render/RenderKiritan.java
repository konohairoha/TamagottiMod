package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityKiritan;

@SideOnly(Side.CLIENT)
public class RenderKiritan extends RenderArrow<EntityKiritan>{
    public static final ResourceLocation TArrow = new ResourceLocation("tamagottimod:textures/entity/kiritan.png");

    public  RenderKiritan(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityKiritan entity) {
		return TArrow;
	}
}