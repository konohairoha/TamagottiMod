package tamagotti.init.items.tool.blade.voiceroid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityZunda;
import tamagotti.init.items.tool.tamagotti.iitem.IRelode;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.PlayerHelper;
import tamagotti.util.TUtil;

public class ZundaBow extends BaseBow implements IRelode {

	public static boolean maxC = false;

	public ZundaBow(String name) {
		super(name);
		this.setMaxDamage(1024);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
	    	@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (entityIn == null) {
					return 0.0F;
				} else {
	            	return entityIn.getActiveItemStack().getItem() != ItemInit.zundabow ? 0.0F : (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
	            }
	        }
	    });
	    this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
	    	@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
	        	return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
	        }
	    });
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (stack.getItemDamage() < this.getMaxDamage(stack)) {

			EntityPlayer player = (EntityPlayer) living;
			this.zunShot(stack, world, player, timeLeft);
			player.addStat(StatList.getObjectUseStats(this));
		}
	}

	// ずんだアロー射撃メソッド
	public void zunShot (ItemStack stack, World world, EntityPlayer player, int timeLeft) {

		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 1F);
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		maxC = f == 1.0F;

		EntityZunda arrow = new EntityZunda(world, player);
		arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.5F, 0.0F);
		arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 2.0F + f + k, 0);
		if (!world.isRemote) {
			arrow.setDamage(arrow.getDamage() + (8 + j) * f);
			world.spawnEntity(arrow);
			stack.damageItem(1, player);
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
				arrow.setFire(150);
			}
		}
		world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT,
				SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 8 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		int shot = 3 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーでずんだアローを4つ消費して耐久を全回復"));
  		} else {
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.RELODE) + "キーでずんだアローを1つ消費して耐久を16回復"));
  		}
		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックでずんだアローを１個消費して耐久16回復"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }

	@Override
	public void doRelode(EntityPlayer player, ItemStack stack) {

		if (stack.getItemDamage() != 0) {

			//インベントリ内のアイテム消費
			if (TUtil.getStackFromInventory(player.inventory.mainInventory, ItemInit.zundaarrow, 0, 1) != null) {

				if (stack.getItemDamage() == this.getMaxDamage(stack)) {

					// 耐久回復(消費アイテム、データ値、個数、回復量)
					TUtil.itemRecovery(player, stack, ItemInit.zundaarrow, 0, 4, this.getMaxDamage(stack));
				} else {

					// 耐久回復(消費アイテム、データ値、個数、回復量)
					TUtil.itemRecovery(player, stack, ItemInit.zundaarrow, 0, 1, 16);
				}
			}
			PlayerHelper.swingItem(player, player.getActiveHand());
		}
	}
}
