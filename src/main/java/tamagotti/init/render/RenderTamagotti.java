package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTamagotti;

@SideOnly(Side.CLIENT)
public class RenderTamagotti extends RenderArrow<EntityTamagotti>{
    public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/tamagottiarrow.png");

    public RenderTamagotti(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityTamagotti entity) {
		return TEXTURES;
	}
}