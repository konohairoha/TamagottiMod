package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBlade;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.TUtil;

public class GreatSpear extends BaseBlade {

	public GreatSpear(String name, ToolMaterial material, Double atack) {
		super(name, material, atack);
		this.speed = atack;
	}

	//エンティティに攻撃を与えるとそのエンティティにポーション効果を与える
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 0));
		target.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 1200, 1));
		return true;
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
  		return repair.getItem() == ItemInit.fluorite ? true : super.getIsRepairable(toRepair, repair);
  	}

  	//右クリックをした際の挙動を弓に
  	@Override
  	public EnumAction getItemUseAction(ItemStack stack) {
  		return EnumAction.BOW;
  	}

  	//右クリックチャージをやめたときに矢を消費せずに矢を射る
  	@Override
  	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
  		EntityPlayer player = (EntityPlayer) living;
  		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 1F);
    	int a = (int) (30 * f);
        stack.damageItem(1, player);
    	attackAOE(stack, player, false, (6F + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack)) * f), 0);	//与えるダメージ
    	player.getCooldownTracker().setCooldown(this, a);
    	world.playSound(player, new BlockPos(player), TSoundEvent.SWING, SoundCategory.AMBIENT, 1.0F, 1.0F);
  	}

	protected void attackAOE(ItemStack stack, EntityPlayer player, boolean slayAll, float damage, int emcCost) {

		if (player.getEntityWorld().isRemote){ return; }

		float factor = 2.5F * (4 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack));	//ダメージを与える範囲
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(factor);
		List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, aabb);
		DamageSource src = DamageSource.causePlayerDamage(player);
		src.setDamageBypassesArmor();

		if (toAttack.isEmpty()) { return; }
		for (Entity entity : toAttack) {
			if (entity instanceof IMob) {
				entity.attackEntityFrom(src, damage);
			}
		}
    }

  	@Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
        return false;
    }

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		int range = 4 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
  		int damage = 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "右長押しで範囲にいる敵モブにダメージ"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "攻撃範囲：" + range));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "範囲攻撃ダメージ：" + damage));
    }
}
