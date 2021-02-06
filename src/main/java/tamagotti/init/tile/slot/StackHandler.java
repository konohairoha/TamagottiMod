package tamagotti.init.tile.slot;

import net.minecraftforge.items.ItemStackHandler;
import tamagotti.init.tile.TileTBase;

public class StackHandler extends ItemStackHandler {

	TileTBase tile;

	public StackHandler (TileTBase tile, int size) {
		super(size);
		this.tile = tile;
	}

	@Override
	public void onContentsChanged(int slot) {
		this.tile.markDirty();
	}
}