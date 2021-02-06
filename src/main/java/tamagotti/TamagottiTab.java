package tamagotti;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import tamagotti.init.ItemInit;

public class TamagottiTab extends CreativeTabs {

	public TamagottiTab(String type) {
		super(type);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ItemInit.tamagotti);
	}
}