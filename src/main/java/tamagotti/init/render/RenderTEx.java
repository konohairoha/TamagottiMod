package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTEx;

@SideOnly(Side.CLIENT)
public class RenderTEx extends RenderArrow<EntityTEx>{
    public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/tamagottiarrow.png");

    public RenderTEx(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityTEx entity) {
		return TEXTURES;
	}
}