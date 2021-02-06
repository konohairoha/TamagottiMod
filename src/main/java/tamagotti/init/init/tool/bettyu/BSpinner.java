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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityShotter;

public class BSpinner extends BaseBow {

	private final int data;

	public BSpinner(String name, int meta) {
        super(name);
        this.setMaxDamage(128);
        this.data = meta;
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);

		if(!player.isSneaking() && !world.isRemote){

    		if(stack.getItemDamage() != this.getMaxDamage(stack)) {

				int dame = (this.data * 2);
		        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		        int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);

				EntityShotter arrow = new EntityShotter(world, player);
				arrow.setDamage(arrow.getDamage() + 4 + j + dame);
				arrow.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.0F, 0.0F);
				arrow.shoot(arrow.motionX, arrow.motionY, arrow.motionZ, 4.0F + k + dame, 6 - (l * 6));
				world.spawnEntity(arrow);

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					arrow.setFire(200);
				}

				world.playSound(null, new BlockPos(player), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1F, 64F);
				stack.damageItem(1, player);
			} else {
				player.getCooldownTracker().setCooldown(this, 20);
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	//右クリックでチャージした量で射程を伸ばす
	public static float getArrowVelocity(int charge) {
		float f = charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 16.0F) {
			f = 16.0F;
		}
		return f;
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		EntityPlayer player = (EntityPlayer) living;
		if(player.isSneaking() || stack.getItemDamage() == this.getMaxDamage(stack)) {
			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			float f = getArrowVelocity(i);
			int a = (int) (8 * f);
	  		stack.setItemDamage(stack.getItemDamage() - a);
		}
	}

    //エンチャントが付いてなければ付ける
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING ,stack) == 0){
        	if(this.data == 0) {
        		stack.addEnchantment(Enchantments.MENDING, 3);
        	} else {
        		stack.addEnchantment(Enchantments.MENDING, 5);
        	}
        }
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = (4 + this.data * 2 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack)));
  		int shot = 4 + this.data * 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "スニーク右クリックで耐久回復"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
