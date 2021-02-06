package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import tamagotti.util.TDamage;

public class EntityBlaster extends EntityRitter {

	protected int tickTime = 0;
	public float exexplosion;

	public EntityBlaster(World worldIn) {
		super(worldIn);
	}

	public EntityBlaster(World worldIn, EntityLivingBase throwerIn, float ex) {
		super(worldIn, throwerIn);
		this.exexplosion = ex;
	}

	public EntityBlaster(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void onUpdate() {
		this.tickTime++;
		if (this.tickTime > 0) {

			if (this.world.isRemote && !inGround) {
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
			}

			if (this.tickTime >= 7 + (this.exexplosion)) {
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.exexplosion, false);
				this.setDead();
			}
		}
		super.onUpdate();
	}

	//エンティティに当たると爆発
	protected void arrowHit(EntityLivingBase living) {
		this.world.createExplosion(null, this.posX, this.posY, this.posZ, this.exexplosion, false);
	}

	@Override
	protected void setEntityDead() {
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.exexplosion / 2, false);
		this.setDead();
	}

	@Override
	protected DamageSource damageSource() {
		return TDamage.BlasterDamage(this, this.thrower);
	}
}
