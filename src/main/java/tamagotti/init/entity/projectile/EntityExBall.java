package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityExBall extends EntityFireball {

    private int tickTime;
    private EntityLivingBase living;

	public EntityExBall(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntityExBall(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.5F, 0.5F);
		this.living = shooter;
	}

	public EntityExBall(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(0.5F, 0.5F);
	}

	public static void registerFixesSmallFireball(DataFixer fixer) {
		EntityFireball.registerFixesFireball(fixer, "SmallFireball");
	}

	@Override
	public void onUpdate() {

		if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead)
				&& this.world.isBlockLoaded(new BlockPos(this))) {
			if (!this.world.isRemote) {
				this.setFlag(6, this.isGlowing());
			}
	        this.onEntityUpdate();

			++this.tickTime;
			if (tickTime >= 60) {
				this.setDead();
			}

			RayTraceResult result = ProjectileHelper.forwardsRaycast(this, true, this.tickTime >= 60, this.shootingEntity);

			if (result != null && !ForgeEventFactory.onProjectileImpact(this, result)) {
				this.onImpact(result);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			ProjectileHelper.rotateTowardsMovement(this, 0.2F);
			float f = this.getMotionFactor();

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= f;
			this.motionY *= f;
			this.motionZ *= f;
			this.setPosition(this.posX, this.posY, this.posZ);
		} else {
			this.setDead();
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {

		if (!this.world.isRemote) {

			// えんちちーに当たった時
			if (result.entityHit != null) {
				if (!result.entityHit.isImmuneToFire()) {
					boolean flag = result.entityHit.attackEntityFrom(DamageSource.MAGIC, 10.0F);

					if (flag) {
						this.world.createExplosion(this.living, this.posX, this.posY, this.posZ, 3.0F, false);
						this.applyEnchantments(this.shootingEntity, result.entityHit);
					}
				}

			// ブロックに当たった時
			} else {

				if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
					this.world.createExplosion(this.living, this.posX, this.posY, this.posZ, 2.0F, false);
					ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
				}
			}

			this.setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}
}
