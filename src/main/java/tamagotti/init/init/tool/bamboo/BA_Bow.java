package tamagotti.init.items.tool.bamboo;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseBow;
import tamagotti.init.entity.projectile.EntityTKArrow;
import tamagotti.init.event.TSoundEvent;
import tamagotti.util.TUtil;

public class BA_Bow extends BaseBow {

	public int data;

	public BA_Bow(String name, int maxDame, int data) {
        super(name);
        setMaxDamage(maxDame);
        this.data = data;
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
				if (entity == null) { return 0F; }
                return (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20F;
            }
        });
		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1F : 0F;
            }
        });
    }

	/**
	 * 0 = 竹弓
	 * 1 = 三連装竹弓
	 */

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (this.checkArrow(player) != null || this.getEnchant(Enchantments.INFINITY, stack) > 0 ||
				player.capabilities.isCreativeMode) {
			player.setActiveHand(hand);
			return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult(EnumActionResult.FAIL, stack);
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		if (!(living instanceof EntityPlayer)) { return; }

		EntityPlayer player = (EntityPlayer) living;

		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		float f = TUtil.getArrowVelocity(i, 2F);
        int j = this.getEnchant(Enchantments.POWER, stack);
        float k = this.getEnchant(Enchantments.PUNCH, stack);
        boolean infinity = this.getEnchant(Enchantments.INFINITY, stack) > 0;
        boolean isShot = false;

		//インベントリ内のアイテム消費
		Object[] obj = this.checkArrow(player);
		ItemStack item = obj != null ? ((ItemStack)obj[1]).copy() : new ItemStack(ItemInit.bambooarrow);
		item.setCount(1);

		if (!world.isRemote) {

    		EntityTKArrow arrow = new EntityTKArrow(world, player, item, infinity, this.data == 1);
            arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0F, (2F + (k / 2F)) * f, 1F);
            arrow.setDamage(arrow.getDamage());
            arrow.setIsCritical(f == 2F);

			if (j > 0) {
                arrow.setDamage(arrow.getDamage() + j * 0.5D + 0.5D);
            }

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                arrow.setFire(100);
            }

			// クリエ以外
			if (!player.capabilities.isCreativeMode) {
				if (obj != null || infinity) {

					// 無限エンチャ以外なら矢を消費
					if (!infinity) {
						player.inventory.decrStackSize((Integer) obj[0], 1);
						player.inventoryContainer.detectAndSendChanges();
					}

					world.spawnEntity(arrow);
					stack.damageItem(1, player);
					isShot = true;
				}
			}

			// クリエなら
			else {
				world.spawnEntity(arrow);
				isShot = true;
        	}
        }

		// 射撃に成功して三連装竹弓なら
		if (isShot && this.data == 1) {

			// 向きの取得
			Vec3d vec = this.lookVector(player.rotationYaw, player.rotationPitch);
			Vec3d left = vec.crossProduct(new Vec3d(0, 1, 0));
			Vec3d right = vec.crossProduct(new Vec3d(0, -1, 0));

			// 左右の矢の射撃
			this.spawnArrow(world, player, item, false, stack, f, left.normalize());
			this.spawnArrow(world, player, item, false, stack, f, right.normalize());
		}

		if (this.data == 1) {
	        world.playSound(player, new BlockPos(player), TSoundEvent.ARROW, SoundCategory.NEUTRAL, 1F, 1F);
		} else {
	        world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1F, 1F);
		}
		player.addStat(StatList.getObjectUseStats(this));

	}

	// 矢のチェック
	public Object[] checkArrow (EntityPlayer player) {
		Item item = this.data == 1 ? ItemInit.re_bambooarrow : ItemInit.bambooarrow;
		return TUtil.getStackFromInventory(player.inventory.mainInventory, item, 0, 1);
	}

	// 向きの取得
	public Vec3d lookVector(float rotYaw, float rotPitch) {
		return new Vec3d(
				Math.sin(rotYaw) * Math.cos(rotPitch),
				Math.sin(rotPitch),
				Math.cos(rotYaw) * Math.cos(rotPitch));
	}

	// 左右の矢の射撃
	public void spawnArrow(World world, EntityPlayer player, ItemStack item, boolean flag, ItemStack stackBow, float charge, Vec3d vec) {

		if (!world.isRemote) {

			EntityTKArrow arrow = new EntityTKArrow(world, player, item, flag, this.data == 1);
			int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
			arrow.pickupStatus = EntityTKArrow.PickupStatus.DISALLOWED;
			arrow.posX += vec.x;
			arrow.posY += vec.y;
			arrow.posZ += vec.z;
            arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0F, (2F + (punch / 2F)) * charge, 1F);
		    arrow.setIsCritical(charge == 2F);

			int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);

			if (power > 0) {
				arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
				arrow.setFire(100);
			}
			world.spawnEntity(arrow);
		}
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
  		int damage = ((5 + EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack)) + 1) *2;
  		int shot = (int) (2 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack) * 0.5) * 2);
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "威力    ：" + damage));
  		tooltip.add(I18n.format(TextFormatting.YELLOW + "射撃速度：" + shot));
    }
}
