package tamagotti.init.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.util.TDamage;

public class EntityRitter extends Entity implements IProjectile {

	protected int xTile;
	protected int yTile;
	protected int zTile;
	protected Block inTile;
	protected int inData;
	protected boolean inGround;
	protected EntityLivingBase thrower;
	private int ticksInGround;
	protected int tickTime;
	protected int knockback;
	protected double damage;

	public EntityRitter(World worldIn) {
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.setSize(1.0F, 1.0F);
	}

	public EntityRitter(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityRitter(World world, EntityLivingBase thrower) {
		this(world, thrower.posX, thrower.posY + thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);
		this.thrower = thrower;
	}

	@Override
	protected void entityInit() { }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}
		d0 = d0 * 128.0D;
		return distance < d0 * d0;
	}

	public void setHeadingFromThrower(Entity entity, float pitch, float yaw, float offset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017F);
		float f1 = -MathHelper.sin((pitch + offset) * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
		this.motionX += entity.motionX;
		this.motionZ += entity.motionZ;
//		if (!entity.onGround) {
//			this.motionY += entity.motionY;
//		}
	}

	//射撃時のメソッド
	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / f;
		y = y / f;
		z = z / f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationPitch = (float) (MathHelper.atan2(y, f) * (180D / Math.PI));
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
		BlockPos pos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState state = this.world.getBlockState(pos);
		if (state.getMaterial() != Material.AIR) {
			AxisAlignedBB aabb = state.getCollisionBoundingBox(this.world, pos);
			if (aabb != Block.NULL_AABB && aabb.offset(pos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.inGround) {
			this.inGround();
		} else {
			++this.tickTime;
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult result = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (result != null) {
				vec3d = new Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z);
			}
			Entity entity = this.findEntityOnPath(vec3d1, vec3d);
			if (entity != null) {
				result = new RayTraceResult(entity);
			}
			if (result != null && result.entityHit != null
					&& result.entityHit instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) result.entityHit;
				if (this.thrower instanceof EntityPlayer
						&& !((EntityPlayer) this.thrower).canAttackPlayer(entityplayer)) {
					result = null;
				}
			}
			if (result != null
					&& !ForgeEventFactory.onProjectileImpact(this, result)) {
				this.onHit(result);
			}
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f4) * (180D / Math.PI));
					this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;}
			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}
			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}
			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}
			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f1 = 0.99F;
			if (this.isInWater()) {
				f1 = this.inWaterSpeed();
			}
			if (this.world.isRemote) {
				this.generateRandomParticles();
			}
			if (this.isWet()) {
				this.extinguish();
			}
			this.motionX *= f1;
			this.motionY *= f1;
			this.motionZ *= f1;
			this.isGravity();
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}
		if (this.tickTime >= 30 + this.plusTickAir()) {
			this.setEntityDead();
		}
	}

	// ダメージソース(誰が攻撃したかをわかるために)
	protected DamageSource damageSource(){
		return TDamage.RitterDamage(this, this.thrower);
	}

	protected void onHit(RayTraceResult result) {
		Entity entity = result.entityHit;
		if (entity != null) {
			DamageSource src = this.damageSource();
			if (this.isBurning()) {
				entity.setFire(5);
			}

//			entity.getEntityAttribute(SharedMonsterAttributes.ARMOR);

			if (entity.attackEntityFrom(src, (float) this.damage)) {
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase living = (EntityLivingBase) entity;
					if (this.knockback > 0) {
						float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
						if (f1 > 0.0F) {
							living.addVelocity(
									this.motionX * this.knockback * 0.6D / f1, 0.1D,
									this.motionZ * this.knockback * 0.6D / f1);
						}
					}
					if (this.thrower instanceof EntityLivingBase) {
						EnchantmentHelper.applyThornEnchantments(living, this.thrower);
						EnchantmentHelper.applyArthropodEnchantments(this.thrower, living);
					}
					this.entityHit(living);

					// 無敵時間をなくす
					living.hurtResistantTime = 0;
					if (this.thrower != null && living != this.thrower
							&& living instanceof EntityPlayer && this.thrower instanceof EntityPlayerMP) {
						((EntityPlayerMP) this.thrower).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
					}
				}
				if (!(entity instanceof EntityEnderman)) {
					this.setEntityDead();
				}
			} else {
				this.setEntityDead();
			}
		} else {
			BlockPos pos = result.getBlockPos();
			this.xTile = pos.getX();
			this.yTile = pos.getY();
			this.zTile = pos.getZ();
			IBlockState state = this.world.getBlockState(pos);
			this.inTile = state.getBlock();
			this.inData = this.inTile.getMetaFromState(state);
			this.motionX = ((float) (result.hitVec.x - this.posX));
			this.motionY = ((float) (result.hitVec.y - this.posY));
			this.motionZ = ((float) (result.hitVec.z - this.posZ));
			float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			this.posX -= this.motionX / f2 * 0.05000000074505806D;
			this.posY -= this.motionY / f2 * 0.05000000074505806D;
			this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
			this.inGround = true;
			if (state.getMaterial() != Material.AIR) {
				this.inTile.onEntityCollidedWithBlock(this.world, pos, state, this);
			}
		}
	}

	//地面についたときの処理
	protected void inGround() {
		this.setEntityDead();
	}

	//えんちちーの死亡の処理
	protected void setEntityDead() {
		this.setDead();
	}

	// 重力加速度及び空中時のonUpdateの追加
	protected void isGravity() { }

	// 水中での速度減衰
	protected float inWaterSpeed() {
		return 0.6F;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir(増やしたい場合は-10とか付ければおっけー)
	protected int plusTickAir() {
		return 0;
	}

	public static void registerFixesThrowable(DataFixer fixer, String name){}

	//えんちちーに当たった時の処理
	protected void entityHit(EntityLivingBase living){}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		super.move(type, x, y, z);
		if (this.inGround) {
			this.xTile = MathHelper.floor(this.posX);
			this.yTile = MathHelper.floor(this.posY);
			this.zTile = MathHelper.floor(this.posZ);
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
		if (list.isEmpty()) {return entity;}

		double d0 = 0.0D;
		double d1;
		if (!this.world.isRemote) {
			for (Entity e : list) {
				if (e.canBeCollidedWith() && (e != this.thrower || this.tickTime >= 5)) {
					AxisAlignedBB aabb = e.getEntityBoundingBox().grow(0.30000001192092896D);
					RayTraceResult result = aabb.calculateIntercept(start, end);
					if (result != null) {
						d1 = start.distanceTo(result.hitVec);
						if (d1 < d0 || d0 == 0.0D) {
							entity = e;
							d0 = d1;
						}
					}
				}
			}
		}
		return entity;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		tags.setInteger("xTile", this.xTile);
		tags.setInteger("yTile", this.yTile);
		tags.setInteger("zTile", this.zTile);
		tags.setShort("life", (short) this.ticksInGround);
		ResourceLocation location = Block.REGISTRY.getNameForObject(this.inTile);
		tags.setString("inTile", location == null ? "" : location.toString());
		tags.setByte("inData", (byte) this.inData);
		tags.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		tags.setDouble("damage", this.damage);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		this.xTile = tags.getInteger("xTile");
		this.yTile = tags.getInteger("yTile");
		this.zTile = tags.getInteger("zTile");
		this.ticksInGround = tags.getShort("life");
		if (tags.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(tags.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(tags.getByte("inTile") & 255);
		}
		this.inData = tags.getByte("inData") & 255;
		this.inGround = tags.getByte("inGround") == 1;
		if (tags.hasKey("damage", 99)) {
			this.damage = tags.getDouble("damage");
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {}

	public void setDamage(double damageIn) {
		this.damage = damageIn;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setKnockbackStrength(int knockback) {
		this.knockback = knockback;
	}

	@SideOnly(Side.CLIENT)
	protected void generateRandomParticles(){}
}
