package tamagotti.init.items.tool.yukari;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityRitter;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.iitem.IZoom;
import tamagotti.util.TUtil;

public class YRitter extends BaseBow implements IZoom {

	private final int cycle;
	private final int dame;

	public YRitter(String name, int cool, int damage, int value) {
		super(name);
        setMaxDamage(value);
        this.cycle = cool;
        this.dame = damage;
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			float f = TUtil.getArrowVelocity((this.getMaxItemUseDuration(stack) - timeLeft), 1F);
            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            int n = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
	        int o = (int) (this.cycle * f);
            EntityRitter arrow = new EntityRitter(world, player);
            arrow.setDamage(arrow.getDamage() + (this.dame + (j * 2))  * f * f);
            //弾の初期弾速と集弾性の下限値
			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);
	        arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, (float) (3.5F + ((this.cycle - 30) * 0.25) + k), 0);

			if (stack.getItemDamage() < this.getMaxDamage(stack)) {
				if (!world.isRemote) {
	            	world.spawnEntity(arrow);
	            }
            	player.getCooldownTracker().setCooldown(this, o / (n + 1));
            	stack.damageItem(1, player);
    	        world.playSound(player, new BlockPos(player), TSoundEvent.TYA, SoundCategory.AMBIENT, 0.3F, 1.0F);
	        } else {
	        	// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.tamagotti, 0, 1, this.getMaxDamage(stack));
            }
			player.addStat(StatList.getObjectUseStats(this));
		}
	}

	@Override
	public float zoomItem() {
		return this.cycle == 30 ? 2.75F : 5.0F;
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = this.dame + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 2;
  		int shot = 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
  			tooltip.add(I18n.format(TextFormatting.RED + "たまごっちを消費して耐久回復"));
  		}
  		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク状態でズーム"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
