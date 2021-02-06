package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlaster extends RenderRitter {

    public RenderBlaster(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
}