package tamagotti.init.items.tool.extend;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.init.items.tool.tamagotti.TItem;

public class ExItem extends TItem {

	public ExItem(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}