package tamagotti.init.blocks.chest.sw;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.TileBaseChest;

@SideOnly(Side.CLIENT)
public class GuiSWChest extends GuiContainer {

	private final static ResourceLocation TEX = new ResourceLocation("tamagottimod:textures/gui/swchest.png");
	public final TileBaseChest tile;

	public GuiSWChest(EntityPlayer inventory, TileBaseChest tile) {
		super(new ContainerSWChest(inventory, tile));
		this.tile = tile;
		this.xSize = 255;
		this.ySize = 250;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
