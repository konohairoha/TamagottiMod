package tamagotti.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainarMFRifle extends Container {

	private final InventorySMWand inventory;
	private final World world;
	private final EntityPlayer player;
	private final int slot;

	public ContainarMFRifle(InventoryPlayer invPlayer, InventorySMWand gemInv) {

		this.inventory = gemInv;
		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();
		this.slot = this.inventory.getSlots();
		SlotPredicates.stack = player.getHeldItemMainhand();

		switch (this.slot) {
		case 2:
			this.tier1Slot(gemInv);
			break;
		case 4:
			this.tier2Slot(gemInv);
			break;
		case 8:
			this.tier3Slot(gemInv);
			break;
		}

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 16 + j * 18, 112 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 16 + i * 18, 170));

	}

	// tier1
	public void tier1Slot (InventorySMWand gemInv) {
		for (int j = 0; j < this.slot; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 60, 60, SlotPredicates.ISHOTER));
	}

	// tier2
	public void tier2Slot (InventorySMWand gemInv) {

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 50, 40, SlotPredicates.ISHOTER));

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 2, 60 + j * 50, 80, SlotPredicates.ISHOTER));
	}

	// tier3
	public void tier3Slot (InventorySMWand gemInv) {

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 30, 30, SlotPredicates.ISHOTER));

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 3, 60 + j * 60, 60, SlotPredicates.ISHOTER));

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 5, 60 + j * 30, 90, SlotPredicates.ISHOTER));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		int slotCount = this.slot;

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= slotCount && !this.mergeItemStack(stack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return stack;
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
