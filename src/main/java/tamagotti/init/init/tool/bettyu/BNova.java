package tamagotti.init.items.tool.bettyu;

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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityNova;
import tamagotti.init.entity.projectile.EntityScorp;
import tamagotti.init.event.TSoundEvent;
import tamagotti.init.items.tool.tamagotti.iitem.IZoom;
import tamagotti.util.TUtil;

public class BNova extends BBow implements IZoom {

	private final int data;

	public BNova(String name, int meta) {
		super(name);
        this.setMaxStackSize(1);
        this.data = meta;
	}

	/**
	 * 0 = ノヴァ
	 * 1 = スコープ
	 */

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;

		if (!world.isRemote) {

			float f = TUtil.getArrowVelocity(this.getMaxItemUseDuration(stack) - timeLeft, 1F);
	        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
	        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

			if (this.data == 0) {

        		EntityNova nova = new EntityNova(world, player);
        		nova.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 0.0F);
        		nova.shoot(nova.motionX, nova.motionY, nova.motionZ, 2.0F + k, 0);
    			world.spawnEntity(nova);
    			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6, 9));
    	        player.getCooldownTracker().setCooldown(this, 20);

        	} else {

        		EntityScorp arrow = new EntityScorp(world, player);
                arrow.setDamage(arrow.getDamage() + (21 + j * 4) * f * f);
                arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 5.0F + k, 0);
    			arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.0F, 0.0F);
	        	player.getCooldownTracker().setCooldown(this, (int) (20 * f));
	        	world.spawnEntity(arrow);

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
	        		arrow.setFire(200);
	        	}
        	}
        }

        if(this.data == 1) {
        	world.playSound(player, new BlockPos(player), TSoundEvent.TYA, SoundCategory.AMBIENT, 0.3F, 1.0F);
        }
	}

	@Override
	public float zoomItem() {
		return this.data == 1 ? 4.0F : 0;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int shot = 3 + this.data * 2+ EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
        if(this.data == 1) {
      		int damage = 21 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) * 4);
        	tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク状態でズーム"));
      		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
      		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
        } else {
      		tooltip.add(I18n.format(TextFormatting.YELLOW + "直撃時威力：??"));
      		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度  ：" + shot));
        }
    }
}
