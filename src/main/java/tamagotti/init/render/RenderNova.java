package tamagotti.init.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNova extends RenderRitter{

    public RenderNova(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
}