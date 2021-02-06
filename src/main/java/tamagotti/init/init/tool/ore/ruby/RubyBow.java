package tamagotti.init.items.tool.ore.ruby;

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
import tamagotti.init.entity.projectile.EntityRubyShot;
import tamagotti.util.TUtil;

public class RubyBow extends BaseBow {

	public RubyBow(String name) {
        super(name);
        setMaxDamage(512);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
				if (entity == null) {
					return 0.0F;
				} else {
					return entity.getActiveItemStack().getItem() != ItemInit.rubybow ? 0.0F
							: (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20.0F;
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
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			float f = TUtil.getArrowVelocity(i, 1F);
			float k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
			int rockTime = 5 + (j * 5);

			EntityRubyShot arrow = new EntityRubyShot(world, player, rockTime);
			arrow.setDamage((6 + j) * f);
			stack.damageItem(1, player);
			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);
	        arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, (2F + k) * f, 0);
			player.getCooldownTracker().setCooldown(this, 60);

			if (!world.isRemote) {
				world.spawnEntity(arrow);
			}

			world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			player.addStat(StatList.getObjectUseStats(this));

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
				arrow.setFire(100);
			}
		}
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		int rockTime = 5 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 5);
  		int damage = 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		float shot = (float) 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

		tooltip.add(I18n.format(TextFormatting.GOLD + "弾を当てた敵にスタン"));
		tooltip.add(I18n.format(TextFormatting.YELLOW + "停止時間：" + rockTime + "秒"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
  	}
}
