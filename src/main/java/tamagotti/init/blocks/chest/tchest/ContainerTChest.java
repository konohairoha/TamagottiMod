package tamagotti.init.blocks.chest.tchest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.init.BlockInit;
import tamagotti.init.blocks.chest.base.BaseContainerChest;
import tamagotti.init.tile.TileBaseChest;
import tamagotti.init.tile.slot.SlotItem;

@ChestContainer(isLargeChest = true, rowSize = 13)
public class ContainerTChest extends BaseContainerChest {

	public ContainerTChest(EntityPlayer player, TileBaseChest tile) {
		super(player, tile);
	}

	// チェストのスロット
	@Override
	public void chestSlot(TileBaseChest tileEntity) {

		for (int k = 0; k < 8; ++k){
			for (int j = 0; j < 13; ++j){
				this.addSlotToContainer(new SlotItem(tileEntity, j + k * 13, 12 + j * 18, 5 + k * 18));
			}
		}

		for (int i = 0; i < 3; ++i){
			for (int j = 0; j < 9; ++j){
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 48 + j * 18, 152 + i * 18));
			}
		}

		for (int l = 0; l < 9; ++l){
			this.addSlotToContainer(new Slot(playerInv, l, 48 + l * 18, 210));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		World world = player.world;
		if (this.tile.isBlock(BlockInit.tamagottichest)) {
			world.playSound(null, new BlockPos(player), SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}
}
