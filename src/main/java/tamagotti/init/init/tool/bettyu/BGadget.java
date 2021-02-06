package tamagotti.init.items.tool.bettyu;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.entity.projectile.EntityTorpedo;

public class BGadget extends BBow {

	public BGadget(String name) {
		super(name);
        this.setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
		EntityPlayer player = (EntityPlayer) living;
		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_CLOTH_BREAK, SoundCategory.NEUTRAL, 2.0F, 6.0F);
		player.addStat(StatList.getObjectUseStats(this));
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		world.playSound(player, new BlockPos(player), SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.NEUTRAL, 2.0F, 16.0F);
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		EntityTorpedo arrow = new EntityTorpedo(world, player);
		arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrow.setDamage(arrow.getDamage() + j * 0.5D);
        if (!world.isRemote){
        	world.spawnEntity(arrow);
        	player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6, 9));
        }
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	//右クリックをした際の挙動を盾に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：??"));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：??"));
    }
}
