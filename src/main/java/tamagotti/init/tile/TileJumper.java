package tamagotti.init.tile;

import net.minecraft.util.math.AxisAlignedBB;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class TileJumper extends TileTBase {

	@Override
	public void update(){

		if (TUtil.isActive(this.world, this.pos)) {
			if(!this.world.isRemote) {
				WorldHelper.upEntity(this.world, new AxisAlignedBB(this.pos.add(0, 0, 0), this.pos.add(1, 48, 1)), this.pos);
			}
		} else {

			// tileがRSで止まっているとき
			this.notActive();
		}
    }
}
