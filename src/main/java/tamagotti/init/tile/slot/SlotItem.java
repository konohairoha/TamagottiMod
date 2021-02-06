package tamagotti.init.tile.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotItem extends Slot {

	public SlotItem(IInventory inven, int index, int xpos, int ypos) {
		super(inven, index, xpos, ypos);
	}
}