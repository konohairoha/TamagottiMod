package tamagotti.init.items.tool.bamboo;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.entity.projectile.EntityTKArrow;
import tamagotti.util.TUtil;

public class BA_Arrow extends ItemArrow {


	public BA_Arrow(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		ItemInit.itemList.add(this);
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) living;
		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 1F);

		if (!world.isRemote) {
			EntityTKArrow arrow = new EntityTKArrow(world, player, new ItemStack(this), false, false);
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0F, f * 1.5F, 1F);
			arrow.setDamage(arrow.getDamage() - 0.25D);
			arrow.setIsCritical(f == 1F);
			world.spawnEntity(arrow);
			if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
		}

		world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 1F, 1F);
	}

	//最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：3"));
		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：1.5"));
	}

	// バニラの矢での射撃時の設定
	@Override
	public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter) {
		return new EntityTKArrow(world, shooter, new ItemStack(this), false, false);
	}
}
