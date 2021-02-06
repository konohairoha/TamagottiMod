package tamagotti.init.items.tool.tamagotti.iitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMode {

	void changeMode (ItemStack stack, EntityPlayer player);
}
