package tamagotti.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityShot extends EntityTArrow {

	public EntityShot(World worldIn) {
		super(worldIn);
	}

	public EntityShot(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityShot(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
}