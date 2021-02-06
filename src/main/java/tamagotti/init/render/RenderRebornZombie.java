package tamagotti.init.render;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRebornZombie extends RenderLiving<EntityZombie> {

	public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/zombiemaster.png");

	public RenderRebornZombie(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelZombie(), 0.3F);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelZombie(0.5F, true);
				this.modelArmor = new ModelZombie(1.0F, true);
			}
		});
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombie entity) {
		return TEXTURES;
	}

	@Override
	public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }
}
