package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityTCross extends EntityRitter {

	public EntityTCross(World worldIn) {
		super(worldIn);
	}

	public EntityTCross(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityTCross(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	// 水中での速度減衰
	@Override
	protected float inWaterSpeed() {
		return 1F;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir
	@Override
	protected int plusTickAir() {
		return 10;
	}
}