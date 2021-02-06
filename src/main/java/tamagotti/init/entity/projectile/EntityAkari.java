package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TDamage;

public class EntityAkari extends EntityRitter {

	public EntityAkari(World world) {
		super(world);
	}

	public EntityAkari(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	public EntityAkari(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		//　攻撃対象の体力が弾の攻撃力を上回ってた時
		if (living.getHealth() <= this.getDamage()) {
			living.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(ItemInit.rice)));
			living.setHealth(0); //防具着てようがうるせーーーーーしらねーーーーーーーー！！
		}
	}

	//地面についたときの処理
	@Override
	protected void inGround() {
		this.setEntityDead();
	}

	@Override
	protected void setEntityDead() {
		this.setDead();
	}

	@Override
	protected DamageSource damageSource() {
		return TDamage.AkariDamage(this, this.thrower);
	}
}