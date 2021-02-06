package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderZunda <T extends Entity> extends RenderRitter<T>{

	private static final ResourceLocation tex = new ResourceLocation("tamagottimod:textures/entity/zundaarrow.png");

    public RenderZunda(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
	public void doRender(T var1, double var2, double var4, double var6, float var8, float var9){
        this.renderBullet(var1, var2, var4, var6, var8, var9);
    }

    @Override
	protected ResourceLocation getEntityTexture(T entity){
		return tex;
	}
}