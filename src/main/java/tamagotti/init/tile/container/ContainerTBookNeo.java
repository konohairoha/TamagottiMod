package tamagotti.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.init.items.tool.tamagotti.TLink;
import tamagotti.init.items.tool.tamagotti.TPendant;
import tamagotti.init.items.tool.tamagotti.TSail;
import tamagotti.init.items.tool.tamagotti.iitem.IChange;
import tamagotti.init.items.tool.tamagotti.iitem.ILink;
import tamagotti.init.tile.inventory.InventoryTBookNeo;
import tamagotti.init.tile.slot.SlotPredicates;
import tamagotti.init.tile.slot.ValidatedSlot;

public class ContainerTBookNeo extends Container {

	private final InventoryTBookNeo inventory;
	private final InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	private final InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	private final EntityPlayer player;
	private final int blocked;

	public ContainerTBookNeo(InventoryPlayer invPlayer, InventoryTBookNeo gemInv, EnumHand hand) {

		this.inventory = gemInv;
		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();

		this.addSlotToContainer(new SlotCrafting(invPlayer.player, this.craftMatrix, this.craftResult, 0, 131, 25));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 37 + j * 18, 7 + i * 18));

		for (int j = 0; j < 6; j++)
			for (int i = 0; i < 9; i++)
				this.addSlotToContainer(new ValidatedSlot(gemInv, i + j * 9, 11 + i * 18, 64 + j * 18, SlotPredicates.BOOKNEO));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 11 + j * 18, 176 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 11 + i * 18, 233));
		this.onCraftMatrixChanged(this.craftMatrix);

		this.blocked = hand == EnumHand.MAIN_HAND ? (this.inventorySlots.size() - 1) - (8 - invPlayer.currentItem) : -1;
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
		this.inventory.writeBack();
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex == 0) {
				stack1.getItem().onCreated(stack1, this.world, player);
				if (!this.mergeItemStack(stack1, 10, 100, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack1, stack);
			} else if (slotIndex >= 10 && slotIndex <= 64 && !this.mergeItemStack(stack1, 1, 10, false)) {
				if (!this.mergeItemStack(stack1, 1, 10, false)) {
					this.mergeItemStack(stack1, 65, 100, false);
				}
				return ItemStack.EMPTY;
			} else if (slotIndex > 0 && slotIndex < 10 && !this.mergeItemStack(stack1, 10, 100, false)) {
				return ItemStack.EMPTY;
			} else if (slotIndex >= 10 && slotIndex <= 64 && !this.mergeItemStack(stack1, 65, 100, false)) {
				return ItemStack.EMPTY;
			} else if (slotIndex >= 65 && !this.mergeItemStack(stack1, 10, 64, false)) {
				if (!this.mergeItemStack(stack1, 10, 64, false)) {
					this.mergeItemStack(stack1, 1, 10, false);
				}
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

	@Override
	public ItemStack slotClick(int slot, int button, ClickType flag, EntityPlayer player) {
		if (slot >= 0 && slot == this.blocked && player.getHeldItemMainhand().getItem() == ItemInit.tamagotti_book_neo) {
			return ItemStack.EMPTY;
		}

		if (flag == ClickType.CLONE) {
			Slot s = this.inventorySlots.get(slot);
			ItemStack stack = s.getStack();
			Item item = s.getStack().getItem();

			if (item instanceof IChange || item instanceof ILink) {
				if (item == ItemInit.y_pendant) {
					TPendant pe = (TPendant) item;
					pe.changeMode(player, stack);
				} else if (item instanceof TSail) {
					TSail pe = (TSail) item;
					pe.changeMode(player, stack);
				} else if (item == ItemInit.tamagottilink) {
					TLink pe = (TLink) item;
					pe.teleportPlayer(stack.getTagCompound(), player);
				}
			}
		}
		return super.slotClick(slot, button, flag, player);
	}
}
