package tamagotti.init.blocks.chest.thopper;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import tamagotti.util.TUtil;

public class TileTHopperF extends TileTHopperN {

	@Override
	public boolean isFilterd() {
		return true;
	}

	@Override
	public boolean extractItem() {

		EnumFacing face = this.getCurrentFacing();
		if (face == null) { return false; }

		TileEntity tile = this.world.getTileEntity(this.pos.offset(face));
		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) {

			IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
			if (target == null) { return false; }

			for (int i = 0; i < this.getSizeInventory(); i++) {

				ItemStack item = inv.getStackInSlot(i);
				int min = isFilterd() ? 1 : 0;

				if (!TUtil.isEmpty(item)) { continue; }

				if (item.getItem().getItemStackLimit(item) == 1) {
					min = 0;
				}

				if (TUtil.getSize(item) <= min) { continue; }

				ItemStack ins = item.copy();
				ins.setCount(ins.getCount() - 1);
				for (int j = 0; j < target.getSlots(); j++) {

					ItemStack ret = target.insertItem(j, ins, true);
					if (TUtil.isEmpty(ret)) { continue; }

					target.insertItem(j, ins, false);
					this.decrStackSize(i, ins.getCount());
					this.markDirty();
					tile.markDirty();
					return true;
				}
			}
		}
		return false;
	}
}
