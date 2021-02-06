package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityBettyu extends EntityArrow implements IProjectile{

	protected int livingTimeCount = 0;
	public int count;

    public EntityBettyu(World worldIn){
        super(worldIn);
    }

    public EntityBettyu(World worldIn, EntityLivingBase throwerIn, int count){
        super(worldIn, throwerIn);
        this.count = count;
    }

    public EntityBettyu(World worldIn, double x, double y, double z){
        super(worldIn, x, y, z);
    }

    @Override
	public void onUpdate() {
		super.onUpdate();
		livingTimeCount++;
		float a = (livingTimeCount - 60) / 10;
		if(livingTimeCount >= 60) {
			if(livingTimeCount % 10 == 0){
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2.5F * (a + 1), true);
			} else if(livingTimeCount > 30 * this.count) {
				this.setDead();
			}
		}
	}

    //アイテムを拾えないように
    @Override
	public ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

    @Override
    protected void arrowHit(EntityLivingBase living){
    	this.world.createExplosion(this, this.posX, this.posY, this.posZ, 64.0F, true);
    }
}