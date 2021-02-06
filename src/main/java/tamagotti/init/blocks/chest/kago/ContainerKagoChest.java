package tamagotti.init.blocks.chest.kago;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import tamagotti.init.blocks.chest.base.BaseContainerChest;
import tamagotti.init.blocks.chest.tchest.ChestContainer;
import tamagotti.init.tile.TileBaseChest;
import tamagotti.init.tile.slot.SlotItem;

@ChestContainer(isLargeChest = true, rowSize = 9)
public class ContainerKagoChest extends BaseContainerChest {

	public ContainerKagoChest(EntityPlayer player, TileKagoChest tileEntity) {
		super(player, tileEntity);
    }

	// チェストのスロット
	@Override
	public void chestSlot(TileBaseChest tileEntity) {
		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 9; ++l) {
				this.addSlotToContainer(new SlotItem(tileEntity, l + k * 9, 8 + l * 18, 18 + k * 18));
			}
		}

		for (int i1 = 0; i1 < 3; ++i1) {
			for (int k1 = 0; k1 < 9; ++k1) {
				this.addSlotToContainer(new Slot(playerInv, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
			}
		}

		for (int j1 = 0; j1 < 9; ++j1) {
			this.addSlotToContainer(new Slot(playerInv, j1, 8 + j1 * 18, 142));
		}
	}
}
