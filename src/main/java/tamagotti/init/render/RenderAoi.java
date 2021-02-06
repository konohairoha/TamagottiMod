package tamagotti.init.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.monster.EntityAoi;
import tamagotti.init.model.ModelAoi;

@SideOnly(Side.CLIENT)
public class RenderAoi extends RenderLiving<EntityAoi>{

	public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/aoi.png");

	@Override
	protected ResourceLocation getEntityTexture(EntityAoi entity) {
		return TEXTURES;
	}

	public RenderAoi(RenderManager render) {
        super(render, new ModelAoi(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().Head));
    }

	@Override
	public ModelAoi getMainModel() {
        return (ModelAoi)super.getMainModel();
    }

    @Override
	protected void preRenderCallback(EntityAoi entity, float partialTickTime) {
        float f = 0.9375F;
        if (entity.getGrowingAge() < 0) {
            f = (float)(f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }
        GlStateManager.scale(f, f, f);
    }
}