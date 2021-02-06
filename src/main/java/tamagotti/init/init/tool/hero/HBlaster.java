package tamagotti.init.items.tool.hero;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityBlaster;

public class HBlaster extends BaseBow {

	public HBlaster(String name) {
        super(name);
        setMaxDamage(4096);
    }

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {}
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {return EnumAction.NONE;}

	//右クリックした際に爆発する弾を発射
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
			EntityBlaster blaster = new EntityBlaster(world, player, 3F);
			blaster.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.0F, 0.0F);
			blaster.shoot(blaster.motionX, blaster.motionY, blaster.motionZ, 2.0F + k, 0);
			world.spawnEntity(blaster);
        	stack.damageItem(1, player);
			blaster.setDamage(blaster.getDamage() + 13 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4, 9));
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		int damage = 17 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
  		int shot = 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		tooltip.add(I18n.format(TextFormatting.GOLD + "右クリック長押しで連射可能"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "直撃威力：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}