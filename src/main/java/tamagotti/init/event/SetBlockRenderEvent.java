package tamagotti.init.event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import tamagotti.TamagottiMod;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.extend.ExShovel;
import tamagotti.init.items.tool.tamagotti.iitem.IHeld;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = TamagottiMod.MODID)
public class SetBlockRenderEvent {

	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final List<AxisAlignedBB> renderList = new ArrayList<>();
	private static double x;
	private static double y;
	private static double z;
	private static IBlockState state;

	// ブロックレンダー
	@SubscribeEvent
	public static void preDrawHud(RenderGameOverlayEvent.Pre event) {

		if (event.getType() == ElementType.CROSSHAIRS || state == null) { return; }

		if (FluidRegistry.lookupFluidForBlock(state.getBlock()) != null) {
			TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite( FluidRegistry.lookupFluidForBlock(state.getBlock()).getFlowing().toString());
			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			BufferBuilder wr = Tessellator.getInstance().getBuffer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(0, 0, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
			wr.pos(0, 16, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
			wr.pos(16, 16, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
			wr.pos(16, 0, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
			Tessellator.getInstance().draw();
		} else {
			RenderHelper.enableStandardItemLighting();
			RenderHelper.disableStandardItemLighting();
		}
	}

	@SubscribeEvent
	public static void onOverlay(DrawBlockHighlightEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = player.getEntityWorld();
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		// メインハンドに何もないならオフハンドで判断
		if (stack.isEmpty()){ stack = player.getHeldItem(EnumHand.OFF_HAND); }

		Item item = stack.getItem();

		if (stack.isEmpty() || (item != ItemInit.tshovel_ex && item != ItemInit.rubywand && !(item instanceof IHeld))) {
			state = null;
			return;
		}

		x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

		RayTraceResult mop = ((ExShovel) ItemInit.tshovel_ex).getHitBlock(player);

		// 向いてる方向にすでにブロックがある場合
		if (mop != null && mop.typeOfHit == Type.BLOCK) {
			BlockPos pos = mop.getBlockPos().offset(mop.sideHit);
			if (item instanceof IHeld) {
				addHeldToRenderList(world, pos, item);
			} else {
				addBlockToRenderList(world, pos);
			}

		// 向てる方向にブロックがない場合
		} else if (!(item instanceof IHeld)){
			Vec3d vec3d = player.getLookVec();
			vec3d = vec3d.normalize().scale(7);
			BlockPos pos = new BlockPos(player.posX + (int) vec3d.x, player.posY + (int) vec3d.y + 1, player.posZ + (int) vec3d.z);
			addBlockToRenderList(world, pos);
		}

		drawAll();
		renderList.clear();
	}

	private static void drawAll() {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.35f);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION);

		for (AxisAlignedBB b : renderList) {

			//Top
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();

			//Bottom
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			//Front
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();

			//Back
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();

			//Left
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			//Right
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
		}

		tess.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	private static void addBlockToRenderList(World world, BlockPos pos) {
		AxisAlignedBB box = world.getBlockState(pos).getSelectedBoundingBox(world, pos).grow(-0.01);
		box = box.offset(-x, -y, -z);
		renderList.add(box);
	}

	private static void addHeldToRenderList(World world, BlockPos pos, Item item) {
		IHeld held = (IHeld) item;
		AxisAlignedBB box = held.getRange(pos);
		box = box.offset(-x, -y + 0.2, -z + 1);
		renderList.add(box);
	}
}
