package tamagotti.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import tamagotti.init.ItemInit;
import tamagotti.util.TMobLootTable;

public class EntitySkullFlame extends EntitySkeleton {

	private int tickTime = 0;
	private int teleportDelay;
    public boolean chickenJockey;

	public EntitySkullFlame (World world) {
		super(world);
        this.experienceValue = 40;
        this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

	@Nullable
	@Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        if (this.rand.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemInit.copper_helmet));
        }
        if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }
        return livingdata;
    }

	@Override
	protected EntityArrow getArrow(float par1) {
		Random rand = new Random();
		EntityArrow arrow = super.getArrow(par1);
		arrow = super.getArrow(par1);
	    arrow.setFire(200);

		if (rand.nextInt(32) == 0) {
			if (!this.world.isRemote) {
				this.world.spawnEntity(new EntityBurningSkullHead(this.world));
			}
		}
        return arrow;
    }

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemInit.copper_helmet));
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.world.isRemote && isEntityAlive() && this.teleportDelay-- <= 0 && getAttackTarget() != null && this.rand.nextInt(30) == 0 && !this.isChickenJockey()) {
			if (this.getAttackTarget().getDistanceSq(this) > 36.0) {
				for (int i = 0; i < 16; i++) {
					if (this.teleportToEntity(getAttackTarget())) {
						this.teleportDelay = 90;
						break;
					}
				}
			} else {
				for (int i = 0; i < 16; i++) {
					if (this.teleportRandomly()) {
						this.teleportDelay = 45;
						break;
					}
				}
			}
		}

		this.tickTime++;
		if (this.tickTime >= 60) {
			this.tickTime = 0;
			this.spawnParticle();
		}
	}

	private boolean teleportRandomly() {
		double targetX = this.posX + (this.rand.nextDouble() - 0.5) * 20.0;
		double targetY = this.posY + (this.rand.nextInt(12) - 4);
		double targetZ = this.posZ + (this.rand.nextDouble() - 0.5) * 20.0;
		this.spawnParticle();
		return teleportTo(targetX, targetY, targetZ);
	}

	private boolean teleportToEntity (Entity entity ) {
		Vec3d vec3d = new Vec3d(this.posX - entity.posX, getEntityBoundingBox().minY + this.height / 2.0F - entity.posY + entity.getEyeHeight( ), this.posZ - entity.posZ);
		vec3d = vec3d.normalize();
		double targetX = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
		double targetY = this.posY + (this.rand.nextInt(8) - 2) - vec3d.y * 16.0;
		double targetZ = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
		this.spawnParticle();
		return teleportTo(targetX, targetY, targetZ);
	}

	private boolean teleportTo(double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean success = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		if (success) {
			playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
		}
		return success;
	}

	public void spawnParticle() {
		for (int i = 0; i < 16; i++) {
			float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
			float f2 = (float) ((float) this.posY + 0.25F + this.rand.nextFloat() * 1.5);
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, 0, 0, 0);
		}
	}

	// えんちちーの光の大きさ
	@Override
	public float getBrightness() {
        return 1.0F;
    }

	@Override
	public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote){
    		this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, TMobLootTable.loot(1)));
        }
    }

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (src.getTrueSource() instanceof EntityKumongous) {
			return false;
		}
		super.attackEntityFrom(src, amount);
		return true;
	}

	public boolean isChickenJockey() {
		return this.chickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		this.chickenJockey = jockey;
	}

    @Override
	public void readEntityFromNBT(NBTTagCompound tags) {
        super.readEntityFromNBT(tags);
		this.chickenJockey = tags.getBoolean("IsChickenJockey");
    }

    @Override
	public void writeEntityToNBT(NBTTagCompound tags) {
        super.writeEntityToNBT(tags);
        tags.setBoolean("IsChickenJockey", this.chickenJockey);
    }
}
