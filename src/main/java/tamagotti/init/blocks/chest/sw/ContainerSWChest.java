package tamagotti.init.blocks.chest.sw;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import tamagotti.init.blocks.chest.base.BaseContainerChest;
import tamagotti.init.blocks.chest.tchest.ChestContainer;
import tamagotti.init.tile.TileBaseChest;
import tamagotti.init.tile.slot.SlotItem;

@ChestContainer(isLargeChest = true, rowSize = 15)
public class ContainerSWChest extends BaseContainerChest {

	public ContainerSWChest(EntityPlayer player, TileBaseChest tile) {
		super(player, tile);
	}

	// チェストのスロット
	@Override
	public void chestSlot(TileBaseChest tile) {

		for (int k = 0; k < 10; ++k)
			for (int j = 0; j < 15; ++j)
				this.addSlotToContainer(new SlotItem(tile, j + k * 15, 7 + j * 16, 2 + k * 17));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 48 + j * 17, 175 + i * 17));

		for (int l = 0; l < 9; ++l)
			this.addSlotToContainer(new Slot(playerInv, l, 48 + l * 17, 230));
	}
}
