package tamagotti.init.blocks.chest.dchest;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.tile.slot.SlotItem;

public class ContainerDChest extends Container{

	public final TileDChest tile;
	public final InventoryPlayer playerInv;

	public ContainerDChest(EntityPlayer player, TileDChest tileEntity){
		this.tile = tileEntity;
		this.playerInv = player.inventory;
		tileEntity.openInventory(player);
		for (int k = 0; k < 8; ++k){
			for (int j = 0; j < 13; ++j){
				this.addSlotToContainer(new SlotItem(tileEntity, j + k * 13, 12 + j * 18, 5 + k * 18));
			}
		}

		for (int i = 0; i < 3; ++i){
			for (int j = 0; j < 9; ++j){
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 48 + j * 18, 152 + i * 18));
			}
		}

		for (int l = 0; l < 9; ++l){
			this.addSlotToContainer(new Slot(playerInv, l, 48 + l * 18, 210));
		}
	}

	@Override
	public void addListener(IContainerListener listener){
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile);
	}

	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data){
		this.tile.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(par2);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (par2 < 104 && !this.mergeItemStack(itemstack1, 104, 140, false)){
				return ItemStack.EMPTY;
			}if (par2 >= 104 && !this.mergeItemStack(itemstack1, 0, 104, false)){
				return ItemStack.EMPTY;
			}if (itemstack1.isEmpty()){
				slot.putStack(ItemStack.EMPTY);
			}else{
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}
