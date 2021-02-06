package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityAkari;
import tamagotti.init.items.tool.tamagotti.iitem.IRelode;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.TUtil;

public class AkariStar extends BaseBow implements IRelode {

	public AkariStar(String name) {
        super(name);
		this.setMaxDamage(128);
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			float f = TUtil.getArrowVelocity(i, 1F);
			if (stack.getItemDamage() < this.getMaxDamage(stack)) {
				EntityAkari arrow = new EntityAkari(world, player);
				arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.0F, 0.0F);
				arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 2.0F, 4);
				if (!world.isRemote) {
					world.spawnEntity(arrow);
					arrow.setDamage(arrow.getDamage() + 2 * f);
					stack.damageItem(1, player);
				}
				world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1F,
						1F);
			}
			player.addStat(StatList.getObjectUseStats(this));
		}
	}

	//手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
    	EntityLivingBase living = (EntityLivingBase) entity;
    	if(main) {
			living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20, 0));
		} else if (off < 1) {
			living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20, 1));
		}
	}

	//敵に攻撃したら自分または相手にポーション効果
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
  		stack.damageItem(1, attacker);
  		attacker.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 200, 3));
  		target.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 3));
  		target.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 3));
  		return true;
  	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 2;
  		int shot = 2;
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーでご飯を4つ消費して耐久を全回復"));
  		} else {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーでご飯を1つ消費して耐久を16回復"));
  		}
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }

	@Override
	public void doRelode(EntityPlayer player, ItemStack stack) {

		if (stack.getItemDamage() != 0) {
			if (stack.getItemDamage() == this.getMaxDamage(stack)) {

				// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.rice, 0, 4, this.getMaxDamage(stack));
			} else {

				// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.rice, 0, 1, 16);
			}
			player.getEntityWorld().playSound(player, new BlockPos(player), SoundEvents.ENTITY_GENERIC_EAT,SoundCategory.NEUTRAL, 1F, 1F);
		}
	}
}
