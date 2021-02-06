package tamagotti.init.base;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.init.entity.projectile.EntityTArrow;
import tamagotti.util.TUtil;

public class BaseBow extends ItemBow {

	public BaseBow(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
		setMaxStackSize(1);
		ItemInit.itemList.add(this);
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (world.isRemote) { return; }
		EntityPlayer player = (EntityPlayer) living;
		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 1F);
		int j = this.getEnchant(Enchantments.POWER, stack);
		int k = this.getEnchant(Enchantments.PUNCH, stack);
		EntityTArrow arrow = new EntityTArrow(world, player);
		arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
		arrow.setIsCritical(f == 1F);
		world.spawnEntity(arrow);
		world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1F, 1F);
		player.addStat(StatList.getObjectUseStats(this));

		if (j > 0) {
			arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
		}

		if (k > 0) {
			arrow.setKnockbackStrength(k);
		}

		if (this.getEnchant(Enchantments.FLAME, stack) > 0) {
			arrow.setFire(100);
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

	// エンチャントのレベルの取得
	public int getEnchant (Enchantment ench, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(ench, stack);
	}
}
