package tamagotti.util;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import tamagotti.init.entity.ai.EntityAIAnger;

public class TUtil {

	public static final UUID TOOL_REACH = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	public static boolean isEmpty(ItemStack item) {
		if (item == null) {
			item = ItemStack.EMPTY;
			return true;
		}
		return item.getItem() == null || item.isEmpty();
	}

	public static boolean isSameItem(ItemStack i1, ItemStack i2, boolean nullable) {
		if (isEmpty(i1) || isEmpty(i2)) {
			return nullable;
		} else {
			if (i1.getItem() == i2.getItem() && i1.getItemDamage() == i2.getItemDamage()) {
				NBTTagCompound t1 = i1.getTagCompound();
				NBTTagCompound t2 = i2.getTagCompound();
				if (t1 == null && t2 == null) {
					return true;
				} else if (t1 == null || t2 == null) {
					return false;
				} else {
					return t1.equals(t2);
				}
			}
			return false;
		}
	}

	public static int getSize(ItemStack item) {
		return isEmpty(item) ? 0 : item.getCount();
	}

	public static int addStackSize(ItemStack item, int i) {
		if (!isEmpty(item)) {
			int ret = item.getMaxStackSize() - item.getCount();
			ret = Math.min(i, ret);
			item.grow(ret);
			return ret;
		}
		return 0;
	}

	public static void itemRecovery(EntityPlayer player, ItemStack stack, Item item, int data, int amount, int reAmount) {

    	// インベントリ内のアイテム消費
		Object[] obj = getStackFromInventory(player.inventory.mainInventory, item, data, amount);
		if (obj != null) {
			player.inventory.decrStackSize((Integer) obj[0], 1);
			player.inventoryContainer.detectAndSendChanges();
			// 耐久値を全回復
			stack.setItemDamage(stack.getItemDamage() - reAmount);
		}
	}

	public static Object[] getStackFromInventory(NonNullList<ItemStack> inv, Item item, int meta, int minAmount) {
		Object[] obj = new Object[2];
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.get(i);
			if (!stack.isEmpty() && stack.getCount() >= minAmount && stack.getItem() == item && stack.getItemDamage() == meta){
				obj[0] = i;
				obj[1] = stack;
				return obj;
			}
		}
		return null;
	}

	// 停止時の赤石パーティクル
	public static void spawnParticles(World world, BlockPos pos) {
        Random random = world.rand;
		for (int i = 0; i < 8; ++i) {
			double d1 = pos.getX() + random.nextFloat();
			double d2 = (double) (pos.getY() + random.nextFloat()) + 1;
			double d3 = pos.getZ() + random.nextFloat();
			world.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
		}
	}

	// タゲ集め
	public static void tameAIAnger(EntityLiving anger, EntityLivingBase target) {

        boolean isLearning = false;

        for (EntityAITasks.EntityAITaskEntry entry : anger.targetTasks.taskEntries) {

            if (entry.action instanceof EntityAIAnger) {
                EntityAIAnger ai = (EntityAIAnger)entry.action;
                ai.setTarget(target);
                isLearning = true;
                break;
            }
        }

        if (!isLearning) {
            EntityAIAnger ai = new EntityAIAnger(anger, target);
            ai.setTarget(target);
            anger.targetTasks.addTask(0, ai);
        }
    }

	public static boolean isActive(World world, BlockPos pos) {
		return isRedStonePower(world, pos) ? false : true;
	}

	// レッドストーン信号を受けているかを判断する
	public static boolean isRedStonePower(World world, BlockPos pos) {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			redstone = Math.max(redstone, redstoneSide);
		}
		return redstone > 0;
	}


	//右クリックでチャージした量で射程を伸ばす
	public static float getArrowVelocity(int charge, float maxTick) {
		float f = charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > maxTick) {
			f = maxTick;
		}
		return f;
	}

	// ItemStackにNBT保存させる
	public static boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged, String active, String mode) {
		if (oldStack.getItem() != newStack.getItem()) { return true; }
		boolean diffActive = oldStack.hasTagCompound() && newStack.hasTagCompound()
				&& oldStack.getTagCompound().hasKey(active) && newStack.getTagCompound().hasKey(active)
				&& !oldStack.getTagCompound().getTag(active).equals(newStack.getTagCompound().getTag(active));
		boolean diffMode = oldStack.hasTagCompound() && newStack.hasTagCompound()
				&& oldStack.getTagCompound().hasKey(mode) && newStack.getTagCompound().hasKey(mode)
				&& !oldStack.getTagCompound().getTag(mode).equals(newStack.getTagCompound().getTag(mode));
		return diffActive || diffMode;
	}

	public static NBTTagCompound getXToolLevel (ItemStack stack) {

		NBTTagCompound tags = stack.getTagCompound();

		// NBT未登録のときの初期化
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
			tags.setInteger("level", 0);
		}

		return tags;
	}

	public static void callPrivateMethod(Class<?> theClass, Object obj, String name, String obsName) {
		try {
			Method m = ReflectionHelper.findMethod(theClass, name, obsName);
			if (m != null) {
				m.invoke(obj);
			} else {
//				ModCyclic.logger.error("Private function not found on " + theClass.getName() + " : " + name);
			}
		} catch (Exception e) {
//			ModCyclic.logger.error("Reflection error ", e);
		}
	}
}
