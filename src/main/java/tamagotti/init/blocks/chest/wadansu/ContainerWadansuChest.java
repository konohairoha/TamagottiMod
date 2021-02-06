package tamagotti.init.blocks.chest.wadansu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import tamagotti.init.blocks.chest.base.BaseContainerChest;
import tamagotti.init.blocks.chest.tchest.ChestContainer;
import tamagotti.init.tile.TileBaseChest;
import tamagotti.init.tile.slot.SlotItem;

@ChestContainer(isLargeChest = true, rowSize = 9)
public class ContainerWadansuChest extends BaseContainerChest {

	public ContainerWadansuChest(EntityPlayer player, TileBaseChest tile) {
		super(player, tile);
	}

	// チェストのスロット
	@Override
	public void chestSlot(TileBaseChest tile) {

		for (int k = 0; k < 6; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotItem(tile, j + k * 9, 8 + j * 18, 18 + k * 18));
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(playerInv, l, 8 + l * 18, 198));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		player.world.playSound(null, new BlockPos(player), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
