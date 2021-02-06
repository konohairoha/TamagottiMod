package tamagotti.init.blocks.chest.base;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.TileBaseChest;
import tamagotti.init.tile.slot.SlotItem;

public class BaseContainerChest extends Container {

	public final TileBaseChest tile;
	public final InventoryPlayer playerInv;

	public BaseContainerChest(EntityPlayer player, TileBaseChest tileEntity) {
		this.tile = tileEntity;
		this.playerInv = player.inventory;
		tileEntity.openInventory(player);

		// チェストのスロット
		this.chestSlot(tileEntity);

	}

	// チェストのスロット
	public void chestSlot(TileBaseChest tileEntity) {

		for (int k = 0; k < 6; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotItem(tileEntity, j + k * 9, 8 + j * 18, 18 + k * 18));
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.playerInv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(this.playerInv, l, 8 + l * 18, 198));
		}
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tile.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);
		int invSize = this.tile.getSizeInventory();


		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < invSize && !this.mergeItemStack(stack1, invSize, 36 + invSize, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= invSize && !this.mergeItemStack(stack1, 0, invSize, false)) {
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
}