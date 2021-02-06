package tamagotti.init.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import tamagotti.init.PotionInit;
import tamagotti.util.TMobLootTable;

public class EntityKumongous extends EntitySpider {

	public EntityKumongous(World world) {
		super(world);
        this.experienceValue = 70;
        this.setSize(1.862F, 1.197F);
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.365D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) entity;
				(living).addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 1));
				(living).addPotionEffect(new PotionEffect(PotionInit.sticky, 300, 1));
			}
		}
		return true;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY, this.posZ, TMobLootTable.loot(5)));
		}
    }

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (src.getTrueSource() instanceof EntitySkullFlame) {
			return false;
		}
		super.attackEntityFrom(src, amount);
		return true;
	}

	@Override
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData living) {

		living = super.onInitialSpawn(difficulty, living);
		float f = difficulty.getClampedAdditionalDifficulty();
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.35F * f);

		if (this.world.rand.nextFloat() < 0.03F) {
			EntitySkullFlame chicken = new EntitySkullFlame(this.world);
			chicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			chicken.onInitialSpawn(difficulty, (IEntityLivingData) null);
			chicken.setChickenJockey(true);
			this.world.spawnEntity(chicken);
			chicken.startRiding(this);
		}

    	return super.onInitialSpawn(difficulty, living);
	}
}
