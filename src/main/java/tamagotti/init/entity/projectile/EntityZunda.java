package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TDamage;

public class EntityZunda extends EntityRitter {

	public EntityZunda(World worldIn) {
		super(worldIn);
	}

	public EntityZunda(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityZunda(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	//えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		//　攻撃対象の体力が弾の攻撃力を上回ってた時
		if (living.getHealth() <= this.getDamage()) {
			living.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(ItemInit.zunda)));
			living.onDeath(DamageSource.causePlayerDamage((EntityPlayer)this.thrower));
			living.setHealth(0); //防具着てようがうるせーーーーーしらねーーーーーーーー！！
		}
	}

	@Override
	protected DamageSource damageSource() {
		return TDamage.ZundaDamage(this, this.thrower);
	}
}
