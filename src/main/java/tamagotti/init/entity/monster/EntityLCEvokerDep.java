package tamagotti.init.entity.monster;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IProjectile;
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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
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
import tamagotti.init.ItemInit;
import tamagotti.init.entity.projectile.EntityExBall;
import tamagotti.util.TMobLootTable;

public class EntityLCEvokerDep extends EntitySpellcasterIllager {

	public int dethTick = 0;
	public int deathTicks = 0;
	EntityExBall ex = new EntityExBall(world);
	private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityLCEvokerDep(World worldIn) {
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
		this.tasks.addTask(1, new EntityLCEvokerDep.AICastingSpell());
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
		this.tasks.addTask(4, new EntityLCEvokerDep.AISummonSpell());
		this.tasks.addTask(5, new EntityLCEvokerDep.AIAttackSpell(this));
		this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityLCEvokerDep.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
		this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.75D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(128.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
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
		EntityLiving.registerFixesMob(fixer, EntityLCEvokerDep.class);
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
		if (!this.world.isRemote) {
			world.spawnEntity(new EntityItem(world, this.posX, this.posY, this.posZ, TMobLootTable.loot(4)));
			world.spawnEntity(new EntityItem(world, this.posX, this.posY, this.posZ, new ItemStack(ItemInit.tcrystal)));

			for (int i = 0; i < 4; i++) {
				Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(getEntityWorld().rand);
	    		int maxPower = enchantment.getMaxLevel() + 2;
	    		int randomPower = 3 + rand.nextInt(maxPower);

				ItemStack stack = this.entityDropItem(new ItemStack(Items.ENCHANTED_BOOK), 1).getItem();
				if (stack != null && randomPower > 0 && enchantment != null) {
					ItemEnchantedBook book = (ItemEnchantedBook) stack.getItem();
					ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment, randomPower));
					stack = new ItemStack(book, 1);
				}
			}
		}
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

		if (this.deathTicks >= 10 && this.deathTicks <= 80) {
			if (this.deathTicks % 8 == 0) {
			    this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.5F, 1.0F);
			}
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f,
					this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
		}
		this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
		if (this.deathTicks == 80 && !this.world.isRemote) {
			this.setDead();
		}
	}

	@Override
	public void onUpdate() {
		if (this.isArmored()) {
			this.barrier();
		}
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

	// 弾のえんちちー殺す処理
	public void barrier () {
		if (!world.isRemote) {
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(2));
			if (list.isEmpty()) { return; }

			for (Entity entity : list) {
				if (entity != null && !(entity instanceof EntityExBall)) {
					boolean flag = false;
					Entity shooter = null;
					if (entity instanceof EntityArrow) {
						EntityArrow arrow = (EntityArrow) entity;
						if (arrow.shootingEntity != null) {
							shooter = arrow.shootingEntity;
						}
						flag = true;
					} else if (entity instanceof EntityThrowable) {
						EntityThrowable arrow = (EntityThrowable) entity;
						if (arrow.getThrower() != null) {
							shooter = arrow.getThrower();
						}
						flag = true;
					} else if (entity instanceof EntityFireball) {
						EntityFireball arrow = (EntityFireball) entity;
						if (arrow.shootingEntity != null) {
							shooter = arrow.shootingEntity;
						}
						flag = true;
					} else if (entity instanceof IProjectile) {
						flag = true;
					}
					if (flag) {
						if (shooter instanceof Entity || shooter == null) {
							this.world.playSound(null, new BlockPos(entity), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.AMBIENT, 0.5F, 1.0F);
							entity.setDead();
						}
					}
				}
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

		if (amount >= 10) {
			amount = 10;
			if (this.isArmored()) {
				amount = 7;
			}
		}
		super.attackEntityFrom(src, amount);
		return true;
	}

	public boolean isArmored() {
		return this.getHealth() <= this.getMaxHealth() / 2.0F;
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

        private final EntityLCEvokerDep lce;

		private AIAttackSpell(EntityLCEvokerDep dep) {
			super();
			this.lce = dep;
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

			EntityLivingBase living = EntityLCEvokerDep.this.getAttackTarget();
			double d0 = Math.min(living.posY, EntityLCEvokerDep.this.posY);
			double d1 = Math.max(living.posY, EntityLCEvokerDep.this.posY) + 1.0D;
			float f = (float) MathHelper.atan2(living.posZ - EntityLCEvokerDep.this.posZ,
					living.posX - EntityLCEvokerDep.this.posX);

			if (EntityLCEvokerDep.this.getDistanceSq(living) < 9.0D) {

				for (int i = 0; i < 6; ++i) {
					float f1 = f + i * (float) Math.PI * 0.4F;
					this.spawnFangs(EntityLCEvokerDep.this.posX + MathHelper.cos(f1) * 1.5D,
							EntityLCEvokerDep.this.posZ + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
				}

				for (int k = 0; k < 10; ++k) {
					float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
					this.spawnFangs(EntityLCEvokerDep.this.posX + MathHelper.cos(f2) * 2.5D,
							EntityLCEvokerDep.this.posZ + MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
				}

			} else {
				for (int l = 0; l < 16; ++l) {
					double d2 = 1.25D * (l + 1);
					int j = 1 * l;
					this.spawnFangs(EntityLCEvokerDep.this.posX + MathHelper.cos(f) * d2,
							EntityLCEvokerDep.this.posZ + MathHelper.sin(f) * d2, d0, d1, f, j);
				}
			}
		}

		private void spawnFangs(double par1, double par2, double par3, double par4, float par5, int par6) {

			BlockPos pos = new BlockPos(par1, par4, par2);
			boolean flag = false;
			double d0 = 0.0D;
            World world = this.lce.world;

			while (true) {
				if (!world.isBlockNormalCube(pos, true)
						&& world.isBlockNormalCube(pos.down(), true)) {
					if (!world.isAirBlock(pos)) {
						IBlockState state = world.getBlockState(pos);
						AxisAlignedBB aabb = state.getCollisionBoundingBox(world, pos);

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
                EntityLivingBase entity = this.lce.getAttackTarget();

                world.playEvent(null, 1018, new BlockPos(this.lce), 0);

				if (this.lce.isArmored()) {
					Random rand = new Random();
					EntityExBall fireball[] = new EntityExBall[10];
					for (int i = 0; i < fireball.length; ++i) {
						double x = entity.posX - 8 + rand.nextDouble() * 16;
						double y = entity.posY + 6 + rand.nextDouble() * 8;
						double z = entity.posZ - 8 + rand.nextDouble() * 16;
						fireball[i] = new EntityExBall(world, this.lce, 0, -1.25, 0);
						fireball[i].setPosition(x, y, z);
						world.spawnEntity(fireball[i]);
					}
				} else {
	                double d1 = entity.posX - this.lce.posX;
	                double d2 = entity.getEntityBoundingBox().minY + entity.height / 2.0F - (this.lce.posY + this.lce.height / 2.0F);
	                double d3 = entity.posZ - this.lce.posZ;
					float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;

					Random rand = this.lce.getRNG();

					EntityExBall fireball[] = new EntityExBall[4];
					for (int i = 0; i < fireball.length; ++i) {
						fireball[i] = new EntityExBall(world, this.lce, d1 + rand.nextGaussian() * f, d2, d3 + rand.nextGaussian() * f);
						fireball[i].posY = this.lce.posY + this.lce.height / 2.0F + 0.5D;
						world.spawnEntity(fireball[i]);
					}
				}
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

			if (EntityLCEvokerDep.this.getAttackTarget() != null) {
				EntityLCEvokerDep.this.getLookHelper().setLookPositionWithEntity(EntityLCEvokerDep.this.getAttackTarget(),
						EntityLCEvokerDep.this.getHorizontalFaceSpeed(),
						EntityLCEvokerDep.this.getVerticalFaceSpeed());

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
				int i = EntityLCEvokerDep.this.world
						.getEntitiesWithinAABB(EntityZombieMaster.class,
								EntityLCEvokerDep.this.getEntityBoundingBox().grow(16.0D)) .size();
				return EntityLCEvokerDep.this.rand.nextInt(8) + 1 > i;
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

			for (int i = 0; i < 2; ++i) {
				BlockPos pos = (new BlockPos(EntityLCEvokerDep.this)).add(-2 + EntityLCEvokerDep.this.rand.nextInt(5), 1,
						-2 + EntityLCEvokerDep.this.rand.nextInt(5));
				EntityZombieMaster vex = new EntityZombieMaster(EntityLCEvokerDep.this.world);
				vex.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
				vex.onInitialSpawn(EntityLCEvokerDep.this.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
				vex.setOwner(EntityLCEvokerDep.this);
				vex.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottiaxecustom));
				EntityLCEvokerDep.this.world.spawnEntity(vex);
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
