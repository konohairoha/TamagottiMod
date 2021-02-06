package tamagotti.init.event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tamagotti.init.items.tool.tamagotti.TSail;
import tamagotti.init.render.RenderPlayerSail;

public class RenderSailEvent {

	public RenderPlayer playerRenderer;

	@SubscribeEvent
	public void onPlayerRenderPre(RenderPlayerEvent.Pre event) {

		if(event.getEntityPlayer() instanceof AbstractClientPlayer && !(event.getRenderer() instanceof RenderPlayerSail)) {

			EntityPlayer player = event.getEntityPlayer();
			ItemStack stack = player.getHeldItemMainhand();

			// メインハンドで持っていないならオフハンドに切り替え
			if (!(stack.getItem() instanceof TSail) || stack.isEmpty()) {
				stack = player.getHeldItemOffhand();
			}

			// パラセールを持っていないなら終了
			if (stack.isEmpty() || !(stack.getItem() instanceof TSail)) { return; }

			// 開いてる状態ではないなら終了
			NBTTagCompound tags = stack.getTagCompound();
			if (tags == null || !tags.getBoolean("Active")) { return; }

			// レンダー開始
			GlStateManager.pushMatrix();
			event.setCanceled(true);

			float renderTick = event.getPartialRenderTick();
			double moX = player.motionX * 20;
			double moZ = player.motionZ * 20;

			// 速度に合わしてプレイヤーを傾ける
			GlStateManager.rotate((float) -moX, 0, 0, 1);
			GlStateManager.rotate((float) moZ, 1, 0, 0);

			// 手のレンダー
			this.playerRenderer = new RenderPlayerSail(event.getRenderer().getRenderManager(), true);
			float interpolate = this.interpolate(player.prevRotationYaw, player.rotationYaw, renderTick);
			this.playerRenderer.doRender((AbstractClientPlayer) player, event.getX(), event.getY(), event.getZ(), interpolate, renderTick);
			RenderHelper.enableStandardItemLighting();

			// レンダー終了
			GlStateManager.popMatrix();
		}
	}

	public float interpolate(double prev, double now, double partialTicks) {
		return (float) (prev + (now - prev) * partialTicks);
	}
}
