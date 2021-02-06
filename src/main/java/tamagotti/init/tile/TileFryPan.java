package tamagotti.init.tile;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import tamagotti.init.blocks.furnace.FryPan;

public class TileFryPan extends TileEntityLockable implements ITickable, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;

	@Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.furnaceItemStacks) {
			if (!itemstack.isEmpty()) { return false; }
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.furnaceItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack iStack = this.furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(iStack)
				&& ItemStack.areItemStackTagsEqual(stack, iStack);
		this.furnaceItemStacks.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if (index == 0 && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.furnaceCustomName : "フライパン";
	}

	@Override
	public boolean hasCustomName() {
		return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
	}

	public void setCustomInventoryName(String par1) {
		this.furnaceCustomName = par1;
	}

	public static void registerFixesFurnace(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY,
				new ItemStackDataLists(TileEntityFurnace.class, new String[] { "Items" }));
	}

	@Override
	public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, this.furnaceItemStacks);
        this.furnaceBurnTime = tags.getInteger("BurnTime");
        this.cookTime = tags.getInteger("CookTime");
        this.totalCookTime = tags.getInteger("CookTimeTotal");
		this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks.get(1));
		if (tags.hasKey("CustomName", 8)) {
            this.furnaceCustomName = tags.getString("CustomName");
        }
    }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		tags.setInteger("BurnTime", (short) this.furnaceBurnTime);
		tags.setInteger("CookTime", (short) this.cookTime);
		tags.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(tags, this.furnaceItemStacks);
		if (this.hasCustomName()) {
			tags.setString("CustomName", this.furnaceCustomName);
		}
		return tags;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void update() {

		boolean flag = this.isBurning();
		boolean flag1 = false;
		if (this.isBurning()) {
			--this.furnaceBurnTime;
		}

		if (!this.world.isRemote) {
			ItemStack stack = this.furnaceItemStacks.get(1);

			if (this.isBurning() || !stack.isEmpty() && !this.furnaceItemStacks.get(0).isEmpty()) {

				if (!this.isBurning() && this.canSmelt()) {
					this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(stack);
					this.currentItemBurnTime = this.furnaceBurnTime;
					if (this.isBurning()) {
						flag1 = true;
						if (!stack.isEmpty()) {
							Item item = stack.getItem();
							stack.shrink(1);
							if (stack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(stack);
								this.furnaceItemStacks.set(1, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				FryPan.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (flag1) {
			this.markDirty();
		}
	}

	public int getCookTime(ItemStack stack) {
		return 140;
	}

	private boolean canSmelt() {

		if (this.furnaceItemStacks.get(0).isEmpty()) { return false; }

		ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));
		if (stack.isEmpty()) { return false; }

		ItemStack stack1 = this.furnaceItemStacks.get(2);

		if (stack1.isEmpty()) { return true; }

		else if (!stack1.isItemEqual(stack)) { return false; }

		else if (stack1.getCount() + stack.getCount() <= this.getInventoryStackLimit()
				&& stack1.getCount() + stack.getCount() <= stack1.getMaxStackSize()) {
			return true;
		} else {
			return stack1.getCount() + stack.getCount() <= stack.getMaxStackSize();
		}
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = this.furnaceItemStacks.get(0);
			ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
			ItemStack itemstack2 = this.furnaceItemStacks.get(2);
			if (itemstack2.isEmpty()) {
				this.furnaceItemStacks.set(2, itemstack1.copy());
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}
			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1
					&& !this.furnaceItemStacks.get(1).isEmpty()
					&& this.furnaceItemStacks.get(1).getItem() == Items.BUCKET) {
				this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
			}
			itemstack.shrink(1);
		}
	}

	public static boolean isItemFuel(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) > 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			ItemStack itemstack = this.furnaceItemStacks.get(1);
			return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		} else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getGuiID() {
		return "minecraft:furnace";
	}

	@Override
	public Container createContainer(InventoryPlayer Inventory, EntityPlayer playerIn) {
		return new ContainerFurnace(Inventory, this);
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.furnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.furnaceBurnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		this.furnaceItemStacks.clear();
	}

	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else
				return (T) handlerSide;
		return super.getCapability(capability, facing);
	}

	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}
}
