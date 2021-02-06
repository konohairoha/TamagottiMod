package tamagotti.init.tile.furnace;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WrappedItemHandler implements IItemHandlerModifiable {

	private final IItemHandlerModifiable compose;
	private final WriteMode mode;

	public WrappedItemHandler(IItemHandlerModifiable compose, WriteMode mode) {
		this.compose = compose;
		this.mode = mode;
	}

	@Override
	public int getSlots() {
		return compose.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return compose.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (mode == WriteMode.IN || mode == WriteMode.IN_OUT)
			return compose.insertItem(slot, stack, simulate);
		else
			return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (mode == WriteMode.OUT || mode == WriteMode.IN_OUT)
			return compose.extractItem(slot, amount, simulate);
		else
			return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return compose.getSlotLimit(slot);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		compose.setStackInSlot(slot, stack);
	}

	public enum WriteMode {
		IN, OUT, IN_OUT, NONE
	}
}
