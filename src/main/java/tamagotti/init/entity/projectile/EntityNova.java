package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import tamagotti.util.TDamage;

public class EntityNova extends EntityRitter {

	protected int tickTime = 0;

	public EntityNova(World worldIn) {
		super(worldIn);
	}

	public EntityNova(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityNova(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void onUpdate() {
		this.tickTime++;
		if (this.tickTime > 0) {
			if (this.world.isRemote && !this.inGround) {
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
			}

			if (this.tickTime >= 4) {
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4.5F, false);
				this.setDead();
			}
		}
		super.onUpdate();
	}

	//エンティティに当たると爆発
	protected void arrowHit(EntityLivingBase living) {
		this.world.createExplosion(null, this.posX, this.posY, this.posZ, 5.5F, false);
	}

	@Override
	protected void setEntityDead() {
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
		this.setDead();
	}

	@Override
	protected DamageSource damageSource() {
		return TDamage.BlasterDamage(this, this.thrower);
	}
}
