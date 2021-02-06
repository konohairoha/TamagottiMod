package tamagotti.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import tamagotti.util.TDamage;
import tamagotti.util.TUtil;

public class EntitySeika extends EntityRitter {

	public EntitySeika(World world) {
		super(world);
	}

	public EntitySeika(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	public EntitySeika(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		AxisAlignedBB aabb = living.getEntityBoundingBox().grow(16D, 2.5D, 16D);
		List<EntityLiving> list = living.world.getEntitiesWithinAABB(EntityLiving.class, aabb);
		if (list.isEmpty()) { return; }

		for (EntityLiving liv : list) {
			if (liv != living && liv instanceof IMob) {
				TUtil.tameAIAnger(liv, living);
			}
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
