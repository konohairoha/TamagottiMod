package tamagotti.init.entity.projectile;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTEx extends EntityArrow implements IProjectile {

	protected int tickTime = 0;
	Random rand = new Random();

	public EntityTEx(World worldIn) {
		super(worldIn);
	}

	public EntityTEx(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityTEx(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		tickTime++;
		if (this.tickTime > 100) {
			for (int i = 0; i <= 8; i++) {
				int xRand = rand.nextInt(100) - 50;
				int zRand = rand.nextInt(100) - 50;
				this.world.createExplosion(this, this.posX - xRand, this.posY, this.posZ - zRand, 64F, true);
			}
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
		this.world.createExplosion(this, this.posX, this.posY, this.posZ, 128F, true);
		this.setDead();
	}
}
