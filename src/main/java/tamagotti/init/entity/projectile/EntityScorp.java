package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityScorp extends EntityRitter {

	public EntityScorp(World worldIn) {
		super(worldIn);
	}

	public EntityScorp(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityScorp(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	protected void setEntityDead() {
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.5F, false);
		this.setDead();
	}

	//エンティティに当たった時
	@Override
	protected void entityHit(EntityLivingBase living) {
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.5F, false);
		this.setDead();
	}
}