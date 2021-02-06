package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRubyShot <T extends Entity> extends RenderRitter<T> {

	private static final ResourceLocation tex = new ResourceLocation("tamagottimod:textures/entity/rubyshot.png");

    public RenderRubyShot(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}
}