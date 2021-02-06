package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.ItemStack;

public class TFuel extends TItem {

	private final int time;

	public TFuel(String name, int fuel) {
		super(name);
		this.time = fuel;
    }

	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.time;
	}
}