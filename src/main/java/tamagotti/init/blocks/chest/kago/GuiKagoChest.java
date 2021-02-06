package tamagotti.init.blocks.chest.kago;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiKagoChest extends GuiContainer{

	private final static ResourceLocation GUI_BACKGROUND = new ResourceLocation("textures/gui/container/shulker_box.png");
	public final  TileKagoChest tileEntity;

	public GuiKagoChest(EntityPlayer inventory, TileKagoChest tileEntity){
		super(new ContainerKagoChest(inventory, tileEntity));
		this.tileEntity = tileEntity;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		//xx_xx.langファイルから文字を取得する方法
		String text = new TextComponentTranslation("gui.kago.name", new Object[0]).getFormattedText();
		/**
		 * タイトル文字を描画する。
		 * 順番に、「表示したい文字列、表示位置のX座標、Y座標、文字の色」
		 * (注意：文字の色情報はGBRAを表す32bitの2進数を10進数に変換したもの)
		 */
		this.fontRenderer.drawString(I18n.format(text, new Object[0]), 8, 6, 0x404040);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 95 + 2, 0x404040);
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
