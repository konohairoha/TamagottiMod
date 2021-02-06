package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.Item;
import tamagotti.init.ItemInit;

public class TItem extends Item {

	public TItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
    }
}