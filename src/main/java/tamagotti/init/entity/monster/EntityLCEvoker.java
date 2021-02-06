package tamagotti.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import tamagotti.util.TMobLootTable;

public class EntityLCEvoker extends EntitySpellcasterIllager {

	private EntitySheep sTarget;
	public int deathTicks = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.PINK, BossInfo.Overlay.NOTCHED_10);

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityLCEvoker(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		this.experienceValue = 250;
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, 8.0F);
		this.setPathPriority(PathNodeType.LAVA, 8.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
	}

	//えーあい
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityLCEvoker.AICastingSpell());
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
		this.tasks.addTask(4, new EntityLCEvoker.AISummonSpell());
		this.tasks.addTask(5, new EntityLCEvoker.AIAttackSpell());
		this.tasks.addTask(7, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityLCEvoker.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
		this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(256.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(64.0D);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	public static void registerFixesEvoker(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityLCEvoker.class);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_EVOCATION_ILLAGER;
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		this.motionY *= 0.25D;

		EntityLivingBase entity = this.getAttackTarget();

		if (entity != null) {
			if (this.posY < entity.posY || this.posY < entity.posY + 2.0D) {
				if (this.motionY < 0.0D) {
					this.motionY = 0.0D;
				}
				this.motionY += (0.5D - this.motionY) * 0.25D;
			}
		}
	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;
		this.motionY += 0.035;

		if (this.deathTicks >= 10 && this.deathTicks <= 50) {
			if (this.deathTicks % 8 == 0) {
			    this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.5F, 1.0F);
			}
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f,
					this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
		}
		this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
		if (this.deathTicks == 50 && !this.world.isRemote) {
			EntityLCEvokerDep entity = new EntityLCEvokerDep(this.world);
			entity.setLocationAndAngles(this.posX, this.posY + 0.75F, this.posZ, this.rotationYaw, 0.0F);
			this.world.spawnEntity(entity);
			this.setDead();
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.world.spawnEntity(new EntityItem(world, this.posX, this.posY, this.posZ, TMobLootTable.loot(0)));
		}
    }

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.getHealth() >= 0) {
			Random rand = new Random();
			for (int i = 0; i < 4; i++) {
				world.spawnParticle(EnumParticleTypes.PORTAL,
						this.posX + (rand.nextDouble() - 0.5D), this.posY + rand.nextDouble(),
						this.posZ + (rand.nextDouble() - 0.5D), (rand.nextDouble() - 0.5D) * 2.0D,
						-rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
			}
		}
	}

	@Override
	public boolean isOnSameTeam(Entity entity) {
		if (entity == null) {
			return false;
		} else if (entity == this || super.isOnSameTeam(entity)) {
			return true;
		} else if (entity instanceof EntityZombieMaster) {
			return this.isOnSameTeam(((EntityZombieMaster) entity).getOwner());
		} else if (entity instanceof EntityLivingBase
				&& ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER) {
			return this.getTeam() == null && entity.getTeam() == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {
		if (src == DamageSource.FALL || src.isExplosion() ||
				src.getTrueSource() instanceof EntityLCEvoker ||
				src.getTrueSource() instanceof EntityLCEvokerDep) {
			return false;
		}
		super.attackEntityFrom(src, amount);
		return true;
	}

	public boolean isArmored() {
		return this.getHealth() <= this.getMaxHealth() / 3.0F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.EVOCATION_ILLAGER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damage) {
		return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
	}

	private void setWololoTarget(@Nullable EntitySheep sTarget) {
		this.sTarget = sTarget;
	}

	@Nullable
	private EntitySheep getWololoTarget() {
		return this.sTarget;
	}

	@Override
	protected SoundEvent getSpellSound() {
		return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() {}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell {

		private AIAttackSpell() {
			super();
		}

		@Override
		protected int getCastingTime() {
			return 40;
		}

		@Override
		protected int getCastingInterval() {
			return 100;
		}

		@Override
		protected void castSpell() {

			EntityLivingBase living = EntityLCEvoker.this.getAttackTarget();
			double d0 = Math.min(living.posY, EntityLCEvoker.this.posY);
			double d1 = Math.max(living.posY, EntityLCEvoker.this.posY) + 1.0D;
			float f = (float) MathHelper.atan2(living.posZ - EntityLCEvoker.this.posZ,
					living.posX - EntityLCEvoker.this.posX);

			if (EntityLCEvoker.this.getDistanceSq(living) < 9.0D) {

				for (int i = 0; i < 16; ++i) {
					float f1 = f + i * (float) Math.PI * 0.4F;
					this.spawnFangs(EntityLCEvoker.this.posX + MathHelper.cos(f1) * 1.5D,
							EntityLCEvoker.this.posZ + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
				}

				for (int k = 0; k < 24; ++k) {
					float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
					this.spawnFangs(EntityLCEvoker.this.posX + MathHelper.cos(f2) * 2.5D,
							EntityLCEvoker.this.posZ + MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
				}

			} else {
				for (int l = 0; l < 32; ++l) {
					double d2 = 1.25D * (l + 1);
					int j = 1 * l;
					this.spawnFangs(EntityLCEvoker.this.posX + MathHelper.cos(f) * d2,
							EntityLCEvoker.this.posZ + MathHelper.sin(f) * d2, d0, d1, f, j);
				}
			}
		}

		private void spawnFangs(double par1, double par2, double par3, double par4, float par5, int par6) {

			BlockPos pos = new BlockPos(par1, par4, par2);
			boolean flag = false;
			double d0 = 0.0D;

			while (true) {
				if (!EntityLCEvoker.this.world.isBlockNormalCube(pos, true)
						&& EntityLCEvoker.this.world.isBlockNormalCube(pos.down(), true)) {
					if (!EntityLCEvoker.this.world.isAirBlock(pos)) {
						IBlockState state = EntityLCEvoker.this.world.getBlockState(pos);
						AxisAlignedBB aabb = state.getCollisionBoundingBox(EntityLCEvoker.this.world, pos);

						if (aabb != null) {
							d0 = aabb.maxY;
						}
					}

					flag = true;
					break;
				}

				pos = pos.down();

				if (pos.getY() < MathHelper.floor(par3) - 1) {
					break;
				}
			}

			if (flag) {
				EntityEvokerFangs entity = new EntityEvokerFangs(EntityLCEvoker.this.world, par1,
						pos.getY() + d0, par2, par5, par6, EntityLCEvoker.this);
				EntityLCEvoker.this.world.spawnEntity(entity);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.FANGS;
		}
	}

	class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {
		private AICastingSpell() {
			super();
		}

		@Override
		public void updateTask() {

			if (EntityLCEvoker.this.getAttackTarget() != null) {
				EntityLCEvoker.this.getLookHelper().setLookPositionWithEntity(EntityLCEvoker.this.getAttackTarget(),
						EntityLCEvoker.this.getHorizontalFaceSpeed(),
						EntityLCEvoker.this.getVerticalFaceSpeed());

			} else if (EntityLCEvoker.this.getWololoTarget() != null) {
				EntityLCEvoker.this.getLookHelper().setLookPositionWithEntity(EntityLCEvoker.this.getWololoTarget(),
						EntityLCEvoker.this.getHorizontalFaceSpeed(),
						EntityLCEvoker.this.getVerticalFaceSpeed());
			}
		}
	}

	class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell {

		private AISummonSpell() {
			super();
		}

		@Override
		public boolean shouldExecute() {

			if (!super.shouldExecute()) {
				return false;
			} else {
				int i = EntityLCEvoker.this.world
						.getEntitiesWithinAABB(EntityZombieMaster.class,
								EntityLCEvoker.this.getEntityBoundingBox().grow(16.0D))
						.size();
				return EntityLCEvoker.this.rand.nextInt(8) + 1 > i;
			}
		}

		@Override
		protected int getCastingTime() {
			return 100;
		}

		@Override
		protected int getCastingInterval() {
			return 340;
		}

		@Override
		protected void castSpell() {

			for (int i = 0; i < 3; ++i) {
				BlockPos pos = (new BlockPos(EntityLCEvoker.this)).add(-2 + EntityLCEvoker.this.rand.nextInt(5), 1,
						-2 + EntityLCEvoker.this.rand.nextInt(5));
				EntityZombieMaster vex = new EntityZombieMaster(EntityLCEvoker.this.world);
				vex.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				vex.onInitialSpawn(EntityLCEvoker.this.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
				vex.setOwner(EntityLCEvoker.this);
				EntityLCEvoker.this.world.spawnEntity(vex);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
		}
	}
}
