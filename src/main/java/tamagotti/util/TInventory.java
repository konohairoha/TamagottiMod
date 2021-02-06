package tamagotti.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TInventory implements IInventory {

	private final int size;
	public final NonNullList<ItemStack> inv;

	public TInventory(int i) {
		this.size = i;
		this.inv = NonNullList.<ItemStack>withSize(this.size, ItemStack.EMPTY);
	}

	public List<ItemStack> getInputs(int from, int to) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		for (int i = from; i <= to; i++) {
			if (TUtil.isEmpty(getStackInSlot(i))) {
				ret.add(ItemStack.EMPTY);
			} else {
				ret.add(getStackInSlot(i));
			}
		}
		return ret;
	}

	public List<ItemStack> getOutputs(int from, int to) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		for (int i = from; i <= to; i++) {
			if (TUtil.isEmpty(getStackInSlot(i))) {
				ret.add(ItemStack.EMPTY);
			} else {
				ret.add(getStackInSlot(i));
			}
		}
		return ret;
	}

	@Override
	public int getSizeInventory() {
		return this.size;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i >= 0 && i < getSizeInventory()) {
			return this.inv.get(i);
		} else
			return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int i, int num) {
		if (i < 0 || i >= this.getSizeInventory()) {
			return ItemStack.EMPTY;
		}

		if (!TUtil.isEmpty(getStackInSlot(i))) {
			ItemStack item = getStackInSlot(i).splitStack(num);
			if (item.getCount() <= 0) {
				item = ItemStack.EMPTY;
			}
			return item;
		} else
			return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		if (i < 0 || i >= this.getSizeInventory()) {
			return;
		} else {
			if (stack == null) {
				stack = ItemStack.EMPTY;
			}
			this.inv.set(i, stack);
			if (!TUtil.isEmpty(stack) && stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}
			this.markDirty();
		}
	}

	@Override
	public String getName() {
		return "たまごっちホッパー";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void markDirty() {}
	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}
	@Override
	public void setField(int id, int value) {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		return true;
	}

	public static int isItemStackable(ItemStack target, ItemStack current) {
		if (TUtil.isSameItem(target, current, false)) {
			int i1 = TUtil.getSize(current) + TUtil.getSize(target);
			if (!TUtil.isEmpty(current) && i1 > current.getMaxStackSize()) {
				return current.getMaxStackSize() - TUtil.getSize(current);
			} else {
				return TUtil.getSize(target);
			}
		}
		return 0;
	}

	public int canIncr(int i, ItemStack get) {
		if (i < 0 || i >= this.getSizeInventory() || TUtil.isEmpty(get))
			return 0;
		else if (TUtil.isEmpty(getStackInSlot(i)))
			return TUtil.getSize(get);
		else {
			return isItemStackable(get, getStackInSlot(i));
		}
	}

	public int incrStackInSlot(int i, ItemStack input) {
		if (i >= 0 || i < this.getSizeInventory() && !TUtil.isEmpty(input)) {
			if (!TUtil.isEmpty(getStackInSlot(i))) {
				int add = isItemStackable(input, getStackInSlot(i));
				TUtil.addStackSize(getStackInSlot(i), add);
				return add;
			} else {
				this.setInventorySlotContents(i, input);
				return TUtil.getSize(input);
			}
		}
		return 0;
	}

	@Override
	public ItemStack removeStackFromSlot(int i) {
		if (i < 0 || i >= this.getSizeInventory())
			return ItemStack.EMPTY;
		else {
			if (!TUtil.isEmpty(getStackInSlot(i))) {
				ItemStack itemstack = this.getStackInSlot(i);
				inv.set(i, ItemStack.EMPTY);
				return itemstack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			inv.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = tag.getTagList("InvItems", 10);
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound tag1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = tag1.getByte("Slot");
			if (b0 >= 0 && b0 < this.getSizeInventory()) {
				inv.set(b0, new ItemStack(tag1));
			}
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (!TUtil.isEmpty(getStackInSlot(i))) {
				NBTTagCompound tag1 = new NBTTagCompound();
				tag1.setByte("Slot", (byte) i);
				getStackInSlot(i).writeToNBT(tag1);
				nbttaglist.appendTag(tag1);
			}
		}
		tag.setTag("InvItems", nbttaglist);
		return tag;
	}

	@Override
	public boolean isEmpty() {
		boolean flag = true;
		for (ItemStack item : inv) {
			if (!TUtil.isEmpty(item)) {
				flag = false;
			}
		}
		return flag;
	}
}
