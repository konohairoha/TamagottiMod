package tamagotti.init.blocks.chest.dchest;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDChest extends GuiContainer{

	private final static ResourceLocation GUI_BACKGROUND = new ResourceLocation("tamagottimod:textures/gui/tchest.png");
	public final TileDChest tileEntity;

	public GuiDChest(EntityPlayer inventory, TileDChest tileEntity){
		super(new ContainerDChest(inventory, tileEntity));
		this.tileEntity = tileEntity;
		this.xSize = 255;
		this.ySize = 250;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_BACKGROUND);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
