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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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
import tamagotti.init.entity.projectile.EntityShot;

public class TSpinner extends BaseBow {

	private final int data;

	public TSpinner(String name, int meta, int value) {
		super(name);
        setMaxDamage(value);
        this.data = meta;
    }

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) { return new ActionResult(EnumActionResult.SUCCESS, stack); }

        player.setActiveHand(hand);

		if(stack.getItemDamage() != this.getMaxDamage(stack)) {
	        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
	        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

			EntityShot arrow = new EntityShot(world, player);
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F + this.data + j, 1.0F);
    		arrow.setDamage(arrow.getDamage() - (float)0.5D + this.data * 0.5 + k);

			if (!world.isRemote) {
        		world.spawnEntity(arrow);
				stack.damageItem(1, player);
			}

    		player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 2));
    		world.playSound(null, new BlockPos(player),SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 64F );

		} else {
			if (!player.capabilities.isCreativeMode) { player.getCooldownTracker().setCooldown(this, 20); }
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	//右クリックでチャージした量で射程を伸ばす
	public static float getArrowVelocity(int charge) {
		float f = charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 5.0F) {
			f = 5.0F;
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
			int a = (int) (30 / 5 * f + 2);
	  		stack.setItemDamage(stack.getItemDamage() - a);
		}
	}

	//最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 100;
	}

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = 5 + this.data * 3 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		float shot = 3.0F + this.data + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
  		tooltip.add(I18n.format(TextFormatting.GOLD + "5秒間のスニーク右クリックで耐久全回復"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
