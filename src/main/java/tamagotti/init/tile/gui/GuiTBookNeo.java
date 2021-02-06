package tamagotti.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.container.ContainerTBookNeo;
import tamagotti.init.tile.inventory.InventoryTBookNeo;

@SideOnly(Side.CLIENT)
public class GuiTBookNeo extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("tamagottimod", "textures/gui/gui_tbookneo.png");
	public final InventoryTBookNeo inventory;
	public EntityPlayer player;

	public GuiTBookNeo(InventoryPlayer invPlayer, InventoryTBookNeo invGem, EnumHand hand) {
		super(new ContainerTBookNeo(invPlayer, invGem, hand));
		this.player = invPlayer.player;
		this.inventory = invGem;
		this.xSize = 181;
		this.ySize = 255;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
