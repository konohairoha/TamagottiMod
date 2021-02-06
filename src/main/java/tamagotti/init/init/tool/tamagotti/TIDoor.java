package tamagotti.init.items.tool.tamagotti;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import tamagotti.init.ItemInit;

public class TIDoor extends ItemDoor {

	public TIDoor(String name, Block block) {
		super(block);
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
    }
}