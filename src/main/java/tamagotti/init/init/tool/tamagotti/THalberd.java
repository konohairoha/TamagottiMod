package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
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
import tamagotti.init.entity.projectile.EntityTHalberd;
import tamagotti.init.items.tool.tamagotti.iitem.IAmulet;
import tamagotti.util.TUtil;

public class THalberd extends TSword implements IAmulet {

	public THalberd(String name, ToolMaterial material, Double atack) {
		super(name, material, atack, 0);
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
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			float f = TUtil.getArrowVelocity(i, 1F);
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
	  		stack.damageItem(1, player);

			EntityTHalberd arrow = new EntityTHalberd(world, player, stack, k);
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3F, 1.0F);
			arrow.setDamage(arrow.getDamage() + (2 + j + (double)(player.experienceLevel / 5)) * f);

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
				arrow.setFire(100);
			}
			if (f == 1.0F) {
				arrow.setIsCritical(true);
			}

			if (!world.isRemote) {
				world.spawnEntity(arrow);
				if (!player.capabilities.isCreativeMode) {
					stack.shrink(1);
				}
			}
			world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		}
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

	@Override
	public boolean isShrink() {
		return false;
	}

	@Override
	public int needExp() {
		return 15;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.GREEN + "固有魔法：希望を繋ぐ力"));
  		tooltip.add(I18n.format(TextFormatting.GREEN + "経験値分攻撃力アップ"));
  		tooltip.add(I18n.format(TextFormatting.GREEN + "経験値15以上で死亡時にアイテム保持"));
		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリックで投擲"));
	}
}
