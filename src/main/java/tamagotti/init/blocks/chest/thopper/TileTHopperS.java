package tamagotti.init.blocks.chest.thopper;

import tamagotti.util.TUtil;

public class TileTHopperS extends TileTHopperN {

	@Override
	public void onServerUpdate() {

		if (this.cooldown <= 0) {

			this.cooldown = 3;	//クールタイムの発生

			if (TUtil.isActive(this.world, this.pos)) {

				this.extractItem();

				if (!this.suctionItem()) {
					this.suctionDrop();
				}
			}

		} else {
			this.cooldown--;
		}
	}
}