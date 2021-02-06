package tamagotti.init.blocks.chest.thopper;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import tamagotti.util.EnumSide;
import tamagotti.util.TInventory;
import tamagotti.util.TUtil;

public class TileTHopper extends TileEntity implements IInteractionObject, ILockableContainer, ITickable, ISidedInventory {

	private LockCode code = LockCode.EMPTY_CODE;
	protected TInventory inv = new TInventory(5);
	public int cooldown = 0;
	public boolean flag = false;

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean isLocked() {
		return this.code != null && !this.code.isEmpty();
	}

	@Override
	public LockCode getLockCode() {
		return this.code;
	}

	@Override
	public void setLockCode(LockCode code) {
		this.code = code;
	}

	protected IItemHandler createUnSidedHandler() {
		return new InvWrapper(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public void update() {

		this.updateTile();
		this.onTickUpdate();

		if (!this.world.isRemote) {
			this.onServerUpdate();
		}
	}

	public void onTickUpdate() {}
	public void updateTile() {}

	public void onServerUpdate() {

		if (this.cooldown <= 0) {
			this.cooldown = 0;

			if (TUtil.isActive(this.world, this.pos)) {
				this.extractItem();

				if (!this.suctionItem() && !this.flag) {
					this.suctionDrop();
				}
			}

		} else {
			this.cooldown--;
		}
	}

	@Nullable
	protected EnumFacing getCurrentFacing() {
		if (this.world != null && this.pos != null) {
			IBlockState state = this.world.getBlockState(this.pos);
			if (state != null && state.getBlock() instanceof THopper) {
				EnumSide side = THopper.getSide(state, THopper.SIDE);
				return side != null ? side.getFacing() : EnumFacing.DOWN;
			}
		}
		return EnumFacing.DOWN;
	}

	@Nullable
	protected EnumFacing getInsertSide() {
		if (this.world != null && this.pos != null) {
			IBlockState state = this.world.getBlockState(this.pos);
			if (state != null && state.getBlock() instanceof THopper) {
				EnumSide side = THopper.getSide(state, THopper.SIDE);
				return side == EnumSide.UP ? EnumFacing.DOWN : EnumFacing.UP;
			}
		}
		return EnumFacing.UP;
	}

	// 向きの先のチェストにアイテムを突っ込む
	protected boolean extractItem() {

		EnumFacing face = getCurrentFacing();
		if (face == null) { return false; }

		TileEntity tile = this.world.getTileEntity(this.pos.offset(face));
		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) {
			IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());

			if (target == null) { return false; }

			for (int i = 0; i < this.getSizeInventory(); i++) {

				ItemStack item = this.inv.getStackInSlot(i);
				int min = isFilterd() ? 1 : 0;

				if (TUtil.isEmpty(item)) { continue; }

				if (item.getItem().getItemStackLimit(item) == 1) {
					min = 0;
				}

				if (TUtil.getSize(item) <= min) { continue; }

				ItemStack ins = item.copy();
                ins.setCount(ins.getCount());

                for (int j = 0; j < target.getSlots(); j++) {
                    ItemStack ret = target.insertItem(j, ins, true);

                    if (!TUtil.isEmpty(ret)) { continue; }

                    target.insertItem(j, ins, false);
                    this.decrStackSize(i, ins.getCount());
                    this.markDirty();
                    tile.markDirty();
                    return true;
                }
			}
		}
		return false;
	}

	protected boolean suctionItem() {

		EnumFacing face = getCurrentFacing() == EnumFacing.UP ? EnumFacing.DOWN : EnumFacing.UP;
		TileEntity tile = this.world.getTileEntity(this.pos.offset(face));

		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
			IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

			if (target == null) { return false; }

			for (int i = 0; i < target.getSlots(); i++) {

				ItemStack item = target.extractItem(i, target.getStackInSlot(i).getCount(), true);
				if (TUtil.isEmpty(item)) { continue; }

				for (int j = 0; j < this.getSizeInventory(); j++) {
					ItemStack cur = this.getStackInSlot(j);

					if (this.isItemStackable(item, cur) <= 0) { continue; }

					this.incrStackInSlot(j, item);
					target.extractItem(i, item.getCount(), false);
					this.markDirty();
					tile.markDirty();
					return true;
				}
			}
		}
		return false;
	}

	protected void suctionDrop() {

		List<EntityItem> entityList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.add(-1, -1.5, -1), this.pos.add(2, 2, 2)));
		if (entityList.isEmpty()) { return; }

		for (EntityItem entity : entityList) {
			ItemStack stack = entity.getItem().copy();

			// 吸引アイテムを限定する
			if (TUtil.isEmpty(entity.getItem())) { continue; }

			for (int j = 0; j < this.getSizeInventory(); j++) {
				ItemStack cur = this.getStackInSlot(j);
				int count = this.isItemStackable(stack, cur);

				if (count <= 0) { continue; }

				stack.setCount(count);
				this.incrStackInSlot(j, stack);
				entity.getItem().splitStack(count);
				this.markDirty();

				if (TUtil.isEmpty(entity.getItem())) {
					entity.setDead();
				}
				return;
			}
		}
	}

	public int isItemStackable(ItemStack target, ItemStack current) {
		if (TUtil.isSameItem(target, current, true)) {
			int i = current.getCount() + target.getCount();
			if (i > current.getMaxStackSize()) {
				i = current.getMaxStackSize() - current.getCount();
				return i;
			}
			return target.getCount();
		}
		return 0;
	}

	public void incrStackInSlot(int i, ItemStack input) {
		this.inv.incrStackInSlot(i, input);
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.inv.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int num) {
		return this.inv.decrStackSize(i, num);
	}

	@Override
	public ItemStack removeStackFromSlot(int i) {
		return this.inv.removeStackFromSlot(i);
	}

	// インベントリ内のスロットにアイテムを入れる
	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		this.inv.setInventorySlotContents(i, stack);
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.getWorld().getTileEntity(this.pos) != this || player == null)
			return false;
		else
			return Math.sqrt(player.getDistanceSq(this.pos)) < 256D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}
	@Override
	public void closeInventory(EntityPlayer player) {}
	@Override
	public void setField(int id, int value) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
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
		this.inv.clear();
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
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerTHopper(this, player);
	}

	@Override
	public String getGuiID() {
		return "tamagottimod.thopper";
	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	/* === Hopper === */
	protected int[] slotsSides() {
		return new int[5];
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return this.slotsSides();
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStack, EnumFacing dir) {
		return this.isItemValidForSlot(index, itemStack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing dir) {
		if (!TUtil.isEmpty(stack)) {
			return !this.isFilterd() || stack.getCount() > 1 || stack.getItem().getItemStackLimit(stack) == 1;
		}
		return false;
	}

	IItemHandler handler = new HopperInvWrapper(this, this.getCurrentFacing());
	IItemHandler handler2 = new HopperInvWrapper(this, this.getCurrentFacing()) {

		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return stack;
		}
	};

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == getCurrentFacing() || facing == getInsertSide()) {
				return (T) this.handler2;
			} else {
				return (T) this.handler;
			}
		}
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.loadFromNbt(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		return this.saveToNbt(tag);
	}

	public void loadFromNbt(NBTTagCompound compound) {
		this.inv.readFromNBT(compound);
		this.cooldown = compound.getInteger("Cooldown");
	}

	public NBTTagCompound saveToNbt(NBTTagCompound compound) {
		compound.setInteger("Cooldown", this.cooldown);
		this.inv.writeToNBT(compound);
		return compound;
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(this.pos, -50, nbt);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void markDirty() {}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public boolean isEmpty() {
		return this.inv.isEmpty();
	}

	private class HopperInvWrapper extends SidedInvWrapper {
		private HopperInvWrapper(TileTHopper tile, EnumFacing side) {
			super(tile, side);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (amount <= 0)
				return ItemStack.EMPTY;
			int slot1 = getSlot(this.inv, slot, this.side);
			if (slot1 == -1)
				return ItemStack.EMPTY;
			ItemStack stackInSlot = this.inv.getStackInSlot(slot1);
			if (stackInSlot.isEmpty())
				return ItemStack.EMPTY;
			if (!this.inv.canExtractItem(slot1, stackInSlot, this.side))
				return ItemStack.EMPTY;
			ItemStack copy2 = stackInSlot.copy();
			int count = copy2.getCount();
			if (count > amount) {
				count = amount;
			} else {
				if (stackInSlot.getItem().getItemStackLimit(copy2) == 1) {
					count = 1;
				} else if (isFilterd()) {
					count--;
				}
			}
			if (count <= 0) {
				return ItemStack.EMPTY;
			}
			if (simulate) {
				if (stackInSlot.getCount() < count) {
					return stackInSlot.copy();
				} else {
					ItemStack copy = stackInSlot.copy();
					copy.setCount(count);
					return copy;
				}
			} else {
				int m = Math.min(stackInSlot.getCount(), count);
				ItemStack ret = this.inv.decrStackSize(slot1, m);
				this.inv.markDirty();
				return ret;
			}
		}
	}

	public boolean isFilterd() {
		return false;
	}
}
