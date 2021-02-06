package tamagotti.init.items.tool.ore.smoky;

import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;
import tamagotti.init.base.BaseRangeBreak;

public class SmokyPick extends BaseRangeBreak {

	private final int cycle;

	public SmokyPick(String name, ToolMaterial material, int size) {
		super(name, material, size);
		this.cycle = size;
	}

	//特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if (this.cycle == 1) {
			return repair.getItem() == ItemInit.fluorite ? true : super.getIsRepairable(toRepair, repair);
		} else {
			return repair.getItem() == ItemInit.smokyquartz ? true : super.getIsRepairable(toRepair, repair);
		}
	}
}