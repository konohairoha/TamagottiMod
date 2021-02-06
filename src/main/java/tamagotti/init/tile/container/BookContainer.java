package tamagotti.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BookContainer extends Container {

	private final InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	private final InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	private final EntityPlayer player;

	public BookContainer(InventoryPlayer invPlayer) {

		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, this.craftMatrix, this.craftResult, 0, 124, 35));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, this.craftResult);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!this.world.isRemote) {
			this.clearContainer(player, this.world, this.craftMatrix);
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

            if (slotIndex == 0) {
                stack1.getItem().onCreated(stack1, this.world, player);
                if (!this.mergeItemStack(stack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            } else if (slotIndex > 0 && slotIndex < 10 && !this.mergeItemStack(stack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}  else if (slotIndex >= 10 && !this.mergeItemStack(stack1, 1, 10, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            ItemStack stack2 = slot.onTake(player, stack1);
            if (slotIndex == 0) {
                player.dropItem(stack2, false);
            }
		}
		return stack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
	}
}
