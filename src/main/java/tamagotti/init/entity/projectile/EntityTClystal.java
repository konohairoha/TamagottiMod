package tamagotti.init.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TUtil;

public class EntityTClystal extends Entity {

	private static final DataParameter<Integer> AGE = EntityDataManager.<Integer>createKey(EntityTCushion_A.class, DataSerializers.VARINT);
	private static final DataParameter<EnumFacing> SIDE = EntityDataManager.<EnumFacing>createKey(EntityTCushion_A.class, DataSerializers.FACING);
	public int innerRotation;

	public EntityTClystal(World worldIn) {
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(1.0F, 1.0F);
		this.innerRotation = this.rand.nextInt(100000);
	}

	public EntityTClystal(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(AGE, 0);
		this.dataManager.register(SIDE, EnumFacing.DOWN);
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		++this.innerRotation;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!(source.getTrueSource() instanceof EntityPlayer)) {
			return false;
		}

		if (!this.world.isRemote && !TUtil.isEmpty(this.getDropItem())) {
			ItemStack item = this.getDropItem();
			EntityItem drop = new EntityItem(world, this.posX, this.posY, this.posZ, item);
			drop.motionY = 0.025D;
			this.world.spawnEntity(drop);
		}
		this.setDead();
		return true;
	}

	public ItemStack getDropItem() {
		return new ItemStack(ItemInit.tcrystal);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) { }
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}
}
