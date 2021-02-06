package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tamagotti.util.TDamage;

public class EntityShotter extends EntityRitter {

	public EntityShotter(World worldIn) {
		super(worldIn);
	}

	public EntityShotter(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityShotter(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	// 水中での速度減衰
	@Override
	protected float inWaterSpeed() {
		return 0F;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir
	@Override
	protected int plusTickAir() {
		return -25;
	}

	@Override
	protected DamageSource damageSource() {
		return TDamage.ShotterDamage(this, this.thrower);
	}
}