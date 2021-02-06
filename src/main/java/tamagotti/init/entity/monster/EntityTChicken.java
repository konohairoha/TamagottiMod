package tamagotti.init.entity.monster;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;

public class EntityTChicken extends EntityCreeper {

    private int inLove = 0;
    private UUID playerInLove;
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 20;
    public boolean chickenJockey;
    public int timeUntilNextEgg;
    public float wingRotation;
    public float wingRotDelta = 1.0F;
    public float oFlapSpeed;
    public float oFlap;
    public float destPos;
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

	public EntityTChicken(World world) {
		super(world);
        this.setSize(0.4F, 0.7F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	@Override
	protected void applyEntityAttributes(){
        super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		if (this.isChickenJockey()) {
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
			this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
		} else {
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
			this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		}
    }

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAICreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.9D));
		this.tasks.addTask(6, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, this.isChickenJockey() ? 12F : 8F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

    @Override
	public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
		this.chickenJockey = compound.getBoolean("IsChickenJockey");
		if (compound.hasKey("EggLayTime")) {
			this.timeUntilNextEgg = compound.getInteger("EggLayTime");
		}
    }

    @Override
	public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.inLove > 0) {
			--this.inLove;

			if (this.inLove % 10 == 0) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.spawnParticle(EnumParticleTypes.HEART,
						this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
						this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
						this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1,
						d2);
			}
		}

        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);

		if (!this.onGround && this.wingRotDelta < 1.0F) {
			this.wingRotDelta = 1.0F;
		}

		this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);

		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

        this.wingRotation += this.wingRotDelta * 2.0F;

		if (!this.world.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.dropItem(Items.EGG, 1);
            this.timeUntilNextEgg = this.rand.nextInt(2000) + 2000;
        }
	}

	@Override
	public void onUpdate() {
		if (this.isEntityAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;
			int i = this.getCreeperState();
			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}
			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}
			this.timeSinceIgnited += i;
			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}
			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
			this.getAmbientSound();
		}

		super.onUpdate();
	}

	private void explode() {
		if (!this.world.isRemote) {
            this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4.0F, false);
            this.setDead();
        }
    }

    @Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
        	this.entityDropItem(new ItemStack(ItemInit.tamagotticustom, world.rand.nextInt(3) + 1, 0), 0.0F);
        	this.entityDropItem(new ItemStack(Items.CHICKEN, world.rand.nextInt(1) + 1, 0), 0.0F);
        }
    }

	public boolean isChickenJockey() {
		return this.chickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		this.chickenJockey = jockey;
	}

	public boolean isBreedingItem(ItemStack stack) {
		return TEMPTATION_ITEMS.contains(stack.getItem());
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);

		if (!stack.isEmpty() && stack.getItem() == Items.WHEAT_SEEDS/*this.isBreedingItem(stack)*/ /*&& this.inLove <= 0*/) {
			this.consumeItemFromStack(player, stack);
			this.setInLove(player);
			return true;
		}

		return super.processInteract(player, hand);
	}

	protected void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
		if (!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}
	}

	public void setInLove(@Nullable EntityPlayer player) {
		this.inLove = 600;

		if (player != null) {
			this.playerInLove = player.getUniqueID();
		}

		this.world.setEntityState(this, (byte) 18);
	}
}
