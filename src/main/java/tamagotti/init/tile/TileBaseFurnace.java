package tamagotti.init.tile;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import tamagotti.init.blocks.furnace.Pot;
import tamagotti.init.tile.furnace.WrappedItemHandler;
import tamagotti.init.tile.slot.SlotPredicates;
import tamagotti.util.ItemHelper;

public class TileBaseFurnace extends TileEntity implements ITickable {

	public int currentItemBurnTime;
	public int furnaceCookTime;
	public int burnTime;
	public int tickTime = 0;
	public int maxBurnTime;

	protected final ItemStackHandler inputInv = new StackHandler(this.getInvSize());
	protected final ItemStackHandler outputInv = new StackHandler(this.getInvSize());
	private final ItemStackHandler fuelInv = new StackHandler(1);

	private final IItemHandlerModifiable autoInput = new WrappedItemHandler(this.inputInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.SMELTABLE.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final IItemHandlerModifiable autoFuel = new WrappedItemHandler(this.fuelInv, WrappedItemHandler.WriteMode.IN) {
		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.FURNACE_FUEL.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final IItemHandlerModifiable autoOutput = new WrappedItemHandler(this.outputInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide = new CombinedInvWrapper(this.autoFuel, this.autoOutput);
	private final CombinedInvWrapper joined = new CombinedInvWrapper(this.autoInput, this.autoFuel, this.autoOutput);
	protected int ticksBeforeSmelt;
	private int furnaceBurnTime;

	public TileBaseFurnace() {
		this.ticksBeforeSmelt = 100;
		this.maxBurnTime = 10000;
	}

	public TileBaseFurnace(int ticksBeforeSmelt, int maxBurnTime) {
		this.ticksBeforeSmelt = ticksBeforeSmelt;
		this.maxBurnTime = maxBurnTime;
	}

	// インベントリの数
	protected int getInvSize() {
		return 1;
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	private ItemStack getFuelItem() {
		return this.fuelInv.getStackInSlot(0);
	}

	public IItemHandler getInput() {
		return this.inputInv;
	}

	public IItemHandler getOutput() {
		return this.outputInv;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {

		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.joined);
			} else {
				switch (side) {
				case UP:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoInput);
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoOutput);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoSide);
				}
			}
		}

		return super.getCapability(cap, side);
	}

    public int getEnergyUsage() {
        int usage = 1;
        return usage;
    }

	@Override
	public void update() {

//		if (this.burnTime < this.maxBurnTime && this.energy.getStored() > 0) {
//
//	        if (this.energy.extract(getEnergyUsage(), Action.SIMULATE) >= 0) {
//	            this.energy.extract(getEnergyUsage(), Action.PERFORM);
//				this.burnTime++;
//				this.furnaceBurnTime++;
//	        } else {
//	            this.energy.setStored(0);
//	        }
//		}


		boolean flag = this.furnaceBurnTime > 0;
		this.pullFromInventories();
		ItemHelper.compactInventory(this.inputInv);

		// MFが最大量じゃなかったら燃料をひたすら減らす
		if (this.furnaceBurnTime < this.maxBurnTime && this.canSmelt()) {

			this.currentItemBurnTime += this.getItemBurnTime(this.getFuelItem());
			this.furnaceBurnTime += this.getItemBurnTime(this.getFuelItem());
			this.burnTime = this.furnaceBurnTime;

			if (this.furnaceBurnTime > 0) {
				this.markDirty();

				if (!this.getFuelItem().isEmpty()) {
					ItemStack copy = this.getFuelItem().copy();

					this.getFuelItem().shrink(1);

					if (this.getFuelItem().isEmpty()) {
						this.fuelInv.setStackInSlot(0, copy.getItem().getContainerItem(copy));
					}
				}
			}
		}

		// 精錬出来るアイテムがあると精錬
		if (this.furnaceBurnTime > 0 && canSmelt()) {

			this.furnaceCookTime++;
			this.furnaceBurnTime--;
			this.burnTime = this.furnaceBurnTime;

			if (this.furnaceCookTime == this.ticksBeforeSmelt) {
				this.furnaceCookTime = 0;
				this.smeltItem();
			}
		}

		if (flag != this.isBurning()) {
			this.updateState();
		}

		if (this.isBurning()) {

			this.tickTime++;

			if (this.tickTime % 5 == 0) {
				this.tickTime = 0;
				this.spwanPatickle();
			}
		}

		ItemHelper.compactInventory(this.outputInv);
	}

	// ブロック置換処理
	public void updateState () {
		Pot pot = (Pot) world.getBlockState(this.pos).getBlock();
		pot.setState(this.isBurning(), this.world, this.pos);
	}

	public boolean isItemFuel(ItemStack stack) {
		return this.getItemBurnTime(stack) > 0;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	private void pullFromInventories() {
		TileEntity tile = this.world.getTileEntity(pos.up());
		if (tile == null || tile instanceof TileEntityHopper || tile instanceof TileEntityDropper) {
			return;
		}
		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		if (handler == null) {
			if (tile instanceof ISidedInventory) {
				handler = new SidedInvWrapper((ISidedInventory) tile, EnumFacing.DOWN);
			} else if (tile instanceof IInventory) {
				handler = new InvWrapper((IInventory) tile);
			} else {
				return;
			}
		}

		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack extractTest = handler.extractItem(i, Integer.MAX_VALUE, true);
			if (extractTest.isEmpty()) { continue; }

			IItemHandler targetInv = TileEntityFurnace.isItemFuel(extractTest) ? this.fuelInv : this.inputInv;

			ItemStack remainderTest = ItemHandlerHelper.insertItemStacked(targetInv, extractTest, true);
			int successfullyTransferred = extractTest.getCount() - remainderTest.getCount();

			if (successfullyTransferred > 0) {
				ItemStack toInsert = handler.extractItem(i, successfullyTransferred, false);
				ItemStack result = ItemHandlerHelper.insertItemStacked(targetInv, toInsert, false);
				assert result.isEmpty();
			}
		}
	}

	protected void smeltItem() {
		ItemStack toSmelt = inputInv.getStackInSlot(0);
		ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(toSmelt).copy();
		ItemHandlerHelper.insertItemStacked(this.outputInv, smeltResult, false);
		toSmelt.shrink(1);
	}

	public boolean canSmelt() {

		ItemStack toSmelt = this.inputInv.getStackInSlot(0);
		if (toSmelt.isEmpty()) { return false; }

		ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(toSmelt);
		if (smeltResult.isEmpty()) { return false; }

		ItemStack currentSmelted = this.outputInv.getStackInSlot(this.outputInv.getSlots() - 1);

		if (currentSmelted.isEmpty()) { return true; }
		if (!smeltResult.isItemEqual(currentSmelted)) { return false; }

		int result = currentSmelted.getCount() + smeltResult.getCount();
		return result <= currentSmelted.getMaxStackSize();
	}

	public int getCookProgressScaled(int value) {
		return (this.furnaceCookTime + (isBurning() && canSmelt() ? 1 : 0)) * value / this.ticksBeforeSmelt;
	}

	public int getBurnTimeRemainingScaled(int value) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = this.ticksBeforeSmelt;
		}
		return this.furnaceBurnTime * value / this.currentItemBurnTime;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.furnaceBurnTime = nbt.getInteger("BurnTime");
		this.burnTime = nbt.getInteger("smeltBurnTime");
		this.furnaceCookTime = nbt.getShort("CookTime");
		this.inputInv.deserializeNBT(nbt.getCompoundTag("Input"));
		this.outputInv.deserializeNBT(nbt.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(nbt.getCompoundTag("Fuel"));
		this.currentItemBurnTime = this.getItemBurnTime(this.getFuelItem());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("BurnTime", this.furnaceBurnTime);
		nbt.setInteger("smeltBurnTime", this.burnTime);
		nbt.setShort("CookTime", (short) this.furnaceCookTime);
		nbt.setTag("Input", this.inputInv.serializeNBT());
		nbt.setTag("Output", this.outputInv.serializeNBT());
		nbt.setTag("Fuel", this.fuelInv.serializeNBT());
		return nbt;
	}

	public void spwanPatickle() {
		if (this.world.isRemote) { return; }

		Random rand = this.world.rand;
		float x = this.pos.getX() + 0.1F;
		float y = this.pos.getY() + 0.75F;
		float z = this.pos.getZ() + 0.1F;

		for (int i = 0; i < 4; i++) {
			float f1 = x + rand.nextFloat() * 0.8F;
			float f2 = y + rand.nextFloat() * 6.0F / 16.0F;
			float f3 = z + rand.nextFloat() * 0.8F;
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f1, f2, f3, 0.0D, 0.0D, 0.0D);
		}
	}

	class StackHandler extends ItemStackHandler {
		StackHandler(int size) {
			super(size);
		}

		@Override
		public void onContentsChanged(int slot) {
			TileBaseFurnace.this.markDirty();
		}
	}

	/*
	 * =========================================================
	 */

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	protected int getItemBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, -50, nbt);
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
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
	}
}
