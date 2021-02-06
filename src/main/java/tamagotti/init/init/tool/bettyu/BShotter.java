package tamagotti.init.items.tool.bettyu;

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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityShotter;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.TItem;
import tamagotti.util.WorldHelper;

public class BShotter extends TItem {

	public BShotter(String name) {
        super(name);
		this.setMaxStackSize(1);
        setMaxDamage(230);
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);

		if (player.isSneaking() && stack.getItemDamage() == 0) {
			WorldHelper.SuperSensor(world, player.getEntityBoundingBox().grow(50D, 25D, 50D));
			player.clearActivePotions();
			stack.damageItem(230, player);
			player.getCooldownTracker().setCooldown(this, 60);
			world.playSound(player, new BlockPos(player), TSoundEvent.SENSOR, SoundCategory.AMBIENT, 1.0F, 1.0F);

		} else {

			EntityShotter arrow = new EntityShotter(world, player);
			arrow.setDamage(arrow.getDamage() + 6 + j);
			//弾の初期弾速と集弾性の下限値
			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 2.5F, 2.5F, 2.5F);
			arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 4.5F + k, 4);
			player.getCooldownTracker().setCooldown(this, 4 - l * 2);

			if (!world.isRemote) {
				world.spawnEntity(arrow);
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
			world.playSound(player, new BlockPos(player), TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);
		}
		player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = (5 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack)));
  		float shot = (float) 4.5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		if (stack.getItemDamage() == 0) {
  			tooltip.add(I18n.format(TextFormatting.GREEN + "スニーク右クリックで範囲内のモブを発光"));
  		}
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
