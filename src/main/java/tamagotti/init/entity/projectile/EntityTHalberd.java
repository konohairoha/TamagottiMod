package tamagotti.init.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTHalberd extends EntityArrow {

	private ItemStack stack;
	private double damage;
	private int knockback;
	public int tickTime;
	public int xTile;
	public int yTile;
	public int zTile;
    private Block inTile;
    public int inData;
    private EntityPlayer player;
    private int area;

	public EntityTHalberd(World world) {
		super(world);
        this.setSize(0.312F, 1.75F);
	}

	public EntityTHalberd(World world, EntityPlayer thrower, ItemStack stack, int area) {
		super(world, thrower);
        this.setSize(0.312F, 1.75F);
		this.stack = stack.copy();
		this.player = thrower;
		this.area = area;
	}

	public EntityTHalberd(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public void setDamage(double damage) {
		this.damage = damage;
	}

	@Override
	public double getDamage() {
		return this.damage;
	}

	@Override
	protected void onHit(RayTraceResult result) {
		Entity entity = result.entityHit;

		if (entity != null) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			int i = MathHelper.ceil(f * this.damage);

			if (this.getIsCritical()) {
				i += this.rand.nextInt(i / 2 + 2);
			}

			DamageSource src;

			if (this.shootingEntity == null) {
				src = DamageSource.causeArrowDamage(this, this);
			} else {
				src = DamageSource.causeArrowDamage(this, this.shootingEntity);
			}

			if (this.isBurning() && !(entity instanceof EntityEnderman)) {
				entity.setFire(5);
			}

			if (entity.attackEntityFrom(src, i)) {
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase living = (EntityLivingBase) entity;

					if (!this.world.isRemote) {
						living.setArrowCountInEntity(living.getArrowCountInEntity() + 1);
					}

					if (this.knockback > 0) {
						float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

						if (f1 > 0.0F) {
							living.addVelocity(
									this.motionX * this.knockback * 0.6000000238418579D / f1, 0.1D,
									this.motionZ * this.knockback * 0.6000000238418579D / f1);
						}
					}

					if (this.shootingEntity instanceof EntityLivingBase) {
						EnchantmentHelper.applyThornEnchantments(living, this.shootingEntity);
						EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, living);
					}

					this.arrowHit(living);

					if (this.shootingEntity != null && living != this.shootingEntity
							&& living instanceof EntityPlayer
							&& this.shootingEntity instanceof EntityPlayerMP) {
						((EntityPlayerMP) this.shootingEntity).connection
								.sendPacket(new SPacketChangeGameState(6, 0.0F));
					}
				}

			} else {
				this.motionX *= -0.10000000149011612D;
				this.motionY *= -0.10000000149011612D;
				this.motionZ *= -0.10000000149011612D;
				this.rotationYaw += 180.0F;
				this.prevRotationYaw += 180.0F;
				this.tickTime = 0;

				if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY
						+ this.motionZ * this.motionZ < 0.0010000000474974513D) {
					this.setDead();
				}
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
			this.arrowShake = 7;
			this.setIsCritical(false);

			if (state.getMaterial() != Material.AIR) {
				this.inTile.onEntityCollidedWithBlock(this.world, pos, state, this);
			}

			if (this.inGround) {
				this.setDead();
			}
		}
	}

	@Override
	public void setDead() {

		if (!this.world.isRemote) {
			if (this.player != null) {
				this.player.getCooldownTracker().setCooldown(this.getArrowStack().getItem(), 30);
				this.world.spawnEntity(new EntityItem(this.world, this.player.posX, this.player.posY, this.player.posZ, this.getArrowStack()));
			} else {
				this.entityDropItem(this.stack, 0);
			}
		}
		this.isDead = true;
	}

    @Override
	public ItemStack getArrowStack() {
		return this.stack;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase target) {
		if (this.area > 0) {

			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(2.5D + this.area, 2.5D + this.area, 2.5D + this.area);
			List<EntityLiving> list = player.world.getEntitiesWithinAABB(EntityLiving.class, aabb);
			if (list.isEmpty()) { return; }

			DamageSource src = DamageSource.causePlayerDamage(this.player);

			for (EntityLiving liv : list) {
				if (!(liv instanceof EntityPigZombie)) {
					liv.attackEntityFrom(src, (float) this.getDamage());
				}
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		if (tags.hasUniqueId("uuid")) {
			this.stack = this.read(tags.getCompoundTag("Item"));
		}
		super.readEntityFromNBT(tags);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setTag("Item", this.stack.writeToNBT(new NBTTagCompound()));
	}

	public ItemStack read(NBTTagCompound compound) {
		try {
			return new ItemStack(compound);
		} catch (RuntimeException runtimeexception) {
			return ItemStack.EMPTY;
		}
	}
}
