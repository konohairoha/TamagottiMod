package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.ItemPickaxe;
import tamagotti.init.ItemInit;

public class TPick extends ItemPickaxe {

	public TPick(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
	}
}