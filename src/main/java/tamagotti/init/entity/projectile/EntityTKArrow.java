package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTKArrow extends EntityArrow implements IProjectile {

	private ItemStack stack;
	private int tickTime = 0;
	private boolean getArrowFlag;
	private boolean isAP;
	public EntityLivingBase thrower;

	public EntityTKArrow(World world) {
		super(world);
	}

	public EntityTKArrow(World world, EntityLivingBase thrower, ItemStack stack, boolean getArrowFlag, boolean isAP) {
		super(world, thrower);
		this.stack = stack.copy();
		this.getArrowFlag = getArrowFlag;
		this.thrower = thrower;
		this.isAP = isAP;
	}

	public EntityTKArrow(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.tickTime++;
		if (this.tickTime >= 1200) {
			this.setDead();
		}
	}

	//アイテムを拾えないように
	@Override
	public ItemStack getArrowStack() {
		return !this.getArrowFlag ? this.stack : ItemStack.EMPTY;
	}

	//エンティティに当たると爆発
	@Override
	protected void arrowHit(EntityLivingBase target) {
		target.hurtResistantTime = 0;
		if (this.isAP && this.thrower instanceof EntityPlayer) {
			float damage = (float) (target.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue() * 1.5F);
			DamageSource src = DamageSource.causePlayerDamage((EntityPlayer) this.thrower);
			target.attackEntityFrom(src, damage);
			target.hurtResistantTime = 0;
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		this.stack = this.read(tags.getCompoundTag("Item"));
		this.getArrowFlag = tags.getBoolean("getArrowFlag");
		this.isAP = tags.getBoolean("isAP");
		super.readEntityFromNBT(tags);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setTag("Item", this.stack.writeToNBT(new NBTTagCompound()));
		tags.setBoolean("getArrowFlag", this.getArrowFlag);
		tags.setBoolean("isAP", this.isAP);
	}

	public ItemStack read(NBTTagCompound tags) {
		try {
			return new ItemStack(tags);
		} catch (RuntimeException runtimeexception) {
			return ItemStack.EMPTY;
		}
	}
}