package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.Item;
import tamagotti.init.ItemInit;

public class TFoodItem extends Item {

	public TFoodItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.foodList.add(this);
    }
}
