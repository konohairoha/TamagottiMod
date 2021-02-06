package tamagotti.init.tile.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.container.BookContainer;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation("textures/gui/container/crafting_table.png");

	public GuiBook(InventoryPlayer inventoryPlayer){
		super(new BookContainer(inventoryPlayer));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		this.fontRenderer.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_){
		GlStateManager.color(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(TEX);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}