package tamagotti.init.items.tool.tamagotti;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityBlaster;
import tamagotti.util.TUtil;

public class TBlaster extends BaseBow {

	private final int data;

	public TBlaster(String name, int meta, int value) {
		super(name);
        setMaxDamage(value);
        this.data = meta;
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			if (stack.getItemDamage() < this.getMaxDamage(stack)) {


				if (!world.isRemote) {

					int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
					int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
					int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
					int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);

					EntityBlaster blaster = null;

					if (this.data == 0 || this.data == 1) {

						blaster = new EntityBlaster(world, player, 2.5F + this.data + f);
						blaster.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1F, 0.0F);
						blaster.shoot(blaster.motionX, blaster.motionY, blaster.motionZ, (float) ((1.35F + this.data * 0.5) + k), 0);
						blaster.setDamage(blaster.getDamage() + 10 + j * 2 + (this.data * 8));

						if (stack.getItemDamage() != this.getMaxDamage(stack)) {
							player.getCooldownTracker().setCooldown(this, 13 + (this.data * 3) / (l + 1));
							player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 13 + (this.data * 3), 1));
						}

					} else {

						blaster = new EntityBlaster(world, player, 4F + f);
						blaster.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1F, 0.0F);
						blaster.shoot(blaster.motionX, blaster.motionY, blaster.motionZ, 3.15F + k, 0);
						blaster.setDamage(blaster.getDamage() + 26 + j * 3);

						if (stack.getItemDamage() != this.getMaxDamage(stack)) {
							player.getCooldownTracker().setCooldown(this, 14 / (l + 1));
						}
					}

					if (stack.getItemDamage() != this.getMaxDamage(stack)) {
						world.spawnEntity(blaster);
						stack.damageItem(1, player);
						player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 9));
					}
				}
			} else {
	        	// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.tamagotti, 0, 1, this.getMaxDamage(stack));
			}
		}
	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 4 + (10 + (this.data * 8) + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 2));
  		float shot = (float) ((float) 1.85 + (this.data) * 0.65) + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
  		if (stack.getItemDamage() == this.getMaxDamage(stack)){
  			tooltip.add(I18n.format(TextFormatting.RED + "たまごっちを消費して耐久回復"));
  		}
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "直撃時威力：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度  ：" + shot));
    }
}
