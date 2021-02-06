package tamagotti.init.entity.projectile;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

public class EntityTorpedo extends EntityArrow implements IThrowableEntity {

	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityTorpedo.class, DataSerializers.VARINT);
	private static final double seekDistance = 5.0;
	private static final double seekFactor = 0.2;
	private static final double seekAngle = Math.PI / 6.0;
	private static final double seekThreshold = 0.5;
	public int tickTime = 0;

	public EntityTorpedo(World world) {
		super(world);
	}

	public EntityTorpedo(World world, EntityPlayer player) {
		super(world, player);
	}

	//アイテムを拾えないように
	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public Entity getThrower() {
		return this.shootingEntity;
	}

	@Override
	public void setThrower(Entity entity) {
		this.shootingEntity = entity;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TARGET, -1);
	}

	@Override
	public void onUpdate() {

		this.tickTime++;
		if(this.tickTime >= 120) {
			this.setDead();
		}

		if (this.isThisArrowFlying()) {
			if (!this.world.isRemote) {
				this.updateTarget();
			}

			if (this.world.isRemote && !this.inGround) {
				for (int k = 0; k < 16; ++k) {
					this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
				}
			}

			Entity target = getTarget();
			if (target != null) {
				Vec3d targetVec = getVectorToTarget(target).scale(seekFactor);
				Vec3d courseVec = getMotionVec();
				double courseLen = courseVec.lengthVector();
				double targetLen = targetVec.lengthVector();
				double totalLen = MathHelper.sqrt(courseLen*courseLen + targetLen*targetLen);
				double dotProduct = courseVec.dotProduct(targetVec) / (courseLen * targetLen);
				if (dotProduct > seekThreshold) {
					Vec3d newMotion = courseVec.scale(courseLen / totalLen).add(targetVec.scale(targetLen / totalLen));
					this.motionX = newMotion.x;
					this.motionY = newMotion.y;
					this.motionZ = newMotion.z;
					this.motionY += 0.045F;
				} else if (!this.world.isRemote) {
					setTarget(null);
				}
			}
		}
		super.onUpdate();
	}

	private void updateTarget() {

		if (this.getTarget() != null && getTarget().isDead) {
			this.setTarget(null);
		}

		if (this.getTarget() == null) {
			AxisAlignedBB aaBox_A = new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ);
			AxisAlignedBB aaBox_B = aaBox_A;
			Vec3d courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) seekAngle);
			aaBox_B = aaBox_B.union(aaBox_A.offset(courseVec));
			courseVec = getMotionVec().scale(seekDistance).rotateYaw((float) -seekAngle);
			aaBox_B = aaBox_B.union(aaBox_A.offset(courseVec));
			aaBox_B = aaBox_B.grow(0, seekDistance * 0.5, 0);
			double closestDot = -1.0;
			for (EntityLivingBase living : this.world.getEntitiesWithinAABB(EntityLivingBase.class, aaBox_B)) {
				if (!(living instanceof EntityPlayer)) {
					courseVec = getMotionVec().normalize();
					Vec3d targetVec = getVectorToTarget(living).normalize();
					double dot = courseVec.dotProduct(targetVec);
					if (dot > Math.max(closestDot, seekThreshold)) {
						setTarget(living);
						closestDot = dot;
					}
				}
			}
		}
	}

	private Vec3d getMotionVec() {
		return new Vec3d(this.motionX, this.motionY, this.motionZ);
	}

	private Vec3d getVectorToTarget(Entity target) {
		return new Vec3d(target.posX - this.posX, (target.posY + target.getEyeHeight()) - this.posY, target.posZ - this.posZ);
	}

	@Nullable
	private Entity getTarget() {
		return this.world.getEntityByID(this.dataManager.get(TARGET));
	}

	private void setTarget(@Nullable Entity e) {
		this.dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}

	private boolean isThisArrowFlying() {
		return MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) > 1.0;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase living){
	    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4.0F, false);
	}
}
