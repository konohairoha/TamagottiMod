package tamagotti.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.tile.TileBaseFurnace;
import tamagotti.init.tile.container.ContainerPot;

public class GuiPot extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("tamagottimod", "textures/gui/gui_pot.png");
	private final TileBaseFurnace tile;
	public EntityPlayer player;
	public ContainerPot pot;

	public GuiPot(InventoryPlayer invPlayer, TileBaseFurnace tile) {
		super(new ContainerPot(invPlayer, tile));
		this.pot = new ContainerPot(invPlayer, tile);
		this.player = invPlayer.player;
		this.xSize = 209;
		this.ySize = 165;
		this.tile = tile;
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

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		int progress;

		if (tile.isBurning()) {
			progress = tile.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(x + 66, y + 38 + 10 - progress, 210, 10 - progress, 21, progress + 2);
		}

		progress = tile.getCookProgressScaled(24);
		this.drawTexturedModalRect(x + 88, y + 35, 210, 14, progress, 17);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int mf = tile.burnTime;
		int max = tile.maxBurnTime;
		//基準点

		//描画位置を計算
		int tip_x = (this.width - xSize) / 2;
		int tip_y = (this.height - ySize) / 2;

		//ゲージの位置を計算
		tip_x += 66;
		tip_y += 37;

		//drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
		//72 * 26
		if (tip_x <= mouseX && mouseX <= tip_x + 12
				&& tip_y <= mouseY && mouseY <= tip_y + 12) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (width - xSize) / 2);
			int yAxis = (mouseY - (height - ySize) / 2);

			this.drawHoveringText(mf + "BT / " + max + "BT", xAxis, yAxis);
		}
	}
}
