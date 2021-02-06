package tamagotti.init.blocks.chest.wadansu;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.TileBaseChest;
import vazkii.quark.api.IChestButtonCallback;

@SideOnly(Side.CLIENT)
@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiWadansuChest extends GuiContainer implements IChestButtonCallback {

	private final static ResourceLocation TEX = new ResourceLocation( "textures/gui/container/generic_54.png");
	public final TileBaseChest tile;

	public GuiWadansuChest(EntityPlayer inventory, TileBaseChest tile) {
		super(new ContainerWadansuChest(inventory, tile));
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
