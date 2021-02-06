package tamagotti.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import tamagotti.init.blocks.furnace.DFurnace;
import tamagotti.init.tile.furnace.TileDFurnace;
import tamagotti.init.tile.slot.SlotPredicates;
import tamagotti.init.tile.slot.ValidatedSlot;

public class ContainerDFurnace extends Container {

	final TileDFurnace tile;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerDFurnace (InventoryPlayer invPlayer, TileDFurnace tile) {
		this.tile = tile;
		initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		IItemHandler fuel = tile.getFuel();
		IItemHandler input = tile.getInput();
		IItemHandler output = tile.getOutput();

		//Fuel
		this.addSlotToContainer(new ValidatedSlot(fuel, 0, 65, 98, SlotPredicates.FURNACE_FUEL));

		//Input(0)
		this.addSlotToContainer(new ValidatedSlot(input, 0, 65, 62, SlotPredicates.SMELTABLE));

		int counter = input.getSlots() - 1;

		//Input storage
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 10; j++)
				this.addSlotToContainer(new ValidatedSlot(input, counter--, 15 + j * 18, 6 + i * 18, SlotPredicates.SMELTABLE));

		counter = output.getSlots() - 1;

		//Output(0)
		this.addSlotToContainer(new ValidatedSlot(output, counter--, 125, 80, s -> false));

		//Output Storage
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 10; j++)
				this.addSlotToContainer(new ValidatedSlot(output, counter--, 15 + j * 18, 118 + i * 18, s -> false));

		//Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 24 + j * 18, 176 + i * 18));

		//Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 24 + i * 18, 233));
	}


	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return (player.world.getBlockState(tile.getPos()).getBlock() instanceof DFurnace)
				&& player.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5,
						tile.getPos().getZ() + 0.5) <= 64.0;
	}

	@Override
	public void addListener(IContainerListener container) {
		super.addListener(container);
		container.sendWindowProperty(this, 0, tile.furnaceCookTime);
		container.sendWindowProperty(this, 1, tile.burnTime);
		container.sendWindowProperty(this, 2, tile.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (IContainerListener crafter : this.listeners) {
			if (lastCookTime != tile.furnaceCookTime)
				crafter.sendWindowProperty(this, 0, tile.furnaceCookTime);

			if (lastBurnTime != tile.burnTime)
				crafter.sendWindowProperty(this, 1, tile.burnTime);

			if (lastItemBurnTime != tile.currentItemBurnTime)
				crafter.sendWindowProperty(this, 2, tile.currentItemBurnTime);
		}

		lastCookTime = tile.furnaceCookTime;
		lastBurnTime = tile.burnTime;
		lastItemBurnTime = tile.currentItemBurnTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			tile.furnaceCookTime = par2;

		if (par1 == 1)
			tile.burnTime = par2;

		if (par1 == 2)
			tile.currentItemBurnTime = par2;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		Slot slot = this.getSlot(slotIndex);

		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();
		ItemStack newStack = stack.copy();

		if (slotIndex <= 62) {
			if (!this.mergeItemStack(stack, 63, 99, false)) {
				return ItemStack.EMPTY;
			}
		} else {

			if (TileEntityFurnace.isItemFuel(newStack)) {
				if (!this.mergeItemStack(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!FurnaceRecipes.instance().getSmeltingResult(newStack).isEmpty()) {
				if (!this.mergeItemStack(stack, 1, 42, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (stack.isEmpty()) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}

		return newStack;
	}
}
