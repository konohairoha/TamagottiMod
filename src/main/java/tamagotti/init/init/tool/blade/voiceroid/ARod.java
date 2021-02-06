package tamagotti.init.items.tool.blade.voiceroid;

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
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
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
import tamagotti.init.ItemInit;
import tamagotti.init.entity.projectile.EntityRitter;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.TUtil;

public class ARod extends ItemSword {

	public ARod(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (entityIn == null) {
					return 0.0F;
				} else {
	            	return entityIn.getActiveItemStack().getItem() != ItemInit.akanerod ? 0.0F : (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
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

    //敵に攻撃したら自分にポーション効果
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
  		stack.damageItem(1, attacker);
  		target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 4, 48));
  		target.setFire(200);
  		return true;
  	}

  	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == (ItemInit.redberyl) ? true : super.getIsRepairable(toRepair, repair);
  	}

  	//右クリックをした際の処理
  	@Override
  	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
  		player.setActiveHand(hand);
  		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  	}

  	//右クリックチャージをやめたときに矢を消費せずに矢を射る
  	@Override
  	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
  		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 2F);
		int n = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
		int o = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
		int q = (int) (f * 3);
		EntityPlayer player = (EntityPlayer) living;
		EntityRitter arrow[] = new EntityRitter[1 + q];

		for(int p = 0; p < arrow.length; p++) {
			arrow[p] = new EntityRitter(world, player);
			arrow[p].setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);
			arrow[p].shoot(arrow[p].motionX, arrow[p].motionY, arrow[p].motionZ, 1.0F, q * 2);
			if (!world.isRemote) {
  	  			world.spawnEntity(arrow[p]);
  	  			arrow[p].setDamage(arrow[p].getDamage() + 5 * (f + n + o));
  	  			arrow[p].setFire(200);
			}
		}
    	stack.damageItem(1, player);
	  	world.playSound(player, new BlockPos(player), TSoundEvent.FIRE, SoundCategory.AMBIENT, 1.0F, 1.0F);
	}

  	//最大１分間出来るように
  	@Override
  	public int getMaxItemUseDuration(ItemStack stack) {
  		return 72000;
  	}

  	//右クリックをした際の挙動を弓に
  	@Override
  	public EnumAction getItemUseAction(ItemStack stack) {
  		return EnumAction.BOW;
  	}

  	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + "チャージ（２秒）で弾が7つに"));
    }
}
