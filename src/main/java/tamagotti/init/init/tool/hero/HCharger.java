package tamagotti.init.items.tool.hero;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityRitter;
import tamagotti.init.event.TSoundEvent;

public class HCharger extends BaseBow {

	public HCharger(String name) {
		super(name);
        setMaxDamage(4096);
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		EntityPlayer player = (EntityPlayer) living;
	    EntityRitter arrow = new EntityRitter(world, player);
		arrow.setDamage(arrow.getDamage() + 15 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
	    //弾の初期弾速と集弾性の下限値
		arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);
		arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 4.5F + k, 0);
		if (!world.isRemote) {
	    	world.spawnEntity(arrow);
        	stack.damageItem(1, player);
		}
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
			arrow.setFire(60);
        }
	    world.playSound(player, player.posX, player.posY, player.posZ, TSoundEvent.TYA, SoundCategory.AMBIENT, 0.15F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 15 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		float shot = (float) (4.5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
		tooltip.add(I18n.format(TextFormatting.GOLD + "チャージなしで連射可能"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}