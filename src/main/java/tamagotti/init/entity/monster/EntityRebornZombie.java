package tamagotti.init.entity.monster;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import tamagotti.init.ItemInit;
import tamagotti.util.TMobLootTable;

public class EntityRebornZombie extends EntityZombie {

    private EntityLiving owner;
    private int tickTime = 0;

	public EntityRebornZombie(World worldIn) {
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
        this.targetTasks.addTask(7, new EntityRebornZombie.AICopyOwnerTarget(this));
		this.applyEntityAI();
	}

	@Override
	public void onLivingUpdate() {

		this.tickTime++;
		if (this.tickTime % 80 == 0) {
			this.tickTime = 0;
			List<EntityZombieMaster> list = this.world.getEntitiesWithinAABB(EntityZombieMaster.class, this.getEntityBoundingBox().grow(12.5D, 6D, 12.5D));
			if (list.isEmpty()) { return; }

			for (EntityZombieMaster entity : list) {
				entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 4));
			}
		}
		super.onLivingUpdate();
    }

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
    }

	@Override
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData living) {

    	this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemInit.tshieldc));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

		living = super.onInitialSpawn(difficulty, living);
		float f = difficulty.getClampedAdditionalDifficulty();
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

		if (this.world.rand.nextFloat() < 0.075F) {
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
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemInit.tshieldc));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
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

				EntityItem drop1 = entityDropItem(new ItemStack(Items.ENCHANTED_BOOK), 1);
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
		Entity entity = src.getImmediateSource();

		if (!this.canGurd()) {
			if (!(entity instanceof EntityPlayer) || entity instanceof EntityArrow || entity instanceof EntityThrowable) {
				this.world.playSound(null, new BlockPos(this), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.AMBIENT, 0.5F, 1.0F);
				return false;
			}
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
			return EntityRebornZombie.this.owner != null && EntityRebornZombie.this.owner.getAttackTarget() != null
					&& this.isSuitableTarget(EntityRebornZombie.this.owner.getAttackTarget(), false);
		}

		@Override
		public void startExecuting() {
			EntityRebornZombie.this.setAttackTarget(EntityRebornZombie.this.owner.getAttackTarget());
			super.startExecuting();
		}
	}

	// 盾を持ってるかどうかを判定
	public boolean canGurd() {
		return this.getHeldItemMainhand().getItem() == ItemInit.tshieldc || this.getHeldItemOffhand().getItem() == ItemInit.tshieldc ? false : true;
	}
}
