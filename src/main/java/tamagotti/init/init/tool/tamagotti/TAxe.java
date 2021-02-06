package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.ItemAxe;
import tamagotti.init.ItemInit;

public class TAxe extends ItemAxe {

	public TAxe(String name, ToolMaterial material, float damage, float speed) {
		super(material,damage,speed);
		setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
	}
}