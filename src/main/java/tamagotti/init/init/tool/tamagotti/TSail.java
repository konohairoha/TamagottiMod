package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.TamagottiMod;
import tamagotti.init.items.tool.tamagotti.iitem.IChange;
import tamagotti.util.ItemHelper;
import tamagotti.util.TUtil;

public class TSail extends TItem implements IChange {

	private final int data;
	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	protected static final ResourceLocation ACTIVE_NAME = new ResourceLocation(TamagottiMod.MODID, "active");
	protected static final IItemPropertyGetter ACTIVE_GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(TAG_ACTIVE) ? 1F : 0F;

	public TSail(String name, int meta) {
		super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
        this.data = meta;
    }

	@Override
	public boolean changeMode(EntityPlayer player, ItemStack stack) {
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		boolean active = !tags.getBoolean(TAG_ACTIVE);
		tags.setBoolean(TAG_ACTIVE, active);
		player.getEntityWorld().playSound(null, new BlockPos(player),
				active ? SoundEvents.BLOCK_CLOTH_BREAK : SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 2F, 1F);
		return true;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return TUtil.shouldCauseReequipAnimation(oldStack, newStack, slotChanged, TAG_ACTIVE, TAG_MODE);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if(this.data == 0 && !player.isSneaking()) { return ActionResult.newResult(EnumActionResult.PASS, stack); }

		this.changeMode(player, player.getHeldItem(hand));
		player.getCooldownTracker().setCooldown(this, 4);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	//手に持った時にポーション効果を付ける
    @Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int off, boolean main) {

		if (!ItemHelper.getNBT(stack).getBoolean(TAG_ACTIVE) || !(entity instanceof EntityPlayer)){ return; }

		// メインハンドとオフハンドで持つと機能にしないようにする
		if (main || off < 1) {

			float addSpeed = 0;
	    	EntityPlayer player = (EntityPlayer) entity;
			if (stack.hasDisplayName()) { addSpeed = 0.025F; }
			player.fallDistance = 0;

			float moY = 0.04F;
			float moXZ = 1.0775F;

			if (this.data == 1) {
				moY = 0.04825F;
				moXZ = 1.1F;
			}

			if (!player.capabilities.isFlying && TamagottiMod.proxy.isJumpPressed()) {
				player.motionY += moY;
			}

			if (player.onGround) { return; }

			if (player.motionY <= 0) {
				if (player.isSneaking()) {
					player.motionY *= 0.825 + (this.data * 0.05);
				} else {
					player.motionY *= 0.6 - (this.data * 0.05);
				}
			}

			if (player.capabilities.isFlying) { return; }

			if (player.moveForward < 0) {
				player.motionX *= 0.9;
				player.motionZ *= 0.9;
			} else if (player.moveForward > 0 && player.motionX * player.motionX + player.motionY * player.motionY +
					player.motionZ * player.motionZ < (3 + (this.data * 0.3)) + (addSpeed * (10 * (this.data + 1)))) {
				player.motionX *= moXZ + addSpeed;
				player.motionZ *= moXZ + addSpeed;
			}
		}
	}

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		if (stack.hasDisplayName()) {
			tooltip.add(I18n.format(TextFormatting.GREEN + "特殊効果：滑空速度アップ"));
		}
  		if(this.data == 0) {
  			tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックで開閉、滑空出来る"));
  		} else {
  			tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックで開閉、滑空出来る"));
  		}
  	}
}
