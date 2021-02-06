package tamagotti.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TMobLootTable;

public class EntityZombieMaster extends EntityZombie {

	public int tick = 0;
    private EntityLiving owner;

	public EntityZombieMaster(World worldIn) {
		super(worldIn);
        this.experienceValue = 160;
        this.setPathPriority(PathNodeType.DAMAGE_OTHER, 0.0F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(7, new EntityZombieMaster.AICopyOwnerTarget(this));
		this.applyEntityAI();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		tick++;
		if (tick >= 1200) {
			tick = 0;
			Random rand = new Random();
			if (rand.nextInt(8) == 0) {
				for (int i = 0; i <= 3; i++) {
					int xRand = rand.nextInt(10) - 5;
					int zRand = rand.nextInt(10) - 5;
					int sRand = rand.nextInt(4);

					if (!world.isRemote) {
						switch (sRand) {
						case 0:
							EntityZombie zom = new EntityZombie(world);
							zom.setLocationAndAngles(this.posX + xRand, this.posY + 1, this.posZ + zRand, this.rotationYaw, 0.0F);
							zom.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottisword));
							world.spawnEntity(zom);
							if (rand.nextInt(3) == 0) {
								zom.setChild(true);
							}
							break;
						case 1:
							EntityHusk husk = new EntityHusk(world);
							husk.setLocationAndAngles(this.posX + xRand, this.posY + 1, this.posZ + zRand, this.rotationYaw, 0.0F);
							husk.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottisword));
							world.spawnEntity(husk);
							if (rand.nextInt(3) == 0) {
								husk.setChild(true);
							}
							world.spawnEntity(husk);
							break;
						case 2:
							EntitySkeleton skl = new EntitySkeleton(world);
							skl.setLocationAndAngles(this.posX + xRand, this.posY + 1, this.posZ + zRand, this.rotationYaw, 0.0F);
							skl.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottisword));
							world.spawnEntity(skl);
							break;
						case 3:
							EntityStray str = new EntityStray(world);
							str.setLocationAndAngles(this.posX + xRand, this.posY + 1, this.posZ + zRand, this.rotationYaw, 0.0F);
							str.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottisword));
							world.spawnEntity(str);
							break;
						}
					}
				}
			}
		}
    }

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
    }

	@Override
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData living) {

    	this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tamagottiswordcustom));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemInit.copper_helmet));

		living = super.onInitialSpawn(difficulty, living);
		float f = difficulty.getClampedAdditionalDifficulty();
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

		if (this.world.rand.nextFloat() < 0.05F) {
			EntityTChicken chicken = new EntityTChicken(this.world);
			chicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			chicken.onInitialSpawn(difficulty, (IEntityLivingData) null);
			chicken.setChickenJockey(true);
			this.world.spawnEntity(chicken);
			this.startRiding(chicken);
		}

    	return super.onInitialSpawn(difficulty, living);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemInit.tamagottiswordcustom));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemInit.copper_helmet));
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			world.spawnEntity(new EntityItem(world, this.posX, this.posY, this.posZ, TMobLootTable.loot(0)));

			if (rand.nextInt(4) == 0) {
				Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(getEntityWorld().rand);
	    		int maxPower = enchantment.getMaxLevel();
	    		int randomPower = 1 + rand.nextInt(maxPower);

				EntityItem drop1 = this.entityDropItem(new ItemStack(Items.ENCHANTED_BOOK), 1);
				ItemStack stack = drop1.getItem();
				if (stack != null && randomPower > 0 && enchantment != null) {
					ItemEnchantedBook book = (ItemEnchantedBook) stack.getItem();
					ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment, randomPower));
					stack = new ItemStack(book, 1);
				}
			}
		}
    }

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {
		if (src.getTrueSource() instanceof EntityLCEvoker ||
				src.getTrueSource() instanceof EntityLCEvokerDep ||
			src.getTrueSource() instanceof EntityTChicken) {
			return false;
		}
		super.attackEntityFrom(src, damage);
		return true;
	}

	public EntityLiving getOwner() {
		return this.owner;
	}

	public void setOwner(EntityLiving owner) {
		this.owner = owner;
	}

	class AICopyOwnerTarget extends EntityAITarget {
		public AICopyOwnerTarget(EntityCreature creature) {
			super(creature, false);
		}

		@Override
		public boolean shouldExecute() {
			return EntityZombieMaster.this.owner != null && EntityZombieMaster.this.owner.getAttackTarget() != null
					&& this.isSuitableTarget(EntityZombieMaster.this.owner.getAttackTarget(), false);
		}

		@Override
		public void startExecuting() {
			EntityZombieMaster.this.setAttackTarget(EntityZombieMaster.this.owner.getAttackTarget());
			super.startExecuting();
		}
	}
}
