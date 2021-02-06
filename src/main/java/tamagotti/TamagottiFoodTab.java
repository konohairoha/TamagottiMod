package tamagotti;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;

public class TamagottiFoodTab extends CreativeTabs {

	public TamagottiFoodTab(String type) {
		super(type);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ItemInit.tamagottirice);
	}
}