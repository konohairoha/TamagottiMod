package tamagotti.init.items.tool.blade;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.iitem.IMode;
import tamagotti.key.ClientKeyHelper;
import tamagotti.key.TKeybind;
import tamagotti.util.ItemHelper;
import tamagotti.util.PlayerHelper;

public class MSword extends ItemSword implements IMode {

	public static final String TAG_ACTIVE = "Active";
	public static final String TAG_MODE = "Mode";
	public int yActive = 0;

	public MSword(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
        this.setMaxDamage(1024);
		ItemInit.itemList.add(this);
	}

	//敵に攻撃したら自分にポーション効果
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		if (stack.getItemDamage() + 2 < this.getMaxDamage(stack) && stack.getTagCompound() != null) {

			int mode = stack.getTagCompound().getInteger(TAG_MODE);

			if (mode == 0) {
				target.setFire(200);
				stack.damageItem(2, attacker);
			} else if (mode == 1) {
				target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 1));
				stack.damageItem(2, attacker);
			} else if (mode == 2) {
				attacker.addPotionEffect(new PotionEffect(MobEffects.SPEED, 12000, 4));
				stack.damageItem(1, attacker);
			} else if (mode == 3) {
				attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 12000, 2));
				stack.damageItem(3, attacker);
			}
		}
		return true;
	}

	public static float getArrowVelocity(int charge) {
  		float f = charge / 20.0F;
  		f = (f * f + f * 2.0F) / 3.0F;
  		return f;
  	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
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

  	@Override
  	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
  		if (living instanceof EntityPlayer) {
  			EntityPlayer player = (EntityPlayer) living;
  			PlayerHelper.swingItem(player, player.getActiveHand());
  			if(!player.isSneaking()) {
  				int i = this.getMaxItemUseDuration(stack) - timeLeft;
  	  			float f = getArrowVelocity(i);
  	  			int a = (int) (10 * f);
  	  			stack.setItemDamage(stack.getItemDamage() - a);
  			}
  		}
  	}

	@Override
	public void changeMode(ItemStack stack, EntityPlayer player) {
		PlayerHelper.swingItem(player, player.getActiveHand());
		World world = player.getEntityWorld();
		if (!world.isRemote) {

			int newMode = 0;

			switch (ItemHelper.getNBT(stack).getInteger(TAG_MODE)) {
			case 0:
				newMode = 1;
				player.sendMessage(new TextComponentString("ウィザーモード"));
				break;
			case 1:
				newMode = 2;
				player.sendMessage(new TextComponentString("ダッシュモード"));
				break;
			case 2:
				newMode = 3;
				player.sendMessage(new TextComponentString("アタックモード"));
				player.clearActivePotions();
				break;
			case 3:
				newMode = 0;
				player.sendMessage(new TextComponentString("フレイムモード"));
				player.clearActivePotions();
				break;
			}
			player.getCooldownTracker().setCooldown(this, 10);
			ItemHelper.getNBT(stack).setInteger(TAG_MODE, newMode);
			world.playSound(null, new BlockPos(player), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.NEUTRAL, 4F, 1024F);
		}
	}

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(TKeybind.MODE) + "キーで属性切り替え"));
  	}
}
