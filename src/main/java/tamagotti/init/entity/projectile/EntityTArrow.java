package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTArrow extends EntityArrow {

	public int tickTime = 0;

	public EntityTArrow(World worldIn) {
		super(worldIn);
	}

	public EntityTArrow(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

    public EntityTArrow(World worldIn, double x, double y, double z){
        super(worldIn, x, y, z);
    }

	@Override
	public void onUpdate() {
		super.onUpdate();
		tickTime++;

		if (tickTime >= 100) {
			this.setDead();
		}

		if (this.inGround) {
			this.inGround();
		}
	}

	protected void inGround() {
		this.setEntityDead();
	}

	protected void setEntityDead() {
		this.setDead();
	}

	//アイテムを拾えないように
    @Override
	public ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase target) {
		target.hurtResistantTime = 0;
		this.setDead();
	}
}