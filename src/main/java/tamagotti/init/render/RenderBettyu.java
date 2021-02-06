package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityBettyu;

@SideOnly(Side.CLIENT)
public class RenderBettyu extends RenderArrow<EntityBettyu>{
    public static final ResourceLocation TEXTURES = new ResourceLocation("tamagottimod:textures/entity/tamagottiarrow.png");

    public RenderBettyu(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

	@Override
    protected ResourceLocation getEntityTexture(EntityBettyu entity) {
		return TEXTURES;
	}
}