package tamagotti.init.items.tool.bettyu;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import tamagotti.init.entity.projectile.EntityShot;
import tamagotti.init.event.TSoundEvent;

public class BManu extends BBow {

	public BManu(String name) {
		super(name);
        this.setMaxStackSize(1);
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {

	        Vec3d vec3d = player.getLookVec();
	        vec3d = vec3d.normalize().scale(5.5);
	        player.motionX += vec3d.x;
	        player.motionY += vec3d.y * 0.3;
	        player.motionZ += vec3d.z;
        	player.getCooldownTracker().setCooldown(this, 4);
        	world.playSound(player, new BlockPos(player), TSoundEvent.SLIDE, SoundCategory.AMBIENT, 0.5F, 1.0F);

		} else {

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

			EntityShot arrow0 = new EntityShot(world, player);
	        arrow0.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.25F + k, 1.0F);
	        arrow0.posX += 0.3F;
	        arrow0.posZ -= 0.3F;
			EntityShot arrow1 = new EntityShot(world, player);
	        arrow1.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.25F + k, 1.0F);
	        arrow1.posX -= 0.3F;
	        arrow1.posZ += 0.3F;

			if (!world.isRemote) {
				world.spawnEntity(arrow0);
	        	world.spawnEntity(arrow1);
	            arrow0.setDamage(arrow0.getDamage() + j * 0.45D);
	            arrow1.setDamage(arrow1.getDamage() + j * 0.45D);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
	            arrow0.setFire(50);
	            arrow1.setFire(50);
	        }

        	player.getCooldownTracker().setCooldown(this, 5);
	    	world.playSound(player, new BlockPos(player), TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);
		}
		return new ActionResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {}

	//右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE; }

	//手に持った時にポーション効果を付ける
    public void func_77663_a(ItemStack stack, World world, Entity entity, int off, boolean main) {
		if (main || off < 1) {
	    	EntityLivingBase living = (EntityLivingBase) entity;
	    	living.fallDistance = 0;
		}
    }

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = (5 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack) ));
  		float shot = (float) 2.25 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックで向いてる方向にテレポート"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
