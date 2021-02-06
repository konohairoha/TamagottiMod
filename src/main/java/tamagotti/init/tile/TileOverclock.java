package tamagotti.init.tile;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import tamagotti.init.blocks.chest.thopper.TileTHopper;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class TileOverclock extends TileTBase {

	@Override
	public void update(){
		if (TUtil.isActive(this.world, this.pos)) {
			this.speedUpTileEntities(this.world, 8, new AxisAlignedBB(pos.add(-5, -5, -5), pos.add(5, 5, 5)));
			return;
		}

		// tileがRSで止まっているとき
		this.notActive();
    }

	private void speedUpTileEntities(World world, int bonusTicks, AxisAlignedBB bBox) {
		List<TileEntity> list = WorldHelper.getTileEntitiesWithinAABB(world, bBox);
		for (TileEntity tile : list) {
			if (!tile.isInvalid() && tile instanceof ITickable &&
			(tile instanceof TileBaseFurnace || tile instanceof TileTHopper || tile instanceof TileTSpawner)) {
				for (int i = 0; i < bonusTicks; i++) {
					((ITickable) tile).update();
				}
			}
		}
	}
}