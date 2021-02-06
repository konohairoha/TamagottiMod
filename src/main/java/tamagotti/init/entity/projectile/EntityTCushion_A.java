package tamagotti.init.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TUtil;

public class EntityTCushion_A extends Entity {

	private int totalAge = 0;
	private int count = 0;
	private byte sideInt = 0;
	public BlockPos deadPos;
	private static final DataParameter<Integer> AGE = EntityDataManager.<Integer>createKey(EntityTCushion_A.class, DataSerializers.VARINT);
	private static final DataParameter<EnumFacing> SIDE = EntityDataManager.<EnumFacing>createKey(EntityTCushion_A.class, DataSerializers.FACING);

	public EntityTCushion_A(World world) {
		super(world);
		this.setSize(0.4F, 0.25F);
		this.setSide(EnumFacing.DOWN);
	}

	protected ItemStack drops() {
		return new ItemStack(ItemInit.tcushion_a);
	}

	@Override
	public double getMountedYOffset() {
		return -0.125D;
	}

	public EntityTCushion_A(World world, double posX, double posY, double posZ) {
		this(world);
		this.setPosition(posX, posY, posZ);
	}

	public EntityTCushion_A(World world, double posX, double posY, double posZ, @Nullable EntityPlayer player) {
		this(world, posX, posY, posZ);
		if (player != null)
			this.rotationYaw = player.rotationYaw;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {

		if (this.world.isRemote || player == null) { return true; }

		if (!player.isSneaking()) {
			if (!this.getPassengers().isEmpty()) {
				this.getPassengers().get(0).dismountRidingEntity();
			}
			player.startRiding(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {

		if (this.isEntityInvulnerable(source) || source.isFireDamage() || source.isMagicDamage()) {
			return false;
		} else if (source instanceof EntityDamageSource) {
			this.markVelocityChanged();
			this.dropAndDeath(null);
		}
		return false;
	}

	/* update処理 */
	@Override
	public void onUpdate() {

		super.onUpdate();
		if (!this.world.isRemote && this.deadPos != null) {
			this.dropAndDeath(this.deadPos);
		}
		if (this.posY < -16.0D) {
			this.setDead();
		}

		BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY), MathHelper.floor(this.posZ));
		if (!this.world.isRemote && this.count++ == 20) {
			this.count = 0;
			this.totalAge++;
			if (this.getSide().getIndex() != this.sideInt) {
				this.dataManager.set(SIDE, EnumFacing.getFront(this.sideInt));
			}
		}

		// 動作
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (this.isFallable()) {
			this.motionY -= 0.04D;
			this.handleWaterMovement();
			float f = 0.98F;
			IBlockState in = this.world.getBlockState(pos);
			IBlockState under = this.world.getBlockState(pos.down());

			if (in.getBlock() == Blocks.HOPPER || under.getBlock() == Blocks.HOPPER) {
				this.dropAndDeath(null);
			} else if (this.world.getTileEntity(pos.down()) != null && this.world.getTileEntity(pos.down()) instanceof IHopper) {
				this.dropAndDeath(null);
			}

			// 水中
			if (this.inWater && this.isFloatOnWater() /* && this.checkInWater() */) {
				this.motionY += 0.08D;
				if (this.motionY > 0.1D) {
					this.motionY = 0.1D;
				}
				this.motionX *= 0.93D;
				this.motionZ *= 0.93D;
			} else {
				if (this.onGround) {
					f = under.getBlock().slipperiness;
					this.motionX *= f * 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= f * 0.5D;
				} else {
					this.motionX *= 0.5D;
					this.motionY *= 0.95D;
					this.motionZ *= 0.5D;
				}
			}

			this.doBlockCollisions();

			if (this.motionX * this.motionX < 0.0005D) {
				this.motionX = 0.0D;
			}

			if (this.motionY * this.motionY < 0.0005D) {
				this.motionY = 0.0D;
			}

			if (this.motionZ * this.motionZ < 0.0005D) {
				this.motionZ = 0.0D;
			}
		}
	}

	@Override
	protected boolean pushOutOfBlocks(double x, double y, double z) {

		BlockPos pos = new BlockPos(x, y, z);
		double d0 = x - pos.getX();
		double d1 = y - pos.getY();
		double d2 = z - pos.getZ();
		List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, this.getEntityBoundingBox());
		if (list.isEmpty()) { return false; }

		EnumFacing face = EnumFacing.UP;
		double d3 = Double.MAX_VALUE;

		if (!this.world.isBlockFullCube(pos.west()) && d0 < d3) {
			d3 = d0;
			face = EnumFacing.WEST;
		}

		if (!this.world.isBlockFullCube(pos.east()) && 1.0D - d0 < d3) {
			d3 = 1.0D - d0;
			face = EnumFacing.EAST;
		}

		if (!this.world.isBlockFullCube(pos.north()) && d2 < d3) {
			d3 = d2;
			face = EnumFacing.NORTH;
		}


		if (!this.world.isBlockFullCube(pos.south()) && 1.0D - d2 < d3) {
			d3 = 1.0D - d2;
			face = EnumFacing.SOUTH;
		}

		if (!this.world.isBlockFullCube(pos.up()) && 1.0D - d1 < d3) {
			d3 = 1.0D - d1;
			face = EnumFacing.UP;
		}

		float f = 0.1F;
		float f1 = face.getAxisDirection().getOffset();

		if (face.getAxis() == EnumFacing.Axis.X) {
			this.motionX += f1 * f;
		} else if (face.getAxis() == EnumFacing.Axis.Y) {
			this.motionY += f1 * f;
		} else if (face.getAxis() == EnumFacing.Axis.Z) {
			this.motionZ += f1 * f;
		}

		return true;
	}

	public boolean collideable() {
		return true;
	}

	protected void dropAndDeath(@Nullable BlockPos pos) {

		if (pos == null) {
			this.dropAsItem(this.posX, this.posY + 0.25D, this.posZ);
		} else {
			this.dropAsItem(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		}

		this.setDead();
	}

	protected void dropAsItem(double x, double y, double z) {
		if (!this.world.isRemote) {
			EntityItem drop = new EntityItem(this.world, x, y, z, this.drops());
			drop.motionY = 0.025D;
			this.world.spawnEntity(drop);
		}
	}

	/* 動き */
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.getEntityBoundingBox();
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public boolean handleWaterMovement() {

		if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this)) {
			if (!this.inWater && !this.firstUpdate) {
				this.doWaterSplashEffect();
			}
			this.inWater = true;
		} else {
			this.inWater = false;
		}
		return this.inWater;
	}

	public static float getBlockLiquidHeight(IBlockState state, IBlockAccess world, BlockPos pos) {
		int i = state.getValue(BlockLiquid.LEVEL).intValue();
		return (i & 7) == 0 && world.getBlockState(pos.up()).getMaterial() == Material.WATER ? 1.0F
				: 1.0F - BlockLiquid.getLiquidHeightPercent(i);
	}

	public static float getLiquidHeight(IBlockState state, IBlockAccess world, BlockPos pos) {
		return pos.getY() + getBlockLiquidHeight(state, world, pos);
	}

	@Override
	protected void dealFireDamage(int amount) {
		this.attackEntityFrom(DamageSource.IN_FIRE, amount);
	}

	public boolean isCollectable(@Nullable ItemStack item) {
		return !TUtil.isEmpty(item) && item.getItem() instanceof ItemSpade;
	}

	public int getCollectArea(@Nullable ItemStack item) {
		return 2;
	}

	public boolean doCollect(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack tool) {
		if (!world.isRemote && !TUtil.isEmpty(this.drops())) {
			this.deadPos = player.getPosition();
			return true;
		}
		return false;
	}

	/* パラメータ各種 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	protected boolean isFallable() {
		return true;
	}

	protected boolean isFloatOnWater() {
		return true;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(AGE, 0);
		this.dataManager.register(SIDE, EnumFacing.DOWN);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		this.totalAge = tag.getInteger("dcs.entityage");
		this.sideInt = tag.getByte("dcs.entityside");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("dcs.entityage", totalAge);
		tag.setByte("dcs.entityside", sideInt);
	}

	public void setRotation(float f) {
		this.rotationYaw = f;
	}

	public void setSide(EnumFacing side) {
		this.dataManager.set(SIDE, side);
		this.sideInt = (byte) side.getIndex();
	}

	public void setAGE(int age) {
		this.dataManager.set(AGE, age);
	}

	public EnumFacing getSide() {
		return this.dataManager.get(SIDE);
	}

	public int getAge() {
		return this.dataManager.get(AGE);
	}

	public int getTotalAge() {
		return this.totalAge;
	}
}
