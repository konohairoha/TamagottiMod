package tamagotti.init.blocks.chest.dchest;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import tamagotti.init.blocks.chest.ychest.TileYChest;
import tamagotti.init.blocks.furnace.DFurnace;
import tamagotti.util.TUtil;

public class TileDChest extends TileYChest {

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerDChest(player, this);
	}

	@Override
	public void updateTile() {

		if (!this.world.isRemote && this.numPlayersUsing != 0) {
			this.numPlayersUsing = 0;
			for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-5, -5, -5), pos.add(5, 5, 5)))) {

				if (!(player.openContainer instanceof ContainerDChest)) { continue; }

				IInventory iinventory = ((ContainerDChest) player.openContainer).tile;
				if (iinventory == this) {
					++this.numPlayersUsing;
				}
			}
		}
	}

	@Override
	protected void suctionDrop() {

		if (this.world.getBlockState(this.pos.down()).getBlock() instanceof DFurnace) { return; }
		List<EntityItem> entityList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.add(-11.5, -0.5, -11.5), this.pos.add(13, 13, 13)));
		if (entityList.isEmpty()) { return; }

		for (EntityItem entity : entityList) {

			if (TUtil.isEmpty(entity.getItem())) { continue; }

			ItemStack ins = entity.getItem().copy();
			for (int j = 0; j < this.getSizeInventory(); j++) {

				ItemStack cur = this.getStackInSlot(j);
				int count = this.isItemStackable(ins, cur);

				if (count <= 0) { continue; }

				ins.setCount(count);
				this.incrStackInSlot(j, ins);
				entity.getItem().splitStack(count);
				this.markDirty();
				if (TUtil.isEmpty(entity.getItem())) {
					entity.setDead();
				}
				return;
			}
		}
	}
}
