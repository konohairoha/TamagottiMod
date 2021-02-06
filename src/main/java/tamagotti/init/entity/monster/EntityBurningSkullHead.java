package tamagotti.init.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tamagotti.util.TMobLootTable;

public class EntityBurningSkullHead extends EntityMob {

	int tick = 0;
	private float heightOffset = 1F;
    private int heightOffsetUpdateTime;
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.<Byte>createKey(EntityBurningSkullHead.class, DataSerializers.BYTE);

	public EntityBurningSkullHead (World world) {
		super(world);
        this.setPathPriority(PathNodeType.WATER, 8.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.isImmuneToFire = true;
        this.experienceValue = 50;
	}

	@Override
	public double getMountedYOffset() {
		return 1.75D;
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.325D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

	@Override
	protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
	protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6000000238418579D;
        }

        if (this.world.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }
            tick++;
    		if (tick >= 60) {
    			tick = 0;
    			for (int i = 0; i < 16; i++) {
    				float f1 = (float) this.posX - 0.5F + rand.nextFloat();
    				float f2 = (float) ((float) this.posY + 1F + rand.nextFloat() * 0.8);
    				float f3 = (float) this.posZ - 0.5F + rand.nextFloat();
    				world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, 0, 0, 0);
    			}
    		}
        }
        super.onLivingUpdate();
    }

    public static void registerFixesBlaze(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityBurningSkullHead.class);
    }

    @Override
	protected void initEntityAI() {
        this.tasks.addTask(4, new EntityBurningSkullHead.AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
	protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote){
    		EntityItem drop = new EntityItem(world, this.posX, this.posY, this.posZ, TMobLootTable.loot(2));
            world.spawnEntity(drop);
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
	public float getBrightness() {
        return 1.0F;
    }

    @Override
	protected void updateAITasks() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }
        EntityLivingBase entity = this.getAttackTarget();
        if (entity != null && entity.posY + entity.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset)  {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }

    @Override
	public void fall(float distance, float damageMultiplier) {}

    @Override
	public boolean isBurning() {
        return this.isCharged();
    }

    @Override
	@Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_BLAZE;
    }

    public boolean isCharged() {
        return (this.dataManager.get(ON_FIRE).byteValue() & 1) != 0;
    }

    public void setOnFire(boolean onFire) {
        byte b0 = this.dataManager.get(ON_FIRE).byteValue();
        if (onFire) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }
        this.dataManager.set(ON_FIRE, Byte.valueOf(b0));
    }

    @Override
	protected boolean isValidLightLevel() {
        return true;
    }

    static class AIFireballAttack extends EntityAIBase {

            private final EntityBurningSkullHead blaze;
            private int attackStep;
            private int attackTime;

            public AIFireballAttack(EntityBurningSkullHead blazeIn) {
                this.blaze = blazeIn;
                this.setMutexBits(3);
            }

            @Override
			public boolean shouldExecute() {
                EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
                return entitylivingbase != null && entitylivingbase.isEntityAlive();
            }

            @Override
			public void startExecuting() {
                this.attackStep = 0;
            }

            @Override
			public void resetTask() {
                this.blaze.setOnFire(false);
            }

            @Override
			public void updateTask() {
                --this.attackTime;
                EntityLivingBase entity = this.blaze.getAttackTarget();
                double d0 = this.blaze.getDistanceSq(entity);
                if (d0 < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 16;
                        this.blaze.attackEntityAsMob(entity);
                    }
                    this.blaze.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, 1.0D);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
                    double d1 = entity.posX - this.blaze.posX;
                    double d2 = entity.getEntityBoundingBox().minY + entity.height / 2.0F - (this.blaze.posY + this.blaze.height / 2.0F);
                    double d3 = entity.posZ - this.blaze.posZ;
                    if (this.attackTime <= 0) {
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 40;
                            this.blaze.setOnFire(true);
                        } else if (this.attackStep <= 5) {
                            this.attackTime = 6;
                        } else {
                            this.attackTime = 70;
                            this.attackStep = 0;
                            this.blaze.setOnFire(false);
                        }
                        if (this.attackStep > 1) {
                            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                            this.blaze.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
                            EntitySmallFireball fireball[] = new EntitySmallFireball[6];
                            for (int i = 0; i < fireball.length; ++i) {
                            	fireball[i] = new EntitySmallFireball(this.blaze.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * f, d2, d3 + this.blaze.getRNG().nextGaussian() * f);
                            	fireball[i].posY = this.blaze.posY + this.blaze.height / 2.0F + 0.5D;
                            	fireball[i].setFire(200);
                                this.blaze.world.spawnEntity(fireball[i]);
                            }
                        }
                    }
                    this.blaze.getLookHelper().setLookPositionWithEntity(entity, 10.0F, 10.0F);
                } else {
                    this.blaze.getNavigator().clearPath();
                    this.blaze.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, 1.0D);
                }
                super.updateTask();
            }

            private double getFollowDistance() {
                IAttributeInstance instance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
                return instance == null ? 16.0D : instance.getAttributeValue();
            }
        }
}
