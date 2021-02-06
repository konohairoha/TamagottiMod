package tamagotti.init.blocks.chest.thopper;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import tamagotti.util.TUtil;

public class TileTHopperN extends TileTHopper{

	@Override
	protected void suctionDrop() {

		List<EntityItem> entityList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(0, -0.5, 0), pos.add(0, 0.5, 0)));
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
