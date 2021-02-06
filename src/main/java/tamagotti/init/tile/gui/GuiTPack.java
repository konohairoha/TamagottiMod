package tamagotti.init.tile.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tamagotti.init.tile.container.ContainerTPack;
import tamagotti.init.tile.inventory.InventoryTPack;

public class GuiTPack extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("tamagottimod", "textures/gui/gui_tpack.png");
	private final InventoryTPack inventory;
	public EntityPlayer player;

	public GuiTPack(InventoryPlayer invPlayer, InventoryTPack invGem) {
		super(new ContainerTPack(invPlayer, invGem));
		this.player = invPlayer.player;
		this.inventory = invGem;
		this.xSize = 172;
		this.ySize = 165;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(0, (width - xSize) / 2 + 6, (height - ySize) / 2 + 50, 30, 20, "選択"));
		this.buttonList.add(new GuiButton(1, (width - xSize) / 2 + 50, (height - ySize) / 2 + 50, 30, 20, "選択"));
		this.buttonList.add(new GuiButton(2, (width - xSize) / 2 + 94, (height - ySize) / 2 + 50, 30, 20, "選択"));
		this.buttonList.add(new GuiButton(3, (width - xSize) / 2 + 137, (height - ySize) / 2 + 50, 30, 20, "選択"));
	}


	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		this.inventory.teleportPlayer(player, button.id);	// プレイヤーとスロットidを渡す
        this.mc.displayGuiScreen((GuiScreen)null);			// guiを閉じる
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
