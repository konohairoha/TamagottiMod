package tamagotti.init.tile;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import tamagotti.util.TUtil;

public class TileTBase extends TileEntity implements ITickable {

	public int tickTime = 0;
	public int particleTime = 0;
	public Random rand = new Random();

	@Override
	public void update() {
		this.tickTime++;
	}

	// tileがRSで止まっているとき
	public void notActive () {
		this.particleTime++;
		if(this.particleTime >= 100) {
			this.particleTime = 0;
        	TUtil.spawnParticles(this.world, this.pos);
		}
	}
}
