package tamagotti.init.items.tool.yukari;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityShot;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.TUtil;

public class YSweeperC extends BaseBow {

	public YSweeperC(String name) {
		super(name);
        setMaxDamage(256);
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);

		if (player.isSneaking()) {

	        Vec3d vec3d = player.getLookVec();
	        vec3d = vec3d.normalize().scale(4);
	        player.motionX += vec3d.x;
	        player.motionZ += vec3d.z;
        	stack.damageItem(1, player);
        	player.getCooldownTracker().setCooldown(this, 7 - l * 3);
        	world.playSound(player, new BlockPos(player), TSoundEvent.SLIDE, SoundCategory.AMBIENT, 0.5F, 1.0F);

		} else {

			if (stack.getItemDamage() < this.getMaxDamage(stack)) {
				EntityShot arrow0 = new EntityShot(world, player);
		        arrow0.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F + k, 1.0F);
		        arrow0.posX += 0.3F;
		        arrow0.posZ -= 0.3F;
				EntityShot arrow1 = new EntityShot(world, player);
		        arrow1.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F + k, 1.0F);
		        arrow1.posX -= 0.3F;
				arrow1.posZ += 0.3F;

				if (!world.isRemote) {
		            arrow0.setDamage(arrow0.getDamage() + j * 0.33D);
		            arrow1.setDamage(arrow1.getDamage() + j * 0.33D);
					world.spawnEntity(arrow0);
		        	world.spawnEntity(arrow1);
		        	stack.damageItem(1, player);
		        	player.getCooldownTracker().setCooldown(this, 8 - l * 3);
				}

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
		            arrow0.setFire(50);
		            arrow1.setFire(50);
		        }
		    	world.playSound(player, new BlockPos(player), TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);

			} else {
	        	// 耐久回復(消費アイテム、データ値、個数、回復量)
	        	TUtil.itemRecovery(player, stack, ItemInit.tamagotti, 0, 1, this.getMaxDamage(stack));
			}
		}
		return new ActionResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {}
	@Override
	public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE; }

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 6;
  		float shot = (float) (2.5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
  		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックで向いた方向にスライド"));
		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
  			tooltip.add(I18n.format(TextFormatting.RED + "たまごっちを消費して耐久回復"));
  		}
	  	tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
	  	tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
