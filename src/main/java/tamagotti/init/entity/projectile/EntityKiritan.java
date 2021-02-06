package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityKiritan extends EntityArrow implements IProjectile {

	protected int tickTime = 0;

	public EntityKiritan(World worldIn) {
		super(worldIn);
	}

	public EntityKiritan(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityKiritan(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.tickTime++;
		if (this.inGround) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.25F, false);
			this.setDead();
		}
		if (this.tickTime > 30) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.25F, false);
			this.setDead();
		}
	}

	//アイテムを拾えないように
	@Override
	public ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase living) {
		living.hurtResistantTime = 0;
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.5F, false);
		this.setDead();
	}
}