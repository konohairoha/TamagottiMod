package tamagotti.init.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.ore.smoky.SmokySickle;
import tamagotti.init.items.tool.tamagotti.iitem.IZoom;

public class ZoomEvent {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderfov(FOVUpdateEvent event) {

		EntityPlayer player = FMLClientHandler.instance().getClient().player;
		Item item = player.getHeldItemMainhand().getItem();
		if (item == null || !(item instanceof IZoom) || !player.isSneaking()) { return; }

		IZoom zoom = (IZoom) item;
		if (zoom.zoomItem() <= 0) { return; }

		event.setNewfov(event.getFov() / zoom.zoomItem());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onBulletRenderEvent(RenderGameOverlayEvent.Text event) {

		Minecraft mc = FMLClientHandler.instance().getClient();
		ScaledResolution resolution = new ScaledResolution(mc);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		EntityPlayer player = mc.player;
		ItemStack stack = player.getHeldItemMainhand();
		mc.entityRenderer.setupOverlayRendering();
		FontRenderer render = mc.fontRenderer;
		mc.fontRenderer.setUnicodeFlag(mc.isUnicode());
		Minecraft view = Minecraft.getMinecraft();

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT && view.gameSettings.thirdPersonView == 0
				&& stack != null && (stack.getItem() instanceof SmokySickle)) {

			int stackCount = 0;
			NonNullList<ItemStack> playerInv = player.inventory.mainInventory;
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);

			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			for (int i = 0; i < playerInv.size(); i++) {
				ItemStack item = playerInv.get(i);
				if (item != null && item.getItem() instanceof ItemDye &&
						EnumDyeColor.byDyeDamage(item.getMetadata()) == EnumDyeColor.WHITE) {
					stackCount += item.getCount();
				}
			}

			String d1 = String.format("%1$3d", new Object[] { Integer.valueOf(stackCount) });
			render.drawString((new StringBuilder()).append("x ").append(d1).toString(), width - 28, height - 49, 0xffffff);
			RenderItem renderitem = mc.getRenderItem();
			renderitem.renderItemIntoGUI(new ItemStack(Items.DYE, 1, 15), width - 48, height - 55);
			GL11.glPopMatrix();
		}
	}
}
