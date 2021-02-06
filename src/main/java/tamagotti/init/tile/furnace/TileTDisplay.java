package tamagotti.init.tile.furnace;

import tamagotti.init.tile.TileBaseFurnace;

public class TileTDisplay extends TileBaseFurnace {

	public TileTDisplay() {
		this.ticksBeforeSmelt = 1;
		this.maxBurnTime = 50000;
	}

	// インベントリの数
	@Override
	protected int getInvSize() {
		return 9;
	}

	@Override
	public void spwanPatickle() {}
}
