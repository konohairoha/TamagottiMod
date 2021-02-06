package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tamagotti.TamagottiMod;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.iitem.IChange;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class TPendant extends TItem implements IChange {

	public boolean yActive; //テキスト用
	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public TPendant(String name) {
		super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
    }

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	@Override
	public boolean changeMode(EntityPlayer player, ItemStack stack) {
		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE)) {
			this.yActive = false;
			player.getEntityWorld().playSound(null, new BlockPos(player), TSoundEvent.IN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
		} else {
			this.yActive = true;
			player.getEntityWorld().playSound(null, new BlockPos(player), TSoundEvent.OUT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
		}
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (!world.isRemote) {
			this.changeMode(player, player.getHeldItem(hand));
			if(this.yActive) {
				//チャットウインドウへテキストを表示する
				player.sendMessage(new TextComponentString("Inhole Mode: OFF"));
			} else {
				player.sendMessage(new TextComponentString("Inhole Mode: ON"));
			}
			player.getCooldownTracker().setCooldown(this, 5);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE) || !(entity instanceof EntityPlayer)){ return; }
		EntityPlayer player = (EntityPlayer) entity;
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(24);

		// アイテム吸引
		List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, aabb);
		if (!itemList.isEmpty()) {
			for (EntityItem item : itemList) {
				if (ItemHelper.hasSpace(player.inventory.mainInventory, item.getItem())) {
					WorldHelper.gravitateEntityTowards(item, player.posX, player.posY, player.posZ);
				}
			}
		}

		// 経験値吸引
		List<EntityXPOrb> expList = world.getEntitiesWithinAABB(EntityXPOrb.class, aabb);
		if (!expList.isEmpty()) {
			for (EntityXPOrb xpOrb : expList) {
				WorldHelper.gravitateEntityTowards(xpOrb, player.posX, player.posY, player.posZ);
			}
		}
	}
}
