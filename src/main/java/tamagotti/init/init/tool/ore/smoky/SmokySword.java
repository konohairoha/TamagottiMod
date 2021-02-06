package tamagotti.init.items.tool.ore.smoky;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TSword;

public class SmokySword extends TSword {

	private final float range;

	public SmokySword(String name, ToolMaterial material, float size) {
		super(name, material, 0.0, 0);
        this.range = size;
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
  	    return repair.getItem() == ItemInit.smokyquartz ? true : super.getIsRepairable(toRepair, repair);
  	}

  	//右クリックチャージをやめたときに矢を消費せずに矢を射る
  	@Override
  	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
  		if (living instanceof EntityPlayer) {
  			EntityPlayer player = (EntityPlayer) living;
	  		stack.damageItem(1, player);
	  		world.createExplosion(player, player.posX, player.posY + 1, player.posZ, this.range +
	  				EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack), false);
  			player.addStat(StatList.getObjectUseStats(this));
  		}
  	}

  	//右クリックをした際の処理
  	@Override
  	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
  		player.setActiveHand(hand);
  		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
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
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		float exp = this.range + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリック長押しで爆発"));
		tooltip.add(I18n.format(TextFormatting.YELLOW + "爆発範囲：" + exp));
	}
}
