package tamagotti.init.items.tool.tamagotti;

import net.minecraft.item.ItemSpade;
import tamagotti.init.ItemInit;

public class TShovel extends ItemSpade {

	public TShovel(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
	}
}