package tamagotti.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.tile.TileTGeneratorT1;
import tamagotti.init.tile.container.ContianerTGeneratorT1;

public class GuiTGeneratorT1 extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("tamagottimod", "textures/gui/gui_tharvesterpng.png");
	private final TileTGeneratorT1 tile;

	public GuiTGeneratorT1(InventoryPlayer invPlayer, TileTGeneratorT1 tile) {
		super(new ContianerTGeneratorT1(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 200;
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

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		int progress;

		//こっちではゲージ量を計算する　かまどのMFの内容ができてから？
		if (!this.tile.isEmptyEnergy()) {
			progress = this.tile.getMfProgressScaled(101);
			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 22, y + 110 - progress, 186, 100 - progress, 24, progress);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int en = this.tile.energy.getStored();
		int max = this.tile.energy.getCapacity();
		//基準点

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2;
		int tip_y = (this.height - this.ySize) / 2;

		//ゲージの位置を計算
		tip_x += 21;
		tip_y += 9;

		//drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
		//72 * 26
		if (tip_x <= mouseX && mouseX <= tip_x + 25
				&& tip_y <= mouseY && mouseY <= tip_y + 102) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(en + " / " + max + "FE", xAxis, yAxis);
		}
	}
}
