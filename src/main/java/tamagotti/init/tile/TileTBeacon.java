package tamagotti.init.tile;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tamagotti.util.TUtil;
import tamagotti.util.WorldHelper;

public class TileTBeacon extends TileTBase {

	@Override
	public void update() {

		if (!TUtil.isActive(this.world, this.pos)) { return; }

		super.update();
		if(this.tickTime % 100 != 0) { return; }

		this.spawnParticles(this.world, this.pos);
		//与える効果の開始位置と終了位置
		WorldHelper.PlayerPoint(this.world, new AxisAlignedBB(this.pos.add(-8, -8, -8), this.pos.add(8, 8, 8)));
		this.tickTime = 0;
    }

	private void spawnParticles(World world, BlockPos pos) {
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		for (int i = 0; i < 6; ++i) {
            double d1 = x + this.rand.nextFloat();
            double d2 = y + this.rand.nextFloat();
            double d3 = z + this.rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.HEART, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }
}