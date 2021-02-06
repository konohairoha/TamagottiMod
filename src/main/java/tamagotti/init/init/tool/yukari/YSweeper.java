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
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityShot;
import tamagotti.init.entity.projectile.EntityShotter;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.TUtil;

public class YSweeper extends BaseBow {

	private final int data;

	public YSweeper(String name, int meta, int value) {
		super(name);
        setMaxDamage(value);
        this.data = meta;
    }

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
        	int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
			if (stack.getItemDamage() < this.getMaxDamage(stack)) {

				EntityShot arrow0 = new EntityShot(world, player);
				EntityShot arrow1 = new EntityShot(world, player);
            	EntityShotter arrow = new EntityShotter(world, player);

    			if(this.data == 0) {

                    arrow0.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F + k, 1.0F);
                    arrow0.motionY += 0.01D * 20F;
        	        arrow0.posY += 0.02F;
                    arrow1.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F + k, 1.0F);
                    world.spawnEntity(arrow0);
    	        	world.spawnEntity(arrow1);
    	        	world.playSound(player, new BlockPos(player), TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);
    	        	player.getCooldownTracker().setCooldown(this, 7  - l * 3);
    	            arrow0.setDamage(arrow0.getDamage() + j * 0.3D);
    	            arrow1.setDamage(arrow1.getDamage() + j * 0.3D);
					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
    	            	arrow0.setFire(150);
    	            	arrow1.setFire(150);
    	            }

				} else if (this.data == 1) {

                    arrow.setDamage(arrow.getDamage() + 5 + j);
                    //弾の初期弾速と集弾性の下限値
                    int a = 16;
                    if(player.isSneaking()) {
                    	a = 8;
                    }
    				arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 2.5F, 2.5F, 2.5F);
    	            arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 3.0F + k, a);
    	        	player.getCooldownTracker().setCooldown(this, 5 - l * 2);
    	        	world.spawnEntity(arrow);
    	        	world.playSound(player, new BlockPos(player), TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);

                } else {

                    arrow.setDamage(arrow.getDamage() + 6 + j);
                    //弾の初期弾速と集弾性の下限値
                    int a = 8;
                    if(player.isSneaking()) {
                    	a = 4;
                    }
    				arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 2.5F, 2.5F, 2.5F);
    	            arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 4.5F + k, a);
    	        	player.getCooldownTracker().setCooldown(this, 3  - l * 2);
    	        	world.spawnEntity(arrow);

                }
	        	stack.damageItem(1, player);

            } else {
            	// 耐久回復(消費アイテム、データ値、個数、回復量)
            	TUtil.itemRecovery(player, stack, ItemInit.tamagotti, 0, 1, this.getMaxDamage(stack));
            }
        }
    	world.playSound(player, player.posX, player.posY, player.posZ, TSoundEvent.SHOT, SoundCategory.AMBIENT, 1.0F, 1.0F);
		player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {}
	@Override
	public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE;}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		if (stack.getItemDamage() == this.getMaxDamage(stack)) {
			tooltip.add(I18n.format(TextFormatting.RED + "たまごっちを消費して耐久回復"));
		}

		if (this.data != 0) {
  	  		int damage = 5 + this.data + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  	  		float shot = (float) (3 + ((this.data - 1) * 1.5) + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
  		} else {
  	  		int dama = 6 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
  	  		float shot = (float) (3.5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + dama));
  	  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
  		}
    }
}
