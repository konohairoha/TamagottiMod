package tamagotti.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tamagotti.init.tile.inventory.InventoryTPack;
import tamagotti.init.tile.slot.SlotPredicates;
import tamagotti.init.tile.slot.ValidatedSlot;

public class ContainerTPack extends Container {

	public final InventoryTPack inventory;

	public ContainerTPack(InventoryPlayer invPlayer, InventoryTPack gemInv) {

		this.inventory = gemInv;

		for (int i = 0; i < 4; i++)
				this.addSlotToContainer(new ValidatedSlot(gemInv, i, 13 + i * 44, 27, SlotPredicates.TLINK));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 7 + j * 18, 84 + i * 18));

		for (int i = 0; i < 9; ++i)
			this.addSlotToContainer(new Slot(invPlayer, i, 7 + i * 18, 142));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex < 4 && !this.mergeItemStack(itemstack1, 4, 40, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= 4 && !this.mergeItemStack(itemstack1, 0, 4, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return true;
	}
}
